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

class FillVerticalDirectionTest {

    @Test
    fun `FillVerticalDirection default constructor creates All direction`() {
        val fillDirection = FillVerticalDirection()

        assertTrue(fillDirection.top)
        assertTrue(fillDirection.bottom)
    }

    @Test
    fun `FillVerticalDirection None has all directions disabled`() {
        val fillDirection = FillVerticalDirection.None

        assertFalse(fillDirection.top)
        assertFalse(fillDirection.bottom)
    }

    @Test
    fun `FillVerticalDirection All has all directions enabled`() {
        val fillDirection = FillVerticalDirection.All

        assertTrue(fillDirection.top)
        assertTrue(fillDirection.bottom)
    }

    @Test
    fun `FillVerticalDirection Top enables only top`() {
        val fillDirection = FillVerticalDirection.Top

        assertTrue(fillDirection.top)
        assertFalse(fillDirection.bottom)
    }

    @Test
    fun `FillVerticalDirection Bottom enables only bottom`() {
        val fillDirection = FillVerticalDirection.Bottom

        assertFalse(fillDirection.top)
        assertTrue(fillDirection.bottom)
    }

    @Test
    fun `FillVerticalDirection custom constructor works correctly`() {
        val fillDirection = FillVerticalDirection(
            top = true,
            bottom = false,
        )

        assertTrue(fillDirection.top)
        assertFalse(fillDirection.bottom)
    }

    @Test
    fun `FillVerticalDirection data class equality works correctly`() {
        val fillDirection1 = FillVerticalDirection(top = true, bottom = false)
        val fillDirection2 = FillVerticalDirection(top = true, bottom = false)
        val fillDirection3 = FillVerticalDirection(top = false, bottom = false)

        assertEquals(fillDirection1, fillDirection2)
        assertEquals(fillDirection1.hashCode(), fillDirection2.hashCode())
        assertTrue(fillDirection1 != fillDirection3)
    }
}
