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

import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt

/**
 * Sets the status bar to be transparent and adjusts content appearance based on background.
 *
 * @param backgroundColor The background color behind the status bar
 *
 * Usage example:
 * ```kotlin
 * activity.setTransparentStatusBarWithBackground(Color.BLUE)
 * ```
 */
fun ComponentActivity.setTransparentStatusBarWithBackground(
    @ColorInt backgroundColor: Int,
) {
    enableEdgeToEdge()

    // Set appearance based on background color
    setStatusBarAppearanceForBackground(backgroundColor)
}

/**
 * Enables edge-to-edge display with automatic status bar styling based on a specific background color.
 *
 * @param backgroundColor The dominant background color of the app
 *
 * Usage example:
 * ```kotlin
 * activity.enableEdgeToEdgeWithAutoStatusBar(Color.BLACK)
 * ```
 */
fun ComponentActivity.enableEdgeToEdgeWithAutoStatusBar(
    @ColorInt backgroundColor: Int,
) {
    // Enable edge-to-edge
    enableEdgeToEdge()

    // Set status bar appearance based on background
    setStatusBarAppearanceForBackground(backgroundColor)
}
