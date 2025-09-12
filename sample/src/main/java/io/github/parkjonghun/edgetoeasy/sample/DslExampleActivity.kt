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
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.github.parkjonghun.edgetoeasy.core.extension.awayFrom
import io.github.parkjonghun.edgetoeasy.core.model.FillDirection
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea

class DslExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val systemAreaName = intent.getStringExtra("systemArea") ?: "StatusBar"
        val fillMethod = intent.getStringExtra("fillMethod") ?: "fillSpace"
        val endMethod = intent.getStringExtra("endMethod") ?: "handleEdgeToEdge"
        val isAfter = intent.getBooleanExtra("isAfter", false)
        val patternDescription = intent.getStringExtra("patternDescription") ?: ""

        val systemArea = SystemArea.valueOf(systemAreaName)

        setupUI(systemArea, fillMethod, endMethod, isAfter, patternDescription)
    }

    private fun setupUI(
        systemArea: SystemArea,
        fillMethod: String,
        endMethod: String,
        isAfter: Boolean,
        patternDescription: String
    ) {
        val mainContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
        }

        // Toolbar
        val toolbar = Toolbar(this).apply {
            setBackgroundColor(Color.rgb(100, 149, 237))
            title = if (isAfter) "After" else "Before"
            setTitleTextColor(Color.WHITE)
            navigationIcon = resources.getDrawable(android.R.drawable.ic_menu_close_clear_cancel, theme)
            setNavigationOnClickListener { finish() }
        }

        mainContainer.addView(toolbar)

        // Pattern Description
        val patternTextView = TextView(this).apply {
            text = patternDescription
            textSize = 12f
            setTextColor(Color.DKGRAY)
            setPadding(20, 20, 20, 10)
            typeface = android.graphics.Typeface.MONOSPACE
            setBackgroundColor(Color.rgb(248, 248, 248))
        }
        mainContainer.addView(patternTextView)

        // Status Text
        val statusTextView = TextView(this).apply {
            text = if (isAfter) "DSL is applied" else "DSL is not applied"
            textSize = 16f
            setTextColor(if (isAfter) Color.rgb(0, 150, 0) else Color.RED)
            setPadding(20, 15, 20, 15)
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        }
        mainContainer.addView(statusTextView)

        createSampleContent(mainContainer, systemArea, fillMethod, endMethod, isAfter)

        setContentView(mainContainer)

        // When "After", apply the DSL
        if (isAfter) {
            applyDSL(mainContainer, systemArea, fillMethod, endMethod)
        }
    }

    private fun createSampleContent(
        container: LinearLayout,
        systemArea: SystemArea,
        fillMethod: String,
        endMethod: String,
        isAfter: Boolean
    ) {
        // Content Area
        val contentView = TextView(this).apply {
            text = buildString {
                append("Content Area\n\n")
                append("SystemArea: ${systemArea.name}\n")
                append("FillMethod: $fillMethod\n")
                append("EndMethod: $endMethod\n")
                append("Applied: ${if (isAfter) "YES" else "NO"}\n\n")
                append("Ensure that this content is properly separated from the system UI.\n")
                append("Look at the spacing between the status bar, navigation bar, keyboard, etc.")
            }
            textSize = 14f
            setTextColor(Color.BLACK)
            setBackgroundColor(Color.rgb(240, 240, 240))
            setPadding(20, 30, 20, 30)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0
            ).apply {
                weight = 1f
                setMargins(0, 10, 0, 10)
            }
        }
        container.addView(contentView)

        // Footer Area
        val footerView = TextView(this).apply {
            text = "Footer Area\nCheck the spacing with the bottom navigation"
            textSize = 14f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(100, 100, 255))
            setPadding(20, 40, 20, 40)
        }
        container.addView(footerView)

        // EditText for keyboard testing
        val editText = EditText(this).apply {
            hint = "Input field for keyboard test"
            textSize = 14f
            setPadding(20, 20, 20, 20)
            setBackgroundColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(20, 10, 20, 10)
            }
        }
        container.addView(editText)

        // Edge cases
        if (endMethod == "continueToOthers") {
            val individualView1 = TextView(this).apply {
                text = "Individual View 1\nThis view applies the DSL individually."
                textSize = 12f
                setTextColor(Color.BLACK)
                setBackgroundColor(Color.rgb(255, 255, 200))
                setPadding(15, 20, 15, 20)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(10, 5, 10, 5)
                }
            }

            val individualView2 = TextView(this).apply {
                text = "Individual View 2\nDifferent SystemAreas can be applied to each"
                textSize = 12f
                setTextColor(Color.BLACK)
                setBackgroundColor(Color.rgb(200, 255, 200))
                setPadding(15, 20, 15, 20)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(10, 5, 10, 5)
                }
            }

            container.addView(individualView1)
            container.addView(individualView2)

            if (isAfter) {
                individualView1.awayFrom(SystemArea.StatusBar)
                    .fillSpace(FillDirection.Top)
                    .handleEdgeToEdge()

                individualView2.awayFrom(SystemArea.NavigationBar)
                    .fillSpace(FillDirection.Bottom)
                    .handleEdgeToEdge()
            }
        }
    }

    private fun applyDSL(
        root: LinearLayout,
        systemArea: SystemArea,
        fillMethod: String,
        endMethod: String
    ) {
        val chain = root.awayFrom(systemArea)

        val filledChain = when (fillMethod) {
            "fillSpace" -> chain.fillSpace()
            "fillSpace_all" -> chain.fillSpace(FillDirection.All)
            "fillSpace_vertical" -> chain.fillSpace(FillDirection.Vertical)
            "fillSpace_horizontal" -> chain.fillSpace(FillDirection.Horizontal)
            "fillSpace_top" -> chain.fillSpace(FillDirection.Top)
            "fillSpace_bottom" -> chain.fillSpace(FillDirection.Bottom)
            "fillSpace_left" -> chain.fillSpace(FillDirection.Left)
            "fillSpace_right" -> chain.fillSpace(FillDirection.Right)
            "fillWithPadding" -> chain.fillWithPadding()
            "fillWithPadding_all" -> chain.fillWithPadding(FillDirection.All)
            "fillWithPadding_vertical" -> chain.fillWithPadding(FillDirection.Vertical)
            "fillWithPadding_horizontal" -> chain.fillWithPadding(FillDirection.Horizontal)
            "fillWithMargin" -> chain.fillWithMargin()
            "fillWithMargin_all" -> chain.fillWithMargin(FillDirection.All)
            "fillWithMargin_vertical" -> chain.fillWithMargin(FillDirection.Vertical)
            "fillWithMargin_horizontal" -> chain.fillWithMargin(FillDirection.Horizontal)
            else -> chain.fillSpace()
        }

        when (endMethod) {
            "handleEdgeToEdge" -> filledChain.handleEdgeToEdge()
            "continueToOthers" -> filledChain.continueToOthers()
        }
    }
}