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
package io.github.parkjonghun.edgetoeasy.extension

import android.app.Activity
import androidx.core.view.WindowCompat

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
