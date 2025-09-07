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
package io.github.parkjonghun.edgetoeasy.core.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FillDirectionTest {

    @Test
    fun `FillDirection default constructor creates All direction`() {
        val fillDirection = FillDirection()

        assertTrue(fillDirection.top)
        assertTrue(fillDirection.left)
        assertTrue(fillDirection.right)
        assertTrue(fillDirection.bottom)
    }

    @Test
    fun `FillDirection None has all directions disabled`() {
        val fillDirection = FillDirection.None

        assertFalse(fillDirection.top)
        assertFalse(fillDirection.left)
        assertFalse(fillDirection.right)
        assertFalse(fillDirection.bottom)
    }

    @Test
    fun `FillDirection All has all directions enabled`() {
        val fillDirection = FillDirection.All

        assertTrue(fillDirection.top)
        assertTrue(fillDirection.left)
        assertTrue(fillDirection.right)
        assertTrue(fillDirection.bottom)
    }

    @Test
    fun `FillDirection Horizontal enables only left and right`() {
        val fillDirection = FillDirection.Horizontal

        assertFalse(fillDirection.top)
        assertTrue(fillDirection.left)
        assertTrue(fillDirection.right)
        assertFalse(fillDirection.bottom)
    }

    @Test
    fun `FillDirection Vertical enables only top and bottom`() {
        val fillDirection = FillDirection.Vertical

        assertTrue(fillDirection.top)
        assertFalse(fillDirection.left)
        assertFalse(fillDirection.right)
        assertTrue(fillDirection.bottom)
    }

    @Test
    fun `FillDirection Left enables only left`() {
        val fillDirection = FillDirection.Left

        assertFalse(fillDirection.top)
        assertTrue(fillDirection.left)
        assertFalse(fillDirection.right)
        assertFalse(fillDirection.bottom)
    }

    @Test
    fun `FillDirection Top enables only top`() {
        val fillDirection = FillDirection.Top

        assertTrue(fillDirection.top)
        assertFalse(fillDirection.left)
        assertFalse(fillDirection.right)
        assertFalse(fillDirection.bottom)
    }

    @Test
    fun `FillDirection Right enables only right`() {
        val fillDirection = FillDirection.Right

        assertFalse(fillDirection.top)
        assertFalse(fillDirection.left)
        assertTrue(fillDirection.right)
        assertFalse(fillDirection.bottom)
    }

    @Test
    fun `FillDirection Bottom enables only bottom`() {
        val fillDirection = FillDirection.Bottom

        assertFalse(fillDirection.top)
        assertFalse(fillDirection.left)
        assertFalse(fillDirection.right)
        assertTrue(fillDirection.bottom)
    }

    @Test
    fun `FillDirection TopLeft enables top and left`() {
        val fillDirection = FillDirection.TopLeft

        assertTrue(fillDirection.top)
        assertTrue(fillDirection.left)
        assertFalse(fillDirection.right)
        assertFalse(fillDirection.bottom)
    }

    @Test
    fun `FillDirection TopRight enables top and right`() {
        val fillDirection = FillDirection.TopRight

        assertTrue(fillDirection.top)
        assertFalse(fillDirection.left)
        assertTrue(fillDirection.right)
        assertFalse(fillDirection.bottom)
    }

    @Test
    fun `FillDirection BottomLeft enables bottom and left`() {
        val fillDirection = FillDirection.BottomLeft

        assertFalse(fillDirection.top)
        assertTrue(fillDirection.left)
        assertFalse(fillDirection.right)
        assertTrue(fillDirection.bottom)
    }

    @Test
    fun `FillDirection BottomRight enables bottom and right`() {
        val fillDirection = FillDirection.BottomRight

        assertFalse(fillDirection.top)
        assertFalse(fillDirection.left)
        assertTrue(fillDirection.right)
        assertTrue(fillDirection.bottom)
    }

    @Test
    fun `FillDirection custom constructor works correctly`() {
        val fillDirection = FillDirection(
            top = true,
            left = false,
            right = true,
            bottom = false,
        )

        assertTrue(fillDirection.top)
        assertFalse(fillDirection.left)
        assertTrue(fillDirection.right)
        assertFalse(fillDirection.bottom)
    }

    @Test
    fun `FillDirection data class equality works correctly`() {
        val fillDirection1 = FillDirection(top = true, left = false, right = true, bottom = false)
        val fillDirection2 = FillDirection(top = true, left = false, right = true, bottom = false)
        val fillDirection3 = FillDirection(top = false, left = false, right = true, bottom = false)

        assertEquals(fillDirection1, fillDirection2)
        assertEquals(fillDirection1.hashCode(), fillDirection2.hashCode())
        assertTrue(fillDirection1 != fillDirection3)
    }
}
