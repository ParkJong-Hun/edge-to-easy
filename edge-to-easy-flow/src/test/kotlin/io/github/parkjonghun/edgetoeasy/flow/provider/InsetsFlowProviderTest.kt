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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InsetsFlowProviderTest {

    private class TestInsetsFlowProvider : InsetsFlowProvider {
        private val testInsets = Insets.of(10, 20, 30, 40)
        private val stateFlows = mutableMapOf<SystemArea, MutableStateFlow<Insets>>()

        override fun createFlow(systemArea: SystemArea): Flow<Insets> = flowOf(testInsets)

        override fun getStateFlow(systemArea: SystemArea): StateFlow<Insets> = stateFlows.getOrPut(systemArea) {
            MutableStateFlow(testInsets)
        }

        override fun createChannel(systemArea: SystemArea): Channel<Insets> = Channel<Insets>(Channel.UNLIMITED).also { channel ->
            channel.trySend(testInsets)
        }

        override fun createChannelFlow(systemArea: SystemArea): Flow<Insets> = createChannel(systemArea).receiveAsFlow()
    }

    private val provider = TestInsetsFlowProvider()

    @Test
    fun `createFlow returns Flow with correct type`() {
        val flow = provider.createFlow(SystemArea.StatusBar)

        assertNotNull(flow)
        assertTrue(flow is Flow<Insets>)
    }

    @Test
    fun `getStateFlow returns StateFlow with correct type`() {
        val stateFlow = provider.getStateFlow(SystemArea.NavigationBar)

        assertNotNull(stateFlow)
        assertTrue(stateFlow is StateFlow<Insets>)
    }

    @Test
    fun `createChannel returns Channel with correct type`() {
        val channel = provider.createChannel(SystemArea.IME)

        assertNotNull(channel)
        assertTrue(channel is Channel<Insets>)
    }

    @Test
    fun `createChannelFlow returns Flow with correct type`() {
        val flow = provider.createChannelFlow(SystemArea.Everything)

        assertNotNull(flow)
        assertTrue(flow is Flow<Insets>)
    }

    @Test
    fun `getStateFlow returns same StateFlow for same SystemArea`() {
        val stateFlow1 = provider.getStateFlow(SystemArea.StatusBar)
        val stateFlow2 = provider.getStateFlow(SystemArea.StatusBar)

        assertEquals(stateFlow1, stateFlow2)
    }

    @Test
    fun `getStateFlow returns different StateFlow for different SystemArea`() {
        val stateFlow1 = provider.getStateFlow(SystemArea.StatusBar)
        val stateFlow2 = provider.getStateFlow(SystemArea.NavigationBar)

        assertTrue(stateFlow1 != stateFlow2)
    }

    @Test
    fun `provider methods handle all SystemArea types`() {
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
            assertNotNull(provider.createFlow(systemArea))
            assertNotNull(provider.getStateFlow(systemArea))
            assertNotNull(provider.createChannel(systemArea))
            assertNotNull(provider.createChannelFlow(systemArea))
        }
    }
}
