package io.github.parkjonghun.edgetoeasy

/**
 * Represents the directions in which a view should fill the available space.
 *
 * @property top If true, the view should fill the top direction.
 * @property left If true, the view should fill the left direction.
 * @property right If true, the view should fill the right direction.
 * @property bottom If true, the view should fill the bottom direction.
 */
data class FillDirection(
    val top: Boolean = true,
    val left: Boolean = true,
    val right: Boolean = true,
    val bottom: Boolean = true,
) {
    companion object Companion {
        @JvmStatic
        val None = FillDirection(
            top = false,
            left = false,
            right = false,
            bottom = false
        )

        @JvmStatic
        val All = FillDirection(
            top = true,
            left = true,
            right = true,
            bottom = true
        )

        @JvmStatic
        val Horizontal = FillDirection(
            top = false,
            left = true,
            right = true,
            bottom = false
        )

        @JvmStatic
        val Vertical = FillDirection(
            top = true,
            left = false,
            right = false,
            bottom = true
        )

        @JvmStatic
        val Left = FillDirection(
            top = false,
            left = true,
            right = false,
            bottom = false
        )

        @JvmStatic
        val Top = FillDirection(
            top = true,
            left = false,
            right = false,
            bottom = false
        )

        @JvmStatic
        val Right = FillDirection(
            top = false,
            left = false,
            right = true,
            bottom = false
        )

        @JvmStatic
        val Bottom = FillDirection(
            top = false,
            left = false,
            right = false,
            bottom = true
        )

        @JvmStatic
        val TopRight = FillDirection(
            top = true,
            left = false,
            right = true,
            bottom = false
        )

        @JvmStatic
        val TopLeft = FillDirection(
            top = true,
            left = true,
            right = false,
            bottom = false
        )

        @JvmStatic
        val BottomRight = FillDirection(
            top = false,
            left = false,
            right = true,
            bottom = true
        )

        @JvmStatic
        val BottomLeft = FillDirection(
            top = false,
            left = true,
            right = false,
            bottom = true
        )
    }
}
