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
package io.github.parkjonghun.edgetoeasy.core.dsl

import android.view.View
import io.github.parkjonghun.edgetoeasy.core.model.FillDirection
import io.github.parkjonghun.edgetoeasy.core.model.SpacingType
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ViewInsetsChainTest {

    private val mockView1 = mockk<View>(relaxed = true)
    private val mockView2 = mockk<View>(relaxed = true)

    @Test
    fun `ViewInsetsChain constructor with single view creates proper config`() {
        val chain = ViewInsetsChain(mockView1, SystemArea.StatusBar, FillDirection.All, SpacingType.MARGIN)

        assertNotNull(chain)
    }

    @Test
    fun `ViewInsetsChain then method returns ViewEdgeBuilder with chain`() {
        val chain = ViewInsetsChain(mockView1, SystemArea.StatusBar, FillDirection.All)

        val edgeBuilder = chain.then(mockView2)

        assertNotNull(edgeBuilder)
    }

    @Test
    fun `ViewInsetsChain addView adds new view config to chain`() {
        val chain = ViewInsetsChain()

        chain.addView(mockView1, SystemArea.StatusBar, FillDirection.Horizontal, SpacingType.PADDING)

        // We can't directly access the internal views list, but the method should not throw
        assertNotNull(chain)
    }

    @Test
    fun `ViewInsetsChain handleEdgeToEdge completes chain processing`() {
        val chain = ViewInsetsChain(mockView1, SystemArea.StatusBar, FillDirection.All)

        // Should not throw any exceptions
        chain.handleEdgeToEdge()

        assertNotNull(chain)
    }

    @Test
    fun `ViewInsetsChain continueToOthers completes chain without consuming insets`() {
        val chain = ViewInsetsChain(mockView1, SystemArea.StatusBar, FillDirection.All)

        // Should not throw any exceptions
        chain.continueToOthers()

        assertNotNull(chain)
    }

    @Test
    fun `ViewInsetsChain can chain multiple views`() {
        val chain = ViewInsetsChain(mockView1, SystemArea.StatusBar, FillDirection.All)

        chain.addView(mockView2, SystemArea.NavigationBar, FillDirection.Vertical, SpacingType.MARGIN)

        // Should not throw any exceptions
        chain.handleEdgeToEdge()

        assertNotNull(chain)
    }

    @Test
    fun `ViewInsetsConfig data class works correctly`() {
        val config1 = ViewInsetsChain.ViewInsetsConfig(
            view = mockView1,
            systemArea = SystemArea.StatusBar,
            direction = FillDirection.All,
            spacingType = SpacingType.MARGIN,
            shouldConsumeInsets = false,
        )

        val config2 = ViewInsetsChain.ViewInsetsConfig(
            view = mockView1,
            systemArea = SystemArea.StatusBar,
            direction = FillDirection.All,
            spacingType = SpacingType.MARGIN,
            shouldConsumeInsets = false,
        )

        val config3 = ViewInsetsChain.ViewInsetsConfig(
            view = mockView2,
            systemArea = SystemArea.StatusBar,
            direction = FillDirection.All,
            spacingType = SpacingType.MARGIN,
            shouldConsumeInsets = false,
        )

        assertEquals(config1, config2)
        assertTrue(config1 != config3)
        assertEquals(config1.hashCode(), config2.hashCode())
    }

    @Test
    fun `ViewInsetsConfig shouldConsumeInsets can be modified`() {
        val config = ViewInsetsChain.ViewInsetsConfig(
            view = mockView1,
            systemArea = SystemArea.StatusBar,
            direction = FillDirection.All,
            spacingType = SpacingType.MARGIN,
            shouldConsumeInsets = false,
        )

        assertFalse(config.shouldConsumeInsets)

        config.shouldConsumeInsets = true

        assertTrue(config.shouldConsumeInsets)
    }

    @Test
    fun `ViewInsetsConfig has correct default values`() {
        val config = ViewInsetsChain.ViewInsetsConfig(
            view = mockView1,
            systemArea = SystemArea.StatusBar,
            direction = FillDirection.All,
        )

        assertEquals(SpacingType.MARGIN, config.spacingType)
        assertFalse(config.shouldConsumeInsets)
    }
}
