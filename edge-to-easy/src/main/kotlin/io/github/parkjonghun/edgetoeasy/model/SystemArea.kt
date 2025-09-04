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
package io.github.parkjonghun.edgetoeasy.model

/**
 * Represents different areas of the system UI on an Android device.
 *
 * This enum can be used to specify which part of the system UI you want to interact with,
 * such as adding margins or padding to avoid overlapping with these areas.
 */
enum class SystemArea {
    /** Includes both SystemBar and StatusBar */
    SystemBar,

    /** The space above where notifications and battery status are displayed */
    StatusBar,

    /** The space below where the 3-button or 2-button navigation or gesture buttons are displayed. */
    NavigationBar,

    /** The space occupied by the on-screen keyboard when it is visible */
    IME,

    /**
     * The space of the display cutout
     *
     * https://developer.android.com/develop/ui/views/layout/display-cutout
     * */
    Cutout,

    // MARK: More intuitive expression

    /** The larger space of the SystemBar or Cutout or IME */
    Everything,

    /** StatusBar */
    Top,

    /** The larger space of the StatusBar or Cutout */
    TopFull,

    /** NavigationBar */
    Bottom,

    /** The larger space of the NavigationBar or Cutout or IME */
    BottomFull,
}
