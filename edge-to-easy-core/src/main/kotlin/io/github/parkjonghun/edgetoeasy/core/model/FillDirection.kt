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
package io.github.parkjonghun.edgetoeasy.core.model

/**
 * Represents the directions in which a view should fill the available space.
 *
 * @property top If true, the view should fill the top direction.
 * @property left If true, the view should fill the left direction.
 * @property right If true, the view should fill the right direction.
 * @property bottom If true, the view should fill the bottom direction.
 */
public data class FillDirection(
    val top: Boolean = true,
    val left: Boolean = true,
    val right: Boolean = true,
    val bottom: Boolean = true,
) {
    public companion object Companion {
        @JvmStatic
        public val None =
            FillDirection(
                top = false,
                left = false,
                right = false,
                bottom = false,
            )

        @JvmStatic
        public val All =
            FillDirection(
                top = true,
                left = true,
                right = true,
                bottom = true,
            )

        @JvmStatic
        public val Horizontal =
            FillDirection(
                top = false,
                left = true,
                right = true,
                bottom = false,
            )

        @JvmStatic
        public val Vertical =
            FillDirection(
                top = true,
                left = false,
                right = false,
                bottom = true,
            )

        @JvmStatic
        public val Left =
            FillDirection(
                top = false,
                left = true,
                right = false,
                bottom = false,
            )

        @JvmStatic
        public val Top =
            FillDirection(
                top = true,
                left = false,
                right = false,
                bottom = false,
            )

        @JvmStatic
        public val Right =
            FillDirection(
                top = false,
                left = false,
                right = true,
                bottom = false,
            )

        @JvmStatic
        public val Bottom =
            FillDirection(
                top = false,
                left = false,
                right = false,
                bottom = true,
            )

        @JvmStatic
        public val TopRight =
            FillDirection(
                top = true,
                left = false,
                right = true,
                bottom = false,
            )

        @JvmStatic
        public val TopLeft =
            FillDirection(
                top = true,
                left = true,
                right = false,
                bottom = false,
            )

        @JvmStatic
        public val BottomRight =
            FillDirection(
                top = false,
                left = false,
                right = true,
                bottom = true,
            )

        @JvmStatic
        public val BottomLeft =
            FillDirection(
                top = false,
                left = true,
                right = false,
                bottom = true,
            )
    }
}

/**
 * Represents the vertical directions in which a view should fill the available space.
 *
 * @property top If true, the view should fill the top direction.
 * @property bottom If true, the view should fill the bottom direction.
 */
public data class FillVerticalDirection(
    val top: Boolean = true,
    val bottom: Boolean = true,
) {
    public companion object Companion {
        @JvmStatic
        public val None =
            FillVerticalDirection(
                top = false,
                bottom = false,
            )

        @JvmStatic
        public val All =
            FillVerticalDirection(
                top = true,
                bottom = true,
            )

        @JvmStatic
        public val Top =
            FillVerticalDirection(
                top = true,
                bottom = false,
            )

        @JvmStatic
        public val Bottom =
            FillVerticalDirection(
                top = false,
                bottom = true,
            )
    }
}
