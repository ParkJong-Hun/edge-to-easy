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
package io.github.parkjonghun.edgetoeasy.flow.extension

import android.view.View
import androidx.core.graphics.Insets
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import io.github.parkjonghun.edgetoeasy.flow.provider.InsetsFlowProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Creates a Flow that emits WindowInsets for the specified system area whenever they change.
 * The Flow automatically starts with the current insets value and then emits updates when
 * window insets change (e.g., due to keyboard show/hide, orientation change, etc.).
 *
 * @param systemArea The system area to monitor for insets changes. Defaults to SystemArea.Everything.
 * @return Flow<Insets> that emits insets updates for the specified system area
 *
 * Usage examples:
 * ```kotlin
 * // Monitor all system insets (status bar, navigation bar, IME, cutouts)
 * view.insetsFlow()
 *     .onEach { insets ->
 *         // Handle insets change
 *         println("Insets changed: $insets")
 *     }
 *     .launchIn(lifecycleScope)
 *
 * // Monitor only keyboard (IME) insets
 * view.insetsFlow(SystemArea.Ime)
 *     .onEach { imeInsets ->
 *         val keyboardHeight = imeInsets.bottom
 *         // Adjust UI based on keyboard height
 *     }
 *     .launchIn(lifecycleScope)
 *
 * // Monitor navigation bar insets
 * view.insetsFlow(SystemArea.NavigationBar)
 *     .filter { it.bottom > 0 }
 *     .onEach { navInsets ->
 *         // Handle navigation bar insets
 *     }
 *     .launchIn(lifecycleScope)
 * ```
 */
public fun View.insetsFlow(systemArea: SystemArea = SystemArea.Everything): Flow<Insets> = InsetsFlowProvider.getOrCreate(this).getFlow(systemArea)

/**
 * Creates a StateFlow that holds the current WindowInsets for the specified system area
 * and automatically updates when insets change. StateFlow always has a current value
 * and emits updates to all collectors.
 *
 * @param systemArea The system area to monitor for insets changes. Defaults to SystemArea.Everything.
 * @return StateFlow<Insets> that holds and emits insets updates for the specified system area
 *
 * Usage examples:
 * ```kotlin
 * // Get StateFlow for all system insets
 * val insetsStateFlow = view.insetsStateFlow()
 * val currentInsets = insetsStateFlow.value // Get current value immediately
 *
 * insetsStateFlow
 *     .onEach { insets ->
 *         // Handle insets changes
 *         updatePadding(insets)
 *     }
 *     .launchIn(lifecycleScope)
 *
 * // Monitor keyboard insets with StateFlow
 * val keyboardStateFlow = view.insetsStateFlow(SystemArea.Ime)
 * keyboardStateFlow
 *     .map { it.bottom > 0 }
 *     .distinctUntilChanged()
 *     .onEach { isKeyboardVisible ->
 *         // Handle keyboard visibility changes
 *         animateKeyboardAware(isKeyboardVisible)
 *     }
 *     .launchIn(lifecycleScope)
 * ```
 */
public fun View.insetsStateFlow(systemArea: SystemArea = SystemArea.Everything): StateFlow<Insets> = InsetsFlowProvider.getOrCreate(this).getStateFlow(systemArea)

/**
 * Creates a Channel that receives WindowInsets updates for the specified system area.
 * The Channel has unlimited buffer capacity to ensure no insets updates are dropped.
 *
 * @param systemArea The system area to monitor for insets changes. Defaults to SystemArea.Everything.
 * @return Channel<Insets> that receives insets updates for the specified system area
 *
 * Usage examples:
 * ```kotlin
 * // Get Channel for system insets
 * val insetsChannel = view.insetsChannel()
 *
 * // Consume insets updates in a coroutine
 * lifecycleScope.launch {
 *     for (insets in insetsChannel) {
 *         // Process each insets update
 *         handleInsetsChange(insets)
 *     }
 * }
 *
 * // Use Channel for batch processing
 * val keyboardChannel = view.insetsChannel(SystemArea.Ime)
 * lifecycleScope.launch {
 *     keyboardChannel
 *         .consumeAsFlow()
 *         .sample(100) // Sample every 100ms to avoid too frequent updates
 *         .onEach { imeInsets ->
 *             adjustForKeyboard(imeInsets.bottom)
 *         }
 *         .launchIn(this)
 * }
 * ```
 */
public fun View.insetsChannel(systemArea: SystemArea = SystemArea.Everything): Channel<Insets> = InsetsFlowProvider.getOrCreate(this).getChannel(systemArea)

/**
 * Creates a Flow from a Channel that receives WindowInsets updates for the specified system area.
 * This is similar to insetsFlow() but uses a Channel internally for buffering insets updates.
 * The underlying Channel has unlimited capacity to ensure no updates are lost.
 *
 * @param systemArea The system area to monitor for insets changes. Defaults to SystemArea.Everything.
 * @return Flow<Insets> that emits insets updates from the underlying Channel
 *
 * Usage examples:
 * ```kotlin
 * // Get Flow from Channel for system insets
 * view.insetsChannelFlow()
 *     .onEach { insets ->
 *         // Handle each insets update
 *         println("Channel Flow insets: $insets")
 *     }
 *     .launchIn(lifecycleScope)
 *
 * // Use with operators for complex processing
 * view.insetsChannelFlow(SystemArea.NavigationBar)
 *     .debounce(50) // Debounce rapid changes
 *     .distinctUntilChanged()
 *     .onEach { navInsets ->
 *         updateNavigationBarSpacing(navInsets)
 *     }
 *     .launchIn(lifecycleScope)
 *
 * // Combine with other flows
 * combine(
 *     view.insetsChannelFlow(SystemArea.StatusBar),
 *     view.insetsChannelFlow(SystemArea.NavigationBar)
 * ) { statusInsets, navInsets ->
 *     statusInsets.top + navInsets.bottom
 * }.onEach { totalSystemHeight ->
 *     adjustContentHeight(totalSystemHeight)
 * }.launchIn(lifecycleScope)
 * ```
 */
public fun View.insetsChannelFlow(systemArea: SystemArea = SystemArea.Everything): Flow<Insets> = InsetsFlowProvider.getOrCreate(this).getChannel(systemArea).receiveAsFlow()
