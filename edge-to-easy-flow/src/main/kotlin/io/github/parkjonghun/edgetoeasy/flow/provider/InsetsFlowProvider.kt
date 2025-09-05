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
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Provider class that manages StateFlow and Channel instances for WindowInsets updates.
 * This class efficiently manages insets monitoring by reusing providers for the same view-systemArea combination.
 * It automatically handles lifecycle management by cleaning up resources when views are detached.
 *
 * The provider creates and manages:
 * - A StateFlow that holds the current insets value and emits updates
 * - A Channel with unlimited capacity that receives all insets updates
 * - A Flow from the Channel for backpressure-free consumption
 *
 * Key features:
 * - Automatic lifecycle management (cleanup when view detached)
 * - Singleton pattern per (View, SystemArea) combination
 * - Immediate current value availability through StateFlow
 * - Unlimited buffering through Channel to prevent dropped updates
 *
 * @param view The View to monitor for insets changes
 * @param systemArea The specific system area to track (defaults to SystemBar)
 */
class InsetsFlowProvider private constructor(
    private val view: View,
    private val systemArea: SystemArea = SystemArea.SystemBar,
) {
    private val _insetsStateFlow = MutableStateFlow(getCurrentInsets())

    /**
     * StateFlow that holds the current WindowInsets value and emits updates when insets change.
     * Always has a current value available immediately via StateFlow.value.
     */
    val insetsStateFlow: StateFlow<Insets> = _insetsStateFlow.asStateFlow()

    private val _insetsChannel = Channel<Insets>(Channel.UNLIMITED)

    /**
     * Channel that receives WindowInsets updates. Has unlimited capacity to ensure no updates are dropped.
     * Use this for direct Channel operations like consuming in a for loop.
     */
    val insetsChannel: Channel<Insets> = _insetsChannel

    /**
     * Flow created from the Channel that receives WindowInsets updates.
     * Provides backpressure-free consumption with unlimited buffering.
     */
    val insetsChannelFlow: Flow<Insets> = _insetsChannel.receiveAsFlow()

    private var listener: OnApplyWindowInsetsListener? = null

    init {
        setupInsetsListener()
    }

    /**
     * Gets the current WindowInsets for the specified system area.
     * Returns Insets.NONE if no window insets are available.
     */
    private fun getCurrentInsets(): Insets {
        val windowInsets = ViewCompat.getRootWindowInsets(view)
        return windowInsets?.let { SystemAreaInsetsMapper.getInsetsForSystemArea(it, systemArea) } ?: Insets.NONE
    }

    /**
     * Sets up the WindowInsets listener that updates both StateFlow and Channel
     * when insets change. Also sends the initial insets value to the Channel.
     */
    private fun setupInsetsListener() {
        listener =
            OnApplyWindowInsetsListener { _, insets ->
                val targetInsets = SystemAreaInsetsMapper.getInsetsForSystemArea(insets, systemArea)
                _insetsStateFlow.value = targetInsets
                _insetsChannel.trySend(targetInsets)
                insets
            }
        ViewCompat.setOnApplyWindowInsetsListener(view, listener)

        // Send initial value to channel
        _insetsChannel.trySend(getCurrentInsets())
    }

    /**
     * Cleans up resources by removing the insets listener and closing the Channel.
     * Called automatically when the view is detached from window.
     */
    public fun destroy() {
        ViewCompat.setOnApplyWindowInsetsListener(view, null)
        listener = null
        _insetsChannel.close()
    }

    companion object {
        private val providers = mutableMapOf<Pair<View, SystemArea>, InsetsFlowProvider>()

        /**
         * Gets an existing InsetsFlowProvider for the specified view and system area,
         * or creates a new one if none exists. This ensures efficient resource usage
         * by reusing providers for the same view-systemArea combination.
         *
         * The provider automatically manages its lifecycle:
         * - It's created when first requested for a view-systemArea pair
         * - It's automatically destroyed and removed when the view is detached from window
         * - Multiple calls with the same parameters return the same provider instance
         *
         * @param view The View to monitor for insets changes
         * @param systemArea The specific system area to track (defaults to SystemBar)
         * @return InsetsFlowProvider instance for the specified view and system area
         */
        public fun getOrCreate(
            view: View,
            systemArea: SystemArea = SystemArea.SystemBar,
        ): InsetsFlowProvider {
            val key = view to systemArea
            return providers.getOrPut(key) {
                InsetsFlowProvider(view, systemArea).also { provider ->
                    view.addOnAttachStateChangeListener(
                        object : View.OnAttachStateChangeListener {
                            override fun onViewAttachedToWindow(v: View) {}

                            override fun onViewDetachedFromWindow(v: View) {
                                providers.entries.removeAll { (providerKey, providerValue) ->
                                    if (providerKey.first == v) {
                                        providerValue.destroy()
                                        true
                                    } else {
                                        false
                                    }
                                }
                                v.removeOnAttachStateChangeListener(this)
                            }
                        },
                    )
                }
            }
        }
    }
}
