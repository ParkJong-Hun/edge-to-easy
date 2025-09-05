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
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import io.github.parkjonghun.edgetoeasy.core.util.SystemAreaInsetsMapper
import io.github.parkjonghun.edgetoeasy.flow.provider.InsetsFlowProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

fun View.insetsFlow(systemArea: SystemArea = SystemArea.Everything): Flow<Insets> =
    callbackFlow {
        val listener =
            OnApplyWindowInsetsListener { _, insets ->
                val targetInsets = SystemAreaInsetsMapper.getInsetsForSystemArea(insets, systemArea)
                trySend(targetInsets)
                insets
            }

        val currentInsets = ViewCompat
            .getRootWindowInsets(this@insetsFlow)
            ?.let { SystemAreaInsetsMapper.getInsetsForSystemArea(it, systemArea) }
            ?: Insets.NONE
        trySend(currentInsets)

        ViewCompat.setOnApplyWindowInsetsListener(this@insetsFlow, listener)

        awaitClose { ViewCompat.setOnApplyWindowInsetsListener(this@insetsFlow, null) }
    }.conflate()

fun View.insetsStateFlow(systemArea: SystemArea = SystemArea.Everything): StateFlow<Insets> =
    InsetsFlowProvider.getOrCreate(this, systemArea).insetsStateFlow

fun View.insetsChannel(systemArea: SystemArea = SystemArea.Everything): Channel<Insets> =
    InsetsFlowProvider.getOrCreate(this, systemArea).insetsChannel

fun View.insetsChannelFlow(systemArea: SystemArea = SystemArea.Everything): Flow<Insets> =
    InsetsFlowProvider.getOrCreate(this, systemArea).insetsChannelFlow
