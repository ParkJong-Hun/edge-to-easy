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

import android.view.View
import io.github.parkjonghun.edgetoeasy.core.dsl.ViewFillBuilder
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea

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
