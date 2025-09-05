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

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import io.github.parkjonghun.edgetoeasy.core.util.ColorUtils

/**
 * Automatically sets the status bar appearance based on this view's background color.
 * Analyzes the background color and chooses appropriate light/dark status bar content.
 *
 * Usage example:
 * ```kotlin
 * view.setAutoStatusBarAppearance()
 * ```
 */
fun View.setAutoStatusBarAppearance() {
    val activity = findActivity() ?: return
    val backgroundColor = getViewBackgroundColor()
    activity.setAutoStatusBarAppearance(backgroundColor)
}

/**
 * Automatically sets the status bar appearance based on a specific background color.
 *
 * @param backgroundColor The background color to analyze for status bar appearance
 *
 * Usage example:
 * ```kotlin
 * view.setAutoStatusBarAppearance(Color.BLUE)
 * ```
 */
fun View.setAutoStatusBarAppearance(
    @ColorInt backgroundColor: Int,
) {
    val activity = findActivity() ?: return
    activity.setAutoStatusBarAppearance(backgroundColor)
}

/**
 * Sets up automatic status bar appearance that updates whenever this view's background changes.
 * This creates a listener that monitors background changes and updates the status bar accordingly.
 *
 * Usage example:
 * ```kotlin
 * view.enableAutoStatusBarAppearance()
 * ```
 */
fun View.enableAutoStatusBarAppearance() {
    // Set initial appearance
    setAutoStatusBarAppearance()

    // Monitor background changes
    addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        setAutoStatusBarAppearance()
    }
}

/**
 * Checks if the current view background would require light status bar content.
 *
 * @return true if light status bar content should be used (for dark backgrounds)
 */
fun View.shouldUseLightStatusBarContent(): Boolean {
    val backgroundColor = getViewBackgroundColor()
    return ColorUtils.shouldUseLightStatusBar(backgroundColor)
}

/**
 * Gets the recommended status bar content color based on this view's background.
 *
 * @return Color.WHITE for dark backgrounds, Color.BLACK for light backgrounds
 */
@ColorInt
fun View.getRecommendedStatusBarContentColor(): Int {
    val backgroundColor = getViewBackgroundColor()
    return ColorUtils.getRecommendedStatusBarColor(backgroundColor)
}

/**
 * Extracts the background color from this view's background drawable.
 * Falls back to white if no background color is found.
 *
 * @return The view's background color or Color.WHITE as fallback
 */
@ColorInt
private fun View.getViewBackgroundColor(): Int =
    when (val background = this.background) {
        is ColorDrawable -> background.color
        null -> {
            // Try to get color from parent or context
            (parent as? View)?.let { parentView ->
                parentView.getViewBackgroundColor()
            } ?: Color.WHITE
        }
        else -> {
            // Try to extract color from complex drawable
            extractColorFromDrawable(background) ?: Color.WHITE
        }
    }

/**
 * Attempts to extract a representative color from a drawable.
 * Returns null if unable to extract a meaningful color.
 */
@ColorInt
private fun extractColorFromDrawable(drawable: Drawable): Int? {
    // Try to get the dominant color from tinted drawables
    return try {
        val tintedDrawable = DrawableCompat.wrap(drawable.mutate())
        val colorFilter = DrawableCompat.getColorFilter(tintedDrawable)
        // This is a simplified approach - in practice, you might want to use
        // more sophisticated color extraction like Palette API for bitmaps
        null
    } catch (e: Exception) {
        null
    }
}

/**
 * Finds the parent Activity of this view.
 * Returns null if no Activity is found in the context chain.
 */
private fun View.findActivity(): Activity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}
