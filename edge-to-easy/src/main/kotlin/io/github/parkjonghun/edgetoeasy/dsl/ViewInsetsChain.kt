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
package io.github.parkjonghun.edgetoeasy.dsl

import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.parkjonghun.edgetoeasy.model.FillDirection
import io.github.parkjonghun.edgetoeasy.model.SpacingType
import io.github.parkjonghun.edgetoeasy.model.SystemArea

/**
 * Chain class for handling multiple views with insets in sequence.
 * This allows for step-by-step application of insets across different views.
 */
class ViewInsetsChain(
    private val views: MutableList<ViewInsetsConfig> = mutableListOf()
) {
    constructor(view: View, systemArea: SystemArea, direction: FillDirection, spacingType: SpacingType = SpacingType.MARGIN) : this() {
        views.add(ViewInsetsConfig(view, systemArea, direction, spacingType))
    }

    /**
     * Adds another view to the chain for insets processing.
     *
     * @param view The next view to handle insets
     * @return ViewEdgeBuilder to continue the DSL chain
     *
     * Usage example:
     * ```kotlin
     * view1.separateFrom(SystemArea.Top).fillSpace()
     *      .then(view2).awayFrom(SystemArea.Bottom).fillSpace()
     *      .then(view3).distanceFrom(SystemArea.Left).fillSpace().handleEdgeToEdge()
     * ```
     */
    fun then(view: View): ViewEdgeBuilder {
        return ViewEdgeBuilder(view, this)
    }

    /**
     * Completes the edge-to-edge handling and consumes the insets.
     * This prevents other views from processing the same insets, indicating that
     * all edge-to-edge adjustments have been handled by the views in this chain.
     *
     * Usage example:
     * ```kotlin
     * // Single view usage
     * view.separateFrom(SystemArea.StatusBar).fillSpace().handleEdgeToEdge()
     *
     * // Multiple views usage
     * view1.separateFrom(SystemArea.Top).fillSpace()
     *      .then(view2).awayFrom(SystemArea.Bottom).fillSpace().handleEdgeToEdge()
     * ```
     */
    fun handleEdgeToEdge() {
        applyInsetsToViews(consumeInsets = true)
    }

    /**
     * Completes the chain but allows insets to continue to other views not in this chain.
     *
     * Usage example:
     * ```kotlin
     * view1.separateFrom(SystemArea.Top).fillSpace()
     *      .then(view2).awayFrom(SystemArea.Bottom).fillSpace().continueToOthers()
     * ```
     */
    fun continueToOthers() {
        applyInsetsToViews(consumeInsets = false)
    }

    internal fun addView(view: View, systemArea: SystemArea, direction: FillDirection, spacingType: SpacingType = SpacingType.MARGIN) {
        views.add(ViewInsetsConfig(view, systemArea, direction, spacingType))
    }

    private fun applyInsetsToViews(consumeInsets: Boolean) {
        if (views.isEmpty()) return

        views.forEach { config ->
            ViewCompat.setOnApplyWindowInsetsListener(config.view) { v, insets ->
                val systemBars = when (config.systemArea) {
                    SystemArea.SystemBar, SystemArea.Everything -> insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    SystemArea.StatusBar, SystemArea.Top -> insets.getInsets(WindowInsetsCompat.Type.statusBars())
                    SystemArea.NavigationBar, SystemArea.Bottom -> insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                    SystemArea.Cutout -> insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                    SystemArea.TopFull -> {
                        val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
                        val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                        Insets.of(
                            maxOf(statusBar.left, cutout.left),
                            maxOf(statusBar.top, cutout.top),
                            maxOf(statusBar.right, cutout.right),
                            statusBar.bottom
                        )
                    }
                    SystemArea.BottomFull -> {
                        val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                        val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                        Insets.of(
                            maxOf(navBar.left, cutout.left),
                            navBar.top,
                            maxOf(navBar.right, cutout.right),
                            maxOf(navBar.bottom, cutout.bottom)
                        )
                    }
                }

                val spacingLeft = if (config.direction.left) systemBars.left else 0
                val spacingTop = if (config.direction.top) systemBars.top else 0
                val spacingRight = if (config.direction.right) systemBars.right else 0
                val spacingBottom = if (config.direction.bottom) systemBars.bottom else 0

                when (config.spacingType) {
                    SpacingType.PADDING -> {
                        v.setPadding(spacingLeft, spacingTop, spacingRight, spacingBottom)
                    }
                    SpacingType.MARGIN -> {
                        val layoutParams = v.layoutParams
                        if (layoutParams is ViewGroup.MarginLayoutParams) {
                            layoutParams.leftMargin = spacingLeft
                            layoutParams.topMargin = spacingTop
                            layoutParams.rightMargin = spacingRight
                            layoutParams.bottomMargin = spacingBottom
                            v.layoutParams = layoutParams
                        }
                    }
                }

                if (consumeInsets && config == views.last()) {
                    WindowInsetsCompat.CONSUMED
                } else {
                    insets
                }
            }
        }
    }

    data class ViewInsetsConfig(
        val view: View,
        val systemArea: SystemArea,
        val direction: FillDirection,
        val spacingType: SpacingType = SpacingType.MARGIN
    )
}
