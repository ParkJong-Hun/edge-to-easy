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
import io.github.parkjonghun.edgetoeasy.core.model.SpacingType
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea

/**
 * Builder class for creating edge-to-edge configurations starting from a system area.
 * This class is part of the DSL chain and should not be used directly.
 *
 * @param view The view to apply edge-to-edge configuration to
 * @param chain Optional existing chain to continue
 */
class ViewEdgeBuilder(
    private val view: View,
    private val chain: ViewInsetsChain? = null,
) {
    /**
     * Specifies which system area to separate from.
     *
     * @param systemArea The system area to create distance from
     * @return ViewFillBuilder for method chaining
     */
    public fun from(systemArea: SystemArea): ViewFillBuilder = ViewFillBuilder(view, systemArea, chain)
}

/**
 * Builder class for filling space after specifying which system area to separate from.
 * This class provides methods to fill the space with padding based on system insets.
 *
 * @param view The view to apply padding to
 * @param systemArea The system area that was specified to separate from
 * @param chain Optional existing chain to continue
 */
class ViewFillBuilder(
    private val view: View,
    private val systemArea: SystemArea,
    private val chain: ViewInsetsChain? = null,
) {
    /**
     * Fills the space by applying spacing to the view based on system insets.
     * By default, uses margin spacing (outside the view).
     *
     * @param direction The direction(s) to fill. Defaults to FillDirection.All
     * @param useMargin Whether to use margin (true, default) or padding (false)
     * @return ViewInsetsChain to continue applying insets to other views or finish
     *
     * Usage examples:
     * ```kotlin
     * // Default margin spacing
     * view.awayFrom(SystemArea.StatusBar).fillSpace().handleEdgeToEdge()
     *
     * // Explicit padding spacing
     * view.awayFrom(SystemArea.NavigationBar).fillSpace(useMargin = false).handleEdgeToEdge()
     *
     * // Specific direction with padding
     * view.awayFrom(SystemArea.Top).fillSpace(FillDirection.Vertical, useMargin = false).handleEdgeToEdge()
     * ```
     */
    public fun fillSpace(
        direction: FillDirection = FillDirection.All,
        useMargin: Boolean = true,
    ): ViewInsetsChain {
        val spacingType = if (useMargin) SpacingType.MARGIN else SpacingType.PADDING
        return if (chain != null) {
            chain.addView(view, systemArea, direction, spacingType)
            chain
        } else {
            ViewInsetsChain(view, systemArea, direction, spacingType)
        }
    }

    /**
     * Fills the space by applying padding to the view based on system insets.
     *
     * @param direction The direction(s) to fill. Defaults to FillDirection.All
     * @return ViewInsetsChain to continue applying insets to other views or finish
     *
     * Usage examples:
     * ```kotlin
     * // Padding in all directions
     * view.awayFrom(SystemArea.StatusBar).fillWithPadding().handleEdgeToEdge()
     *
     * // Padding only vertically
     * view.awayFrom(SystemArea.NavigationBar).fillWithPadding(FillDirection.Vertical).handleEdgeToEdge()
     * ```
     */
    public fun fillWithPadding(direction: FillDirection = FillDirection.All): ViewInsetsChain = if (chain != null) {
        chain.addView(view, systemArea, direction, SpacingType.PADDING)
        chain
    } else {
        ViewInsetsChain(view, systemArea, direction, SpacingType.PADDING)
    }

    /**
     * Fills the space by applying margin to the view based on system insets.
     *
     * @param direction The direction(s) to fill. Defaults to FillDirection.All
     * @return ViewInsetsChain to continue applying insets to other views or finish
     *
     * Usage examples:
     * ```kotlin
     * // Margin in all directions
     * view.awayFrom(SystemArea.StatusBar).fillWithMargin().handleEdgeToEdge()
     *
     * // Margin only horizontally
     * view.awayFrom(SystemArea.NavigationBar).fillWithMargin(FillDirection.Horizontal).handleEdgeToEdge()
     * ```
     */
    public fun fillWithMargin(direction: FillDirection = FillDirection.All): ViewInsetsChain = if (chain != null) {
        chain.addView(view, systemArea, direction, SpacingType.MARGIN)
        chain
    } else {
        ViewInsetsChain(view, systemArea, direction, SpacingType.MARGIN)
    }
}
