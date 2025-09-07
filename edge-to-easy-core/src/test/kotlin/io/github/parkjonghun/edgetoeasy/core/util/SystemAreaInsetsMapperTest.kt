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
package io.github.parkjonghun.edgetoeasy.core.util

import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class SystemAreaInsetsMapperTest {

    private val mockWindowInsets = mockk<WindowInsetsCompat>()

    @Test
    fun `getInsetsForSystemArea returns status bar insets for StatusBar`() {
        val expectedInsets = Insets.of(0, 24, 0, 0)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.statusBars()) } returns expectedInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.StatusBar)

        assertEquals(expectedInsets, result)
    }

    @Test
    fun `getInsetsForSystemArea returns status bar insets for Top`() {
        val expectedInsets = Insets.of(0, 24, 0, 0)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.statusBars()) } returns expectedInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.Top)

        assertEquals(expectedInsets, result)
    }

    @Test
    fun `getInsetsForSystemArea returns navigation bar insets for NavigationBar`() {
        val expectedInsets = Insets.of(0, 0, 0, 48)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()) } returns expectedInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.NavigationBar)

        assertEquals(expectedInsets, result)
    }

    @Test
    fun `getInsetsForSystemArea returns system bar insets for SystemBar`() {
        val expectedInsets = Insets.of(0, 24, 0, 48)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.systemBars()) } returns expectedInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.SystemBar)

        assertEquals(expectedInsets, result)
    }

    @Test
    fun `getInsetsForSystemArea returns system bar insets for Everything`() {
        val expectedInsets = Insets.of(0, 24, 0, 48)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.systemBars()) } returns expectedInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.Everything)

        assertEquals(expectedInsets, result)
    }

    @Test
    fun `getInsetsForSystemArea returns IME insets for IME`() {
        val expectedInsets = Insets.of(0, 0, 0, 300)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.ime()) } returns expectedInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.IME)

        assertEquals(expectedInsets, result)
    }

    @Test
    fun `getInsetsForSystemArea returns cutout insets for Cutout`() {
        val expectedInsets = Insets.of(0, 32, 0, 0)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.displayCutout()) } returns expectedInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.Cutout)

        assertEquals(expectedInsets, result)
    }

    @Test
    fun `getInsetsForSystemArea returns max of nav and IME for Bottom`() {
        val navInsets = Insets.of(0, 0, 0, 48)
        val imeInsets = Insets.of(0, 0, 0, 300)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()) } returns navInsets
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.ime()) } returns imeInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.Bottom)

        assertEquals(Insets.of(0, 0, 0, 300), result)
    }

    @Test
    fun `getInsetsForSystemArea returns nav when IME is not visible for Bottom`() {
        val navInsets = Insets.of(0, 0, 0, 48)
        val imeInsets = Insets.of(0, 0, 0, 0)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()) } returns navInsets
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.ime()) } returns imeInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.Bottom)

        assertEquals(Insets.of(0, 0, 0, 48), result)
    }

    @Test
    fun `getInsetsForSystemArea returns max of status and cutout for TopFull`() {
        val statusInsets = Insets.of(0, 24, 0, 0)
        val cutoutInsets = Insets.of(32, 40, 32, 0)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.statusBars()) } returns statusInsets
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.displayCutout()) } returns cutoutInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.TopFull)

        assertEquals(Insets.of(32, 40, 32, 0), result)
    }

    @Test
    fun `getInsetsForSystemArea returns max of nav, IME and cutout for BottomFull`() {
        val navInsets = Insets.of(0, 0, 0, 48)
        val imeInsets = Insets.of(0, 0, 0, 300)
        val cutoutInsets = Insets.of(32, 0, 32, 0)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()) } returns navInsets
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.ime()) } returns imeInsets
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.displayCutout()) } returns cutoutInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.BottomFull)

        assertEquals(Insets.of(32, 0, 32, 300), result)
    }

    @Test
    fun `getInsetsForSystemArea handles zero insets correctly`() {
        val zeroInsets = Insets.of(0, 0, 0, 0)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.statusBars()) } returns zeroInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.StatusBar)

        assertEquals(zeroInsets, result)
    }

    @Test
    fun `getInsetsForSystemArea handles negative insets correctly`() {
        val negativeInsets = Insets.of(-10, -20, -30, -40)
        every { mockWindowInsets.getInsets(WindowInsetsCompat.Type.statusBars()) } returns negativeInsets

        val result = SystemAreaInsetsMapper.getInsetsForSystemArea(mockWindowInsets, SystemArea.StatusBar)

        assertEquals(negativeInsets, result)
    }
}
