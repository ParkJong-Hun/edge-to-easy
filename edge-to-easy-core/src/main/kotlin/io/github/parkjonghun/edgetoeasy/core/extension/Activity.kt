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
package io.github.parkjonghun.edgetoeasy.core.extension

import android.app.Activity
import android.os.Build
import android.view.WindowInsetsController
import androidx.annotation.ColorInt
import androidx.core.view.WindowCompat
import io.github.parkjonghun.edgetoeasy.core.util.ColorUtils

/**
 * Forces the status bar text and icons to use light appearance (dark text/icons on light background).
 * This makes the status bar content appear in black/dark colors.
 *
 * Usage example:
 * ```kotlin
 * activity.forceStatusBarItemsLight()
 * ```
 */
fun Activity.forceStatusBarItemsLight() {
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
}

/**
 * Forces the status bar text and icons to use dark appearance (light text/icons on dark background).
 * This makes the status bar content appear in white/light colors.
 *
 * Usage example:
 * ```kotlin
 * activity.forceStatusBarItemsDark()
 * ```
 */
fun Activity.forceStatusBarItemsDark() {
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
}

/**
 * Forces the navigation bar text and icons to use light appearance (dark icons on light background).
 * This makes the navigation bar content appear in black/dark colors.
 *
 * Usage example:
 * ```kotlin
 * activity.forceNavigationBarItemsLight()
 * ```
 */
fun Activity.forceNavigationBarItemsLight() {
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightNavigationBars =
        true
}

/**
 * Forces the navigation bar text and icons to use dark appearance (light icons on dark background).
 * This makes the navigation bar content appear in white/light colors.
 *
 * Usage example:
 * ```kotlin
 * activity.forceNavigationBarItemsDark()
 * ```
 */
fun Activity.forceNavigationBarItemsDark() {
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightNavigationBars =
        false
}

/**
 * Sets the status bar appearance (light/dark content) based on the background color.
 *
 * @param backgroundColor The background color to analyze
 */
fun Activity.setStatusBarAppearanceForBackground(
    @ColorInt backgroundColor: Int,
) {
    val shouldUseLightContent = ColorUtils.shouldUseLightStatusBar(backgroundColor)
    setStatusBarAppearance(shouldUseLightContent)
}

/**
 * Sets the status bar appearance (light/dark content).
 *
 * @param lightContent true for light content (white icons/text), false for dark content (black icons/text)
 */
private fun Activity.setStatusBarAppearance(lightContent: Boolean) {
    val window = this.window
    val decorView = window.decorView

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // Use WindowInsetsController for API 30+
        val insetsController = window.insetsController
        if (lightContent) {
            insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            )
        } else {
            insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            )
        }
    } else {
        // Use WindowCompat for older versions
        WindowCompat.getInsetsController(window, decorView).isAppearanceLightStatusBars = !lightContent
    }
}

/**
 * Automatically sets the status bar appearance based on a specific background color.
 *
 * @param backgroundColor The background color to analyze for status bar appearance
 *
 * Usage example:
 * ```kotlin
 * activity.setAutoStatusBarAppearance(Color.WHITE)
 * ```
 */
fun Activity.setAutoStatusBarAppearance(
    @ColorInt backgroundColor: Int,
) {
    setStatusBarAppearanceForBackground(backgroundColor)
}
