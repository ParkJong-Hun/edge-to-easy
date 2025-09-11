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

import androidx.core.graphics.Insets
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for WindowInsets Flow providers that can create various reactive streams
 * for monitoring WindowInsets changes in different system areas.
 *
 * This interface allows for different implementations (e.g., shared listeners, managed listeners)
 * while providing a consistent API for consumers.
 */
public interface InsetsFlowProvider {

    /**
     * Creates a Flow that emits WindowInsets for the specified system area.
     * The Flow starts with the current insets value and emits updates when insets change.
     *
     * @param systemArea The system area to monitor for insets changes
     * @return Flow<Insets> that emits insets updates for the specified system area
     */
    public fun createFlow(systemArea: SystemArea): Flow<Insets>

    /**
     * Creates or returns a StateFlow for the specified system area.
     * StateFlow always has a current value and emits updates to all collectors.
     *
     * @param systemArea The system area to monitor for insets changes
     * @return StateFlow<Insets> that holds and emits insets updates for the specified system area
     */
    public fun getStateFlow(systemArea: SystemArea): StateFlow<Insets>

    /**
     * Creates a Channel that receives WindowInsets updates for the specified system area.
     * The Channel typically has unlimited buffer capacity to ensure no insets updates are dropped.
     *
     * @param systemArea The system area to monitor for insets changes
     * @return Channel<Insets> that receives insets updates for the specified system area
     */
    public fun createChannel(systemArea: SystemArea): Channel<Insets>

    /**
     * Creates a Flow from a Channel that receives WindowInsets updates for the specified system area.
     * This is similar to createFlow() but uses a Channel internally for buffering insets updates.
     *
     * @param systemArea The system area to monitor for insets changes
     * @return Flow<Insets> that emits insets updates from the underlying Channel
     */
    public fun createChannelFlow(systemArea: SystemArea): Flow<Insets>
}
