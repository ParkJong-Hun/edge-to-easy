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
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class EdgeToEdgeBuildersTest {

    private val mockView = mockk<View>(relaxed = true)

    @Test
    fun `ViewEdgeBuilder awayFrom creates ViewFillBuilder`() {
        val edgeBuilder = ViewEdgeBuilder(mockView)

        val fillBuilder = edgeBuilder.awayFrom(SystemArea.StatusBar)

        assertNotNull(fillBuilder)
    }

    @Test
    fun `ViewEdgeBuilder awayFrom with existing chain passes chain to ViewFillBuilder`() {
        val existingChain = mockk<ViewInsetsChain>()
        val edgeBuilder = ViewEdgeBuilder(mockView, existingChain)

        val fillBuilder = edgeBuilder.awayFrom(SystemArea.NavigationBar)

        assertNotNull(fillBuilder)
    }

    @Test
    fun `ViewFillBuilder fillSpace creates ViewInsetsChain with default parameters`() {
        val fillBuilder = ViewFillBuilder(mockView, SystemArea.StatusBar)

        val chain = fillBuilder.fillSpace()

        assertNotNull(chain)
    }

    @Test
    fun `ViewFillBuilder fillSpace with custom direction and margin creates correct chain`() {
        val fillBuilder = ViewFillBuilder(mockView, SystemArea.NavigationBar)

        val chain = fillBuilder.fillSpace(FillDirection.Horizontal, useMargin = true)

        assertNotNull(chain)
    }

    @Test
    fun `ViewFillBuilder fillSpace with custom direction and padding creates correct chain`() {
        val fillBuilder = ViewFillBuilder(mockView, SystemArea.IME)

        val chain = fillBuilder.fillSpace(FillDirection.Vertical, useMargin = false)

        assertNotNull(chain)
    }

    @Test
    fun `ViewFillBuilder fillWithPadding creates ViewInsetsChain with padding`() {
        val fillBuilder = ViewFillBuilder(mockView, SystemArea.StatusBar)

        val chain = fillBuilder.fillWithPadding()

        assertNotNull(chain)
    }

    @Test
    fun `ViewFillBuilder fillWithPadding with custom direction creates correct chain`() {
        val fillBuilder = ViewFillBuilder(mockView, SystemArea.NavigationBar)

        val chain = fillBuilder.fillWithPadding(FillDirection.Horizontal)

        assertNotNull(chain)
    }

    @Test
    fun `ViewFillBuilder fillWithMargin creates ViewInsetsChain with margin`() {
        val fillBuilder = ViewFillBuilder(mockView, SystemArea.StatusBar)

        val chain = fillBuilder.fillWithMargin()

        assertNotNull(chain)
    }

    @Test
    fun `ViewFillBuilder fillWithMargin with custom direction creates correct chain`() {
        val fillBuilder = ViewFillBuilder(mockView, SystemArea.NavigationBar)

        val chain = fillBuilder.fillWithMargin(FillDirection.Vertical)

        assertNotNull(chain)
    }

    @Test
    fun `ViewFillBuilder with existing chain adds view to chain`() {
        val existingChain = ViewInsetsChain(mockk<View>(relaxed = true), SystemArea.StatusBar, FillDirection.All)
        val fillBuilder = ViewFillBuilder(mockView, SystemArea.NavigationBar, existingChain)

        val returnedChain = fillBuilder.fillSpace()

        assertEquals(existingChain, returnedChain)
    }

    @Test
    fun `ViewFillBuilder fillWithPadding with existing chain adds view to chain`() {
        val existingChain = ViewInsetsChain(mockk<View>(relaxed = true), SystemArea.StatusBar, FillDirection.All)
        val fillBuilder = ViewFillBuilder(mockView, SystemArea.NavigationBar, existingChain)

        val returnedChain = fillBuilder.fillWithPadding()

        assertEquals(existingChain, returnedChain)
    }

    @Test
    fun `ViewFillBuilder fillWithMargin with existing chain adds view to chain`() {
        val existingChain = ViewInsetsChain(mockk<View>(relaxed = true), SystemArea.StatusBar, FillDirection.All)
        val fillBuilder = ViewFillBuilder(mockView, SystemArea.NavigationBar, existingChain)

        val returnedChain = fillBuilder.fillWithMargin()

        assertEquals(existingChain, returnedChain)
    }
}
