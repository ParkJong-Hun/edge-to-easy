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

class InsetsFlowProvider private constructor(
    private val view: View,
    private val systemArea: SystemArea = SystemArea.SystemBar,
) {
    private val _insetsStateFlow = MutableStateFlow(getCurrentInsets())
    val insetsStateFlow: StateFlow<Insets> = _insetsStateFlow.asStateFlow()

    private val _insetsChannel = Channel<Insets>(Channel.UNLIMITED)
    val insetsChannel: Channel<Insets> = _insetsChannel
    val insetsChannelFlow: Flow<Insets> = _insetsChannel.receiveAsFlow()

    private var listener: OnApplyWindowInsetsListener? = null

    init {
        setupInsetsListener()
    }

    private fun getCurrentInsets(): Insets {
        val windowInsets = ViewCompat.getRootWindowInsets(view)
        return windowInsets?.let { SystemAreaInsetsMapper.getInsetsForSystemArea(it, systemArea) } ?: Insets.NONE
    }

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

    fun destroy() {
        ViewCompat.setOnApplyWindowInsetsListener(view, null)
        listener = null
        _insetsChannel.close()
    }

    companion object {
        private val providers = mutableMapOf<Pair<View, SystemArea>, InsetsFlowProvider>()

        fun getOrCreate(
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
