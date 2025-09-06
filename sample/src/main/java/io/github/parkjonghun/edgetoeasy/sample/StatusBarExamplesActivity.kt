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
package io.github.parkjonghun.edgetoeasy.sample

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.graphics.drawable.toDrawable
import io.github.parkjonghun.edgetoeasy.core.extension.awayFrom
import io.github.parkjonghun.edgetoeasy.core.extension.enableAutoStatusBarAppearance
import io.github.parkjonghun.edgetoeasy.core.extension.forceNavigationBarItemsDark
import io.github.parkjonghun.edgetoeasy.core.extension.forceNavigationBarItemsLight
import io.github.parkjonghun.edgetoeasy.core.extension.forceStatusBarItemsDark
import io.github.parkjonghun.edgetoeasy.core.extension.forceStatusBarItemsLight
import io.github.parkjonghun.edgetoeasy.core.extension.setAutoStatusBarAppearance
import io.github.parkjonghun.edgetoeasy.core.extension.shouldUseLightStatusBarContent
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import io.github.parkjonghun.edgetoeasy.core.extension.setAutoStatusBarAppearance as setViewAutoStatusBarAppearance

/**
 * Activity demonstrating automatic status bar appearance management
 * using the edge-to-easy-core library.
 */
class StatusBarExamplesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create main container
        val mainContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
        }

        val scrollView = ScrollView(this).apply {
            addView(mainContainer)
        }

        // Apply edge-to-edge handling
        scrollView.awayFrom(SystemArea.Everything)
            .fillSpace()
            .handleEdgeToEdge()

        setContentView(scrollView)

        setupStatusBarExamples(mainContainer)
    }

    private fun setupStatusBarExamples(container: LinearLayout) {
        // Title
        container.addView(createTitleText("Status Bar Examples"))

        // Current status
        val statusText = createStatusText("Ready")
        container.addView(statusText)

        // Section 1: Manual Status Bar Control
        container.addView(createSectionTitle("1. Manual Status Bar Control"))

        container.addView(
            createButton("Force Light Status Bar") {
                forceStatusBarItemsLight()
                statusText.text = "Status bar forced to light appearance (dark icons)"
            },
        )

        container.addView(
            createButton("Force Dark Status Bar") {
                forceStatusBarItemsDark()
                statusText.text = "Status bar forced to dark appearance (light icons)"
            },
        )

        // Section 2: Manual Navigation Bar Control
        container.addView(createSectionTitle("2. Manual Navigation Bar Control"))

        container.addView(
            createButton("Force Light Navigation Bar") {
                forceNavigationBarItemsLight()
                statusText.text = "Navigation bar forced to light appearance (dark icons)"
            },
        )

        container.addView(
            createButton("Force Dark Navigation Bar") {
                forceNavigationBarItemsDark()
                statusText.text = "Navigation bar forced to dark appearance (light icons)"
            },
        )

        // Section 3: Automatic Status Bar with Different Colors
        container.addView(createSectionTitle("3. Automatic Status Bar by Color"))

        container.addView(
            createButton("Auto with White Background") {
                setAutoStatusBarAppearance(Color.WHITE)
                window.setBackgroundDrawable(Color.WHITE.toDrawable())
                container.setBackgroundColor(Color.WHITE)
                statusText.text = "White background → Dark status bar icons"
            },
        )

        container.addView(
            createButton("Auto with Black Background") {
                setAutoStatusBarAppearance(Color.BLACK)
                container.setBackgroundColor(Color.BLACK)
                window.setBackgroundDrawable(Color.BLACK.toDrawable())
                statusText.text = "Black background → Light status bar icons"
                statusText.setTextColor(Color.WHITE)
            },
        )

        container.addView(
            createButton("Auto with Blue Background") {
                setAutoStatusBarAppearance(Color.BLUE)
                container.setBackgroundColor(Color.BLUE)
                window.setBackgroundDrawable(Color.BLUE.toDrawable())
                statusText.text = "Blue background → Light status bar icons"
                statusText.setTextColor(Color.WHITE)
            },
        )

        container.addView(
            createButton("Auto with Yellow Background") {
                setAutoStatusBarAppearance(Color.YELLOW)
                container.setBackgroundColor(Color.YELLOW)
                window.setBackgroundDrawable(Color.YELLOW.toDrawable())
                statusText.text = "Yellow background → Dark status bar icons"
                statusText.setTextColor(Color.BLACK)
            },
        )

        // Section 4: View-based Auto Status Bar
        container.addView(createSectionTitle("4. View-based Auto Status Bar"))

        // Create colored views that automatically set status bar
        val redView = createColoredView("Red View", Color.RED)
        container.addView(redView)

        container.addView(
            createButton("Auto Status Bar from Red View") {
                redView.setViewAutoStatusBarAppearance()
                statusText.text = "Status bar set automatically from red view background"
            },
        )

        val lightGrayView = createColoredView("Light Gray View", Color.LTGRAY)
        container.addView(lightGrayView)

        container.addView(
            createButton("Auto Status Bar from Gray View") {
                lightGrayView.setViewAutoStatusBarAppearance()
                statusText.text = "Status bar set automatically from gray view background"
            },
        )

        // Section 5: Enable Auto Status Bar (continuous monitoring)
        container.addView(createSectionTitle("5. Continuous Auto Status Bar"))

        val autoView = createColoredView("Auto-updating View", Color.GREEN)
        container.addView(autoView)

        container.addView(
            createButton("Enable Continuous Auto Status Bar") {
                autoView.enableAutoStatusBarAppearance()
                statusText.text = "Enabled continuous auto status bar for green view"
            },
        )

        container.addView(
            createButton("Change Auto View to Purple") {
                autoView.setBackgroundColor(Color.rgb(128, 0, 128))
                window.setBackgroundDrawable(Color.rgb(128, 0, 128).toDrawable())
                statusText.text = "Auto view changed to purple - status bar updates automatically"
            },
        )

        container.addView(
            createButton("Change Auto View to Cyan") {
                autoView.setBackgroundColor(Color.CYAN)
                window.setBackgroundDrawable(Color.CYAN.toDrawable())
                statusText.text = "Auto view changed to cyan - status bar updates automatically"
            },
        )

        // Section 6: Status Bar Content Inspection
        container.addView(createSectionTitle("6. Status Bar Content Analysis"))

        container.addView(
            createButton("Check Current View Status Bar Needs") {
                val shouldUseLightContent = container.shouldUseLightStatusBarContent()
                statusText.text = "Current container suggests: ${if (shouldUseLightContent) "Light" else "Dark"} status bar content"
            },
        )

        // Reset button
        container.addView(createSectionTitle("Reset"))
        container.addView(
            createButton("Reset to Default") {
                container.setBackgroundColor(Color.WHITE)
                setAutoStatusBarAppearance(Color.WHITE)
                window.setBackgroundDrawable(Color.WHITE.toDrawable())
                statusText.text = "Reset to default white background"
                statusText.setTextColor(Color.BLUE)
            },
        )

        // Add some padding at bottom
        container.addView(
            TextView(this).apply {
                text = ""
                setPadding(0, 50, 0, 50)
            },
        )
    }

    private fun createTitleText(text: String): TextView = TextView(this).apply {
        this.text = text
        textSize = 20f
        setTextColor(Color.BLACK)
        setPadding(20, 30, 20, 20)
        setTypeface(typeface, android.graphics.Typeface.BOLD)
    }

    private fun createSectionTitle(text: String): TextView = TextView(this).apply {
        this.text = text
        textSize = 16f
        setTextColor(Color.DKGRAY)
        setPadding(20, 20, 20, 10)
        setTypeface(typeface, android.graphics.Typeface.BOLD)
    }

    private fun createStatusText(text: String): TextView = TextView(this).apply {
        this.text = text
        textSize = 14f
        setTextColor(Color.BLUE)
        setPadding(20, 10, 20, 20)
        setBackgroundColor(Color.rgb(240, 248, 255))
    }

    private fun createButton(text: String, onClick: () -> Unit): Button = Button(this).apply {
        this.text = text
        setOnClickListener { onClick() }
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )
        params.setMargins(20, 5, 20, 5)
        layoutParams = params
    }

    private fun createColoredView(text: String, color: Int): TextView = TextView(this).apply {
        this.text = text
        textSize = 14f
        setTextColor(if (color == Color.YELLOW || color == Color.LTGRAY || color == Color.CYAN) Color.BLACK else Color.WHITE)
        setBackgroundColor(color)
        setPadding(20, 20, 20, 20)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            150, // height in pixels
        )
        params.setMargins(20, 10, 20, 10)
        layoutParams = params
    }
}
