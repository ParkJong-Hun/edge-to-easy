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

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.parkjonghun.edgetoeasy.dsl.ViewFillBuilder
import io.github.parkjonghun.edgetoeasy.model.SystemArea

/**
 * Creates space between the view and the specified system area.
 * This extension function provides a DSL for edge-to-edge configuration.
 *
 * @param systemArea The system area to move away from (StatusBar, NavigationBar, etc.)
 * @return ViewFillBuilder for method chaining to specify fill direction
 *
 * Usage examples:
 * ```kotlin
 * // Basic usage - move away from status bar and fill all directions
 * view.awayFrom(SystemArea.StatusBar).fillSpace().handleEdgeToEdge()
 *
 * // Move away from navigation bar and fill only vertically
 * view.awayFrom(SystemArea.NavigationBar).fillSpace(FillDirection.Vertical).handleEdgeToEdge()
 *
 * // Move away from everything (system bars + cutout) and fill horizontally
 * view.awayFrom(SystemArea.Everything).fillSpace(FillDirection.Horizontal).handleEdgeToEdge()
 * ```
 */
fun View.awayFrom(systemArea: SystemArea): ViewFillBuilder = ViewFillBuilder(this, systemArea)

/**
 * Adds a spacer view to a ViewGroup with height determined by the specified SystemArea's insets.
 * The height will be set to the maximum of top and bottom insets from the specified system area.
 * This is useful for creating space in layouts to avoid system UI overlap.
 *
 * @param systemArea The system area to use for determining the spacer height
 * @param addToTop If true, adds the spacer to the beginning (index 0), otherwise to the end (default: false)
 * @return The created spacer view that was added to the ViewGroup
 *
 * Usage examples:
 * ```kotlin
 * // Add spacer at the end (default)
 * linearLayout.addSystemAreaSpacer(SystemArea.NavigationBar)
 *
 * // Add spacer at the beginning
 * linearLayout.addSystemAreaSpacer(SystemArea.StatusBar, addToTop = true)
 *
 * // Add spacer for full bottom area (navigation bar + cutout)
 * constraintLayout.addSystemAreaSpacer(SystemArea.BottomFull)
 * ```
 */
fun ViewGroup.addSystemAreaSpacer(
    systemArea: SystemArea,
    addToTop: Boolean = false,
): View {
    val spacerView = View(context)

    // Add the spacer view to the ViewGroup at the specified position
    if (addToTop) {
        addView(spacerView, 0)
    } else {
        addView(spacerView)
    }

    // Set up window insets listener to adjust height based on system area
    ViewCompat.setOnApplyWindowInsetsListener(spacerView) { view, insets ->
        val systemInsets =
            when (systemArea) {
                SystemArea.SystemBar -> insets.getInsets(WindowInsetsCompat.Type.systemBars())
                SystemArea.StatusBar, SystemArea.Top -> insets.getInsets(WindowInsetsCompat.Type.statusBars())
                SystemArea.NavigationBar, SystemArea.Bottom -> insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                SystemArea.IME -> insets.getInsets(WindowInsetsCompat.Type.ime())
                SystemArea.Cutout -> insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                SystemArea.Everything -> {
                    val systemBar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                    val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
                    Insets.of(
                        maxOf(systemBar.left, cutout.left, ime.left),
                        maxOf(systemBar.top, cutout.top, ime.top),
                        maxOf(systemBar.right, cutout.right, ime.right),
                        maxOf(systemBar.bottom, cutout.bottom, ime.bottom),
                    )
                }
                SystemArea.TopFull -> {
                    val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
                    val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                    Insets.of(
                        maxOf(statusBar.left, cutout.left),
                        maxOf(statusBar.top, cutout.top),
                        maxOf(statusBar.right, cutout.right),
                        statusBar.bottom,
                    )
                }
                SystemArea.BottomFull -> {
                    val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                    val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                    val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
                    Insets.of(
                        maxOf(navBar.left, cutout.left, ime.left),
                        navBar.top,
                        maxOf(navBar.right, cutout.right, ime.right),
                        maxOf(navBar.bottom, cutout.bottom, ime.bottom),
                    )
                }
            }

        // Set the height of the spacer view based on the maximum of top and bottom insets
        val layoutParams = view.layoutParams
        layoutParams.height = maxOf(systemInsets.top, systemInsets.bottom)
        view.layoutParams = layoutParams

        insets
    }

    return spacerView
}
