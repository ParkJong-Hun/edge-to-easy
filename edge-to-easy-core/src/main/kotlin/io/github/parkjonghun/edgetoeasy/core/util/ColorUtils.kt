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

import android.graphics.Color
import androidx.annotation.ColorInt
import kotlin.math.pow

internal object ColorUtils {
    /**
     * Calculates the luminance of a color according to WCAG guidelines.
     *
     * @param color The color to calculate luminance for
     * @return The luminance value between 0.0 (black) and 1.0 (white)
     */
    fun calculateLuminance(
        @ColorInt color: Int,
    ): Double {
        val red = Color.red(color) / 255.0
        val green = Color.green(color) / 255.0
        val blue = Color.blue(color) / 255.0

        val sRed = if (red <= 0.03928) red / 12.92 else ((red + 0.055) / 1.055).pow(2.4)
        val sGreen = if (green <= 0.03928) green / 12.92 else ((green + 0.055) / 1.055).pow(2.4)
        val sBlue = if (blue <= 0.03928) blue / 12.92 else ((blue + 0.055) / 1.055).pow(2.4)

        return 0.2126 * sRed + 0.7152 * sGreen + 0.0722 * sBlue
    }

    /**
     * Determines if a background color is light or dark.
     *
     * @param backgroundColor The background color to analyze
     * @return true if the background is light (should use dark text), false if dark (should use light text)
     */
    fun isLightColor(
        @ColorInt backgroundColor: Int,
    ): Boolean = calculateLuminance(backgroundColor) > 0.5

    /**
     * Determines if light status bar content should be used based on the background color.
     *
     * @param backgroundColor The background color to analyze
     * @return true if light status bar content should be used (for dark backgrounds)
     */
    fun shouldUseLightStatusBar(
        @ColorInt backgroundColor: Int,
    ): Boolean = !isLightColor(backgroundColor)

    /**
     * Gets the recommended status bar content color based on background luminance.
     *
     * @param backgroundColor The background color to analyze
     * @return Color.WHITE for dark backgrounds, Color.BLACK for light backgrounds
     */
    @ColorInt
    fun getRecommendedStatusBarColor(
        @ColorInt backgroundColor: Int,
    ): Int =
        if (shouldUseLightStatusBar(backgroundColor)) {
            Color.WHITE
        } else {
            Color.BLACK
        }
}
