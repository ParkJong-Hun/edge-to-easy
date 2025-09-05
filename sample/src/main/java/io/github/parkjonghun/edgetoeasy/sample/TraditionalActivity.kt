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
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.parkjonghun.edgetoeasy.core.extension.addSystemAreaSpacer
import io.github.parkjonghun.edgetoeasy.core.extension.awayFrom
import io.github.parkjonghun.edgetoeasy.core.extension.enableAutoStatusBarAppearance
import io.github.parkjonghun.edgetoeasy.core.extension.setAutoStatusBarAppearance
import io.github.parkjonghun.edgetoeasy.core.model.FillDirection
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import io.github.parkjonghun.edgetoeasy.flow.extension.insetsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * Traditional Activity demonstrating comprehensive usage of the Edge to Easy library
 * with various system areas and edge-to-edge handling techniques.
 */
class TraditionalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set automatic status bar appearance based on background
        setAutoStatusBarAppearance(Color.rgb(240, 248, 255))

        // Create main container with light blue background
        val mainContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(240, 248, 255))
        }

        val scrollView = ScrollView(this).apply {
            addView(mainContainer)
        }

        setContentView(scrollView)

        // Apply edge-to-edge handling using our library
        scrollView.awayFrom(SystemArea.Everything)
            .fillSpace()
            .handleEdgeToEdge()

        setupTraditionalExamples(mainContainer)
    }

    private fun setupTraditionalExamples(container: LinearLayout) {
        // Title
        container.addView(createTitleText("Traditional Activity Examples"))

        // Description
        container.addView(
            createDescriptionText(
                "This activity demonstrates various edge-to-edge techniques using the Edge to Easy library " +
                    "in a traditional Android View-based activity.",
            ),
        )

        // Section 1: Different System Areas
        container.addView(createSectionTitle("1. Different System Areas"))

        // Status bar only section
        val statusBarSection = createColoredSection("Status Bar Only", Color.rgb(255, 240, 240))
        statusBarSection.awayFrom(SystemArea.StatusBar)
            .fillSpace(FillDirection.Vertical)
            .handleEdgeToEdge()
        container.addView(statusBarSection)

        // Navigation bar only section
        val navBarSection = createColoredSection("Navigation Bar Only", Color.rgb(240, 255, 240))
        navBarSection.awayFrom(SystemArea.NavigationBar)
            .fillSpace(FillDirection.Vertical)
            .handleEdgeToEdge()
        container.addView(navBarSection)

        // Section 2: Keyboard Handling
        container.addView(createSectionTitle("2. Keyboard (IME) Handling"))

        val editText = EditText(this).apply {
            hint = "Type here to show keyboard..."
            setPadding(20, 20, 20, 20)
            setBackgroundColor(Color.WHITE)
        }
        container.addView(editText)

        val keyboardStatusText = TextView(this).apply {
            text = "Keyboard Status: Hidden"
            setPadding(20, 10, 20, 10)
            setBackgroundColor(Color.rgb(255, 255, 240))
        }
        container.addView(keyboardStatusText)

        // Monitor keyboard with Flow
        editText.insetsFlow(SystemArea.IME)
            .map { it.bottom > 0 }
            .distinctUntilChanged()
            .onEach { isKeyboardVisible ->
                keyboardStatusText.text = if (isKeyboardVisible) {
                    "Keyboard Status: VISIBLE"
                } else {
                    "Keyboard Status: HIDDEN"
                }
            }
            .launchIn(lifecycleScope)

        // Apply keyboard handling to edit text
        editText.awayFrom(SystemArea.IME)
            .fillSpace(FillDirection.Vertical)
            .continueToOthers()

        // Section 3: System Area Spacers
        container.addView(createSectionTitle("3. System Area Spacers"))

        container.addView(
            TextView(this).apply {
                text = "Content before status bar spacer"
                setPadding(20, 10, 20, 10)
            },
        )

        // Add spacer for status bar
        container.addSystemAreaSpacer(SystemArea.StatusBar, addToTop = true)

        container.addView(
            TextView(this).apply {
                text = "Content after status bar spacer (should be below status bar)"
                setPadding(20, 10, 20, 10)
                setBackgroundColor(Color.rgb(255, 245, 245))
            },
        )

        // Section 4: Multiple Views Chain
        container.addView(createSectionTitle("4. Multiple Views Chain"))

        val headerView = createColoredSection("Header (Away from Status Bar)", Color.rgb(255, 230, 230))
        val contentView = createColoredSection("Content (No specific handling)", Color.rgb(230, 255, 230))
        val footerView = createColoredSection("Footer (Away from Navigation Bar)", Color.rgb(230, 230, 255))

        container.addView(headerView)
        container.addView(contentView)
        container.addView(footerView)

        // Apply edge handling to individual views
        headerView.awayFrom(SystemArea.StatusBar)
            .fillSpace(FillDirection.Vertical)
            .handleEdgeToEdge()
            
        footerView.awayFrom(SystemArea.NavigationBar)
            .fillSpace(FillDirection.Vertical)
            .handleEdgeToEdge()

        // Section 5: Auto Status Bar
        container.addView(createSectionTitle("5. Auto Status Bar"))

        val autoStatusBarView = createColoredSection("Auto Status Bar View", Color.BLUE)
        container.addView(autoStatusBarView)

        container.addView(
            Button(this).apply {
                text = "Enable Auto Status Bar for Blue View"
                setOnClickListener {
                    autoStatusBarView.enableAutoStatusBarAppearance()
                }
            },
        )

        container.addView(
            Button(this).apply {
                text = "Change to Yellow Background"
                setOnClickListener {
                    autoStatusBarView.setBackgroundColor(Color.YELLOW)
                }
            },
        )

        container.addView(
            Button(this).apply {
                text = "Change to Dark Green Background"
                setOnClickListener {
                    autoStatusBarView.setBackgroundColor(Color.rgb(0, 100, 0))
                }
            },
        )

        // Section 6: Real-time Insets Monitor
        container.addView(createSectionTitle("6. Real-time Insets Monitor"))

        val insetsMonitor = TextView(this).apply {
            text = "Monitoring insets..."
            setPadding(20, 20, 20, 20)
            setBackgroundColor(Color.rgb(250, 250, 250))
            textSize = 12f
        }
        container.addView(insetsMonitor)

        // Monitor all insets in real-time
        insetsMonitor.insetsFlow(SystemArea.Everything)
            .onEach { insets ->
                insetsMonitor.text = "All Insets: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)

        // Add some bottom padding
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

    private fun createDescriptionText(text: String): TextView = TextView(this).apply {
        this.text = text
        textSize = 14f
        setTextColor(Color.GRAY)
        setPadding(20, 10, 20, 20)
    }

    private fun createColoredSection(text: String, color: Int): TextView = TextView(this).apply {
        this.text = text
        textSize = 14f
        setTextColor(Color.BLACK)
        setBackgroundColor(color)
        setPadding(20, 30, 20, 30)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )
        params.setMargins(20, 10, 20, 10)
        layoutParams = params
    }
}
