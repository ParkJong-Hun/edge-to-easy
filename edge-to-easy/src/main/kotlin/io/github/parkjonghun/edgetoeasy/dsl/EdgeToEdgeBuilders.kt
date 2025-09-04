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
import io.github.parkjonghun.edgetoeasy.model.FillDirection
import io.github.parkjonghun.edgetoeasy.model.SpacingType
import io.github.parkjonghun.edgetoeasy.model.SystemArea

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
    fun from(systemArea: SystemArea): ViewFillBuilder = ViewFillBuilder(view, systemArea, chain)
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
     * Fills the space by applying padding to the view based on system insets.
     * This method sets up a WindowInsetsListener that automatically applies padding
     * to avoid overlapping with the specified system area.
     *
     * @param direction The direction(s) to fill. Defaults to FillDirection.All
     * @return ViewInsetsChain to continue applying insets to other views or finish
     *
     * Usage examples:
     * ```kotlin
     * // Single view usage - this view handles insets exclusively
     * view.separateFrom(SystemArea.StatusBar).fillWithPadding().handleEdgeToEdge()
     *
     * // Multiple views usage - chain to other views
     * view1.separateFrom(SystemArea.Top).fillWithPadding()
     *      .then(view2).separateFrom(SystemArea.Bottom).fillWithPadding().handleEdgeToEdge()
     * ```
     */
    fun fillWithPadding(direction: FillDirection = FillDirection.All): ViewInsetsChain =
        if (chain != null) {
            chain.addView(view, systemArea, direction, SpacingType.PADDING)
            chain
        } else {
            ViewInsetsChain(view, systemArea, direction, SpacingType.PADDING)
        }

    /**
     * Fills the space by applying margin to the view based on system insets.
     * This method sets up a WindowInsetsListener that automatically applies margin
     * to avoid overlapping with the specified system area.
     * This is the default spacing method.
     *
     * @param direction The direction(s) to fill. Defaults to FillDirection.All
     * @return ViewInsetsChain to continue applying insets to other views or finish
     *
     * Usage examples:
     * ```kotlin
     * // Single view usage - this view handles insets exclusively
     * view.separateFrom(SystemArea.StatusBar).fillSpace().handleEdgeToEdge()
     *
     * // Multiple views usage - chain to other views
     * view1.separateFrom(SystemArea.Top).fillSpace()
     *      .then(view2).separateFrom(SystemArea.Bottom).fillSpace().handleEdgeToEdge()
     * ```
     */
    fun fillSpace(direction: FillDirection = FillDirection.All): ViewInsetsChain =
        if (chain != null) {
            chain.addView(view, systemArea, direction, SpacingType.MARGIN)
            chain
        } else {
            ViewInsetsChain(view, systemArea, direction, SpacingType.MARGIN)
        }

    /**
     * Convenience method that fills space in all directions using margin.
     * Equivalent to calling fillSpace(FillDirection.All).
     */
    fun fillSpace() = fillSpace(FillDirection.All)

    /**
     * Fills the space by applying margin to the view based on system insets.
     * This is an alias for fillSpace() for explicit clarity.
     *
     * @param direction The direction(s) to fill. Defaults to FillDirection.All
     * @return ViewInsetsChain to continue applying insets to other views or finish
     */
    fun fillWithMargin(direction: FillDirection = FillDirection.All): ViewInsetsChain = fillSpace(direction)
}
