/*
 * Copyright (C) 2025 Park Jong Hun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.parkjonghun.edgetoeasy.flow.provider

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import io.github.parkjonghun.edgetoeasy.core.util.SystemAreaInsetsMapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

/**
 * Shared provider that manages multiple flows for the same view with a single WindowInsets listener.
 * This prevents listener conflicts when multiple flows are created for the same view.
 */
class InsetsFlowProvider private constructor(
    private val view: View,
) {
    // Map of SystemArea to their respective channels/flows
    private val areaChannels = mutableMapOf<SystemArea, Channel<Insets>>()
    private val areaStateFlows = mutableMapOf<SystemArea, MutableStateFlow<Insets>>()
    private var listener: OnApplyWindowInsetsListener? = null
    private var activeFlowCount = 0

    /**
     * Gets a Flow for the specified system area. Creates the flow if it doesn't exist.
     * Multiple calls for the same SystemArea will return independent flows from the same source.
     */
    fun getFlow(systemArea: SystemArea): Flow<Insets> {
        activeFlowCount++

        val channel = areaChannels.getOrPut(systemArea) {
            Channel<Insets>(Channel.UNLIMITED)
        }

        // Set up listener if this is the first flow
        if (listener == null) {
            setupListener()
        }

        // Send current insets immediately
        val currentInsets = getCurrentInsets(systemArea)
        channel.trySend(currentInsets)

        return callbackFlow {
            // Subscribe to the shared channel
            val job = launch {
                for (insets in channel) {
                    trySend(insets)
                }
            }

            awaitClose {
                job.cancel()
                decrementActiveFlows()
            }
        }
    }

    /**
     * Gets a StateFlow for the specified system area. Creates the StateFlow if it doesn't exist.
     */
    fun getStateFlow(systemArea: SystemArea): StateFlow<Insets> {
        activeFlowCount++

        val stateFlow = areaStateFlows.getOrPut(systemArea) {
            MutableStateFlow(getCurrentInsets(systemArea))
        }

        // Set up listener if this is the first flow
        if (listener == null) {
            setupListener()
        }

        return stateFlow.asStateFlow()
    }

    /**
     * Gets a Channel for the specified system area. Creates the channel if it doesn't exist.
     */
    fun getChannel(systemArea: SystemArea): Channel<Insets> {
        activeFlowCount++

        val channel = areaChannels.getOrPut(systemArea) {
            Channel<Insets>(Channel.UNLIMITED)
        }

        // Set up listener if this is the first flow
        if (listener == null) {
            setupListener()
        }

        // Send current insets immediately
        val currentInsets = getCurrentInsets(systemArea)
        channel.trySend(currentInsets)

        return channel
    }

    /**
     * Notifies that a flow is no longer active. Cleans up resources if no flows are active.
     */
    fun decrementActiveFlows() {
        activeFlowCount--
        if (activeFlowCount <= 0) {
            cleanup()
        }
    }

    private fun getCurrentInsets(systemArea: SystemArea): Insets {
        val windowInsets = ViewCompat.getRootWindowInsets(view)
        return windowInsets?.let {
            SystemAreaInsetsMapper.getInsetsForSystemArea(it, systemArea)
        } ?: Insets.NONE
    }

    private fun setupListener() {
        listener = OnApplyWindowInsetsListener { _, windowInsets ->
            // Distribute insets to all active channels and state flows
            areaChannels.forEach { (systemArea, channel) ->
                val targetInsets = SystemAreaInsetsMapper.getInsetsForSystemArea(windowInsets, systemArea)
                channel.trySend(targetInsets)
            }

            areaStateFlows.forEach { (systemArea, stateFlow) ->
                val targetInsets = SystemAreaInsetsMapper.getInsetsForSystemArea(windowInsets, systemArea)
                stateFlow.value = targetInsets
            }

            windowInsets
        }

        ViewCompat.setOnApplyWindowInsetsListener(view, listener)
    }

    private fun cleanup() {
        // Close all channels
        areaChannels.values.forEach { channel ->
            channel.close()
        }
        areaChannels.clear()
        areaStateFlows.clear()

        // Remove listener
        ViewCompat.setOnApplyWindowInsetsListener(view, null)
        listener = null

        // Remove from providers map
        providers.remove(view)
    }

    companion object Companion {
        private val providers = mutableMapOf<View, InsetsFlowProvider>()

        /**
         * Gets or creates a shared provider for the specified view.
         */
        fun getOrCreate(view: View): InsetsFlowProvider = providers.getOrPut(view) {
            InsetsFlowProvider(view).also { provider ->
                // Set up cleanup when view is detached
                view.addOnAttachStateChangeListener(
                    object : View.OnAttachStateChangeListener {
                        override fun onViewAttachedToWindow(v: View) {}

                        override fun onViewDetachedFromWindow(v: View) {
                            providers.remove(v)?.cleanup()
                            v.removeOnAttachStateChangeListener(this)
                        }
                    },
                )
            }
        }
    }
}
