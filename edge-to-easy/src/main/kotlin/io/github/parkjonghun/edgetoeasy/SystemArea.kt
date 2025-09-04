package io.github.parkjonghun.edgetoeasy

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

    /**
     * The space of the display cutout
     *
     * https://developer.android.com/develop/ui/views/layout/display-cutout
     * */
    Cutout,

    // MARK: More intuitive expression

    /** The larger space of the SystemBar or Cutout */
    Everything,

    /** StatusBar */
    Top,

    /** The larger space of the StatusBar or Cutout */
    TopFull,

    /** NavigationBar */
    Bottom,

    /** The larger space of the NavigationBar or Cutout */
    BottomFull,
}
