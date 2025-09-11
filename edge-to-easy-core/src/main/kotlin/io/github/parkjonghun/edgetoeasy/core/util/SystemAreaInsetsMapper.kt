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

import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea

public object SystemAreaInsetsMapper {
    public fun getInsetsForSystemArea(
        insets: WindowInsetsCompat,
        systemArea: SystemArea,
    ): Insets = when (systemArea) {
        SystemArea.SystemBar, SystemArea.Everything -> insets.getInsets(WindowInsetsCompat.Type.systemBars())
        SystemArea.StatusBar, SystemArea.Top -> insets.getInsets(WindowInsetsCompat.Type.statusBars())
        SystemArea.NavigationBar -> insets.getInsets(WindowInsetsCompat.Type.navigationBars())
        SystemArea.Bottom -> {
            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            Insets.of(
                maxOf(navBar.left, ime.left),
                maxOf(navBar.top, ime.top),
                maxOf(navBar.right, ime.right),
                maxOf(navBar.bottom, ime.bottom),
            )
        }

        SystemArea.IME -> insets.getInsets(WindowInsetsCompat.Type.ime())
        SystemArea.Cutout -> insets.getInsets(WindowInsetsCompat.Type.displayCutout())
        SystemArea.TopFull -> {
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            Insets.of(
                maxOf(statusBar.left, cutout.left),
                maxOf(statusBar.top, cutout.top),
                maxOf(statusBar.right, cutout.right),
                maxOf(statusBar.bottom, cutout.bottom),
            )
        }

        SystemArea.BottomFull -> {
            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            Insets.of(
                maxOf(navBar.left, ime.left, cutout.left),
                maxOf(navBar.top, ime.top, cutout.top),
                maxOf(navBar.right, ime.right, cutout.right),
                maxOf(navBar.bottom, ime.bottom, cutout.bottom),
            )
        }
    }
}
