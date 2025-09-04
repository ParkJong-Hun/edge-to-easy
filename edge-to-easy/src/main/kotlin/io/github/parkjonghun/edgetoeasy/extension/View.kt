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

import android.view.View
import io.github.parkjonghun.edgetoeasy.dsl.ViewFillBuilder
import io.github.parkjonghun.edgetoeasy.model.SystemArea

/**
 * Creates space between the view and the specified system area by applying padding.
 * This extension function provides a natural language DSL for edge-to-edge configuration.
 *
 * @param systemArea The system area to separate from (StatusBar, NavigationBar, etc.)
 * @return ViewFillBuilder for method chaining to specify fill direction
 *
 * Usage examples:
 * ```kotlin
 * // Basic usage - separate from status bar and fill all directions
 * view.separateFrom(SystemArea.StatusBar).fillSpace()
 *
 * // Separate from navigation bar and fill only vertically
 * view.separateFrom(SystemArea.NavigationBar).fillSpace(FillDirection.Vertical)
 *
 * // Separate from everything (system bars + cutout) and fill horizontally
 * view.separateFrom(SystemArea.Everything).fillSpace(FillDirection.Horizontal)
 * ```
 */
fun View.separateFrom(systemArea: SystemArea): ViewFillBuilder = ViewFillBuilder(this, systemArea)

/**
 * Creates distance between the view and the specified system area by applying padding.
 * This extension function is an alias for separateFrom() providing alternative wording.
 *
 * @param systemArea The system area to move away from
 * @return ViewFillBuilder for method chaining to specify fill direction
 *
 * Usage examples:
 * ```kotlin
 * // Move away from top area (status bar)
 * view.awayFrom(SystemArea.Top).fillSpace()
 *
 * // Move away from bottom area (navigation bar) in specific direction
 * view.awayFrom(SystemArea.Bottom).fillSpace(FillDirection.Left)
 * ```
 */
fun View.awayFrom(systemArea: SystemArea): ViewFillBuilder = ViewFillBuilder(this, systemArea)

/**
 * Creates distance between the view and the specified system area by applying padding.
 * This extension function is an alias for separateFrom() providing alternative wording.
 *
 * @param systemArea The system area to create distance from
 * @return ViewFillBuilder for method chaining to specify fill direction
 *
 * Usage examples:
 * ```kotlin
 * // Create distance from cutout area
 * view.distanceFrom(SystemArea.Cutout).fillSpace()
 *
 * // Create distance from system bar in multiple directions
 * view.distanceFrom(SystemArea.SystemBar).fillSpace(FillDirection.TopLeft)
 * ```
 */
fun View.distanceFrom(systemArea: SystemArea): ViewFillBuilder = ViewFillBuilder(this, systemArea)
