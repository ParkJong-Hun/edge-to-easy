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
import io.mockk.mockk
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ViewExtensionsTest {

    private val mockView = mockk<View>(relaxed = true)

    @Test
    fun `insetsFlow returns Flow of correct type`() {
        val flow = mockView.insetsFlow()

        assertNotNull(flow)
        assertTrue(flow is Flow<Insets>)
    }

    @Test
    fun `insetsFlow with SystemArea returns Flow of correct type`() {
        val flow = mockView.insetsFlow(SystemArea.StatusBar)

        assertNotNull(flow)
        assertTrue(flow is Flow<Insets>)
    }

    @Test
    fun `insetsStateFlow returns StateFlow of correct type`() {
        val stateFlow = mockView.insetsStateFlow()

        assertNotNull(stateFlow)
        assertTrue(stateFlow is StateFlow<Insets>)
    }

    @Test
    fun `insetsStateFlow with SystemArea returns StateFlow of correct type`() {
        val stateFlow = mockView.insetsStateFlow(SystemArea.NavigationBar)

        assertNotNull(stateFlow)
        assertTrue(stateFlow is StateFlow<Insets>)
    }

    @Test
    fun `insetsChannel returns Channel of correct type`() {
        val channel = mockView.insetsChannel()

        assertNotNull(channel)
        assertTrue(channel is Channel<Insets>)
    }

    @Test
    fun `insetsChannel with SystemArea returns Channel of correct type`() {
        val channel = mockView.insetsChannel(SystemArea.IME)

        assertNotNull(channel)
        assertTrue(channel is Channel<Insets>)
    }

    @Test
    fun `insetsChannelFlow returns Flow of correct type`() {
        val flow = mockView.insetsChannelFlow()

        assertNotNull(flow)
        assertTrue(flow is Flow<Insets>)
    }

    @Test
    fun `insetsChannelFlow with SystemArea returns Flow of correct type`() {
        val flow = mockView.insetsChannelFlow(SystemArea.Everything)

        assertNotNull(flow)
        assertTrue(flow is Flow<Insets>)
    }

    @Test
    fun `extension functions work with all SystemArea types`() {
        val systemAreas = listOf(
            SystemArea.StatusBar,
            SystemArea.NavigationBar,
            SystemArea.Bottom,
            SystemArea.SystemBar,
            SystemArea.IME,
            SystemArea.Cutout,
            SystemArea.Everything,
            SystemArea.Top,
            SystemArea.TopFull,
            SystemArea.BottomFull,
        )

        systemAreas.forEach { systemArea ->
            assertNotNull(mockView.insetsFlow(systemArea))
            assertNotNull(mockView.insetsStateFlow(systemArea))
            assertNotNull(mockView.insetsChannel(systemArea))
            assertNotNull(mockView.insetsChannelFlow(systemArea))
        }
    }

    @Test
    fun `extension functions use default SystemArea when not specified`() {
        // These should not throw exceptions and should use SystemArea.Everything as default
        assertNotNull(mockView.insetsFlow())
        assertNotNull(mockView.insetsStateFlow())
        assertNotNull(mockView.insetsChannel())
        assertNotNull(mockView.insetsChannelFlow())
    }
}
