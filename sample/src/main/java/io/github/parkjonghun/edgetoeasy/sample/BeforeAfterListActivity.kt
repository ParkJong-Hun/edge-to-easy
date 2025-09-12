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

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import io.github.parkjonghun.edgetoeasy.core.extension.awayFrom
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea

class BeforeAfterListActivity : AppCompatActivity() {

    private var selectedSystemArea = SystemArea.StatusBar
    private var selectedFillMethod = "fillSpace"
    private var selectedEndMethod = "handleEdgeToEdge"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mainContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
        }

        val scrollView = ScrollView(this).apply {
            addView(mainContainer)
        }

        setContentView(scrollView)

        scrollView.awayFrom(SystemArea.Everything)
            .fillSpace()
            .handleEdgeToEdge()

        setupSelectorUI(mainContainer)
    }

    private fun setupSelectorUI(container: LinearLayout) {
        container.addView(createTitleText("DSL Before/After Examples"))
        container.addView(
            createDescriptionText(
                "Select the desired options and compare before and after applying them.",
            ),
        )

        // SystemArea selection spinner
        container.addView(createSectionTitle("System Area"))
        val systemAreaSpinner = createSystemAreaSpinner()
        container.addView(systemAreaSpinner)

        // Fill Method selection spinner
        container.addView(createSectionTitle("Fill Method"))
        val fillMethodSpinner = createFillMethodSpinner()
        container.addView(fillMethodSpinner)

        // End Method selection spinner
        container.addView(createSectionTitle("End Method"))
        val endMethodSpinner = createEndMethodSpinner()
        container.addView(endMethodSpinner)

        // Show current selected pattern
        val patternTextView = TextView(this).apply {
            textSize = 12f
            setTextColor(Color.DKGRAY)
            setPadding(20, 20, 20, 20)
            typeface = android.graphics.Typeface.MONOSPACE
            setBackgroundColor(Color.rgb(248, 248, 248))
        }
        container.addView(patternTextView)

        val buttonContainer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(20, 30, 20, 30)
        }

        val beforeButton = Button(this).apply {
            text = "Before"
            setOnClickListener {
                startExampleActivity(false, getPatternDescription())
            }
        }

        val afterButton = Button(this).apply {
            text = "After"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
                leftMargin = 30
            }
            setOnClickListener {
                startExampleActivity(true, getPatternDescription())
            }
        }

        buttonContainer.addView(beforeButton)
        buttonContainer.addView(afterButton)
        container.addView(buttonContainer)

        // Initial pattern text update
        updatePatternText(patternTextView)

        // Set spinner change listeners
        systemAreaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedSystemArea = getSystemAreas()[position].first
                updatePatternText(patternTextView)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        fillMethodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedFillMethod = getFillOptions()[position].first
                updatePatternText(patternTextView)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        endMethodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedEndMethod = getEndOptions()[position].first
                updatePatternText(patternTextView)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun startExampleActivity(isAfter: Boolean, patternDescription: String) {
        val intent = Intent(this, DslExampleActivity::class.java).apply {
            putExtra("systemArea", selectedSystemArea.name)
            putExtra("fillMethod", selectedFillMethod)
            putExtra("endMethod", selectedEndMethod)
            putExtra("isAfter", isAfter)
            putExtra("patternDescription", patternDescription)
        }
        startActivity(intent)
    }

    private fun getSystemAreas() = listOf(
        SystemArea.StatusBar to "StatusBar",
        SystemArea.NavigationBar to "NavigationBar",
        SystemArea.Everything to "Everything",
        SystemArea.SystemBar to "SystemBar",
        SystemArea.Top to "Top",
        SystemArea.TopFull to "TopFull",
        SystemArea.Bottom to "Bottom",
        SystemArea.BottomFull to "BottomFull",
        SystemArea.IME to "IME",
        SystemArea.Cutout to "Cutout",
    )

    private fun getFillOptions() = listOf(
        "fillSpace" to "fillSpace()",
        "fillSpace_all" to "fillSpace(FillDirection.All)",
        "fillSpace_vertical" to "fillSpace(FillDirection.Vertical)",
        "fillSpace_horizontal" to "fillSpace(FillDirection.Horizontal)",
        "fillSpace_top" to "fillSpace(FillDirection.Top)",
        "fillSpace_bottom" to "fillSpace(FillDirection.Bottom)",
        "fillSpace_left" to "fillSpace(FillDirection.Left)",
        "fillSpace_right" to "fillSpace(FillDirection.Right)",
        "fillWithPadding" to "fillWithPadding()",
        "fillWithPadding_all" to "fillWithPadding(FillDirection.All)",
        "fillWithPadding_vertical" to "fillWithPadding(FillDirection.Vertical)",
        "fillWithPadding_horizontal" to "fillWithPadding(FillDirection.Horizontal)",
        "fillWithMargin" to "fillWithMargin()",
        "fillWithMargin_all" to "fillWithMargin(FillDirection.All)",
        "fillWithMargin_vertical" to "fillWithMargin(FillDirection.Vertical)",
        "fillWithMargin_horizontal" to "fillWithMargin(FillDirection.Horizontal)",
    )

    private fun getEndOptions() = listOf(
        "handleEdgeToEdge" to "handleEdgeToEdge()",
        "continueToOthers" to "continueToOthers()",
    )

    private fun createSystemAreaSpinner(): Spinner {
        val spinner = Spinner(this)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            getSystemAreas().map { it.second },
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setPadding(20, 10, 20, 10)
        return spinner
    }

    private fun createFillMethodSpinner(): Spinner {
        val spinner = Spinner(this)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            getFillOptions().map { it.second },
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setPadding(20, 10, 20, 10)
        return spinner
    }

    private fun createEndMethodSpinner(): Spinner {
        val spinner = Spinner(this)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            getEndOptions().map { it.second },
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setPadding(20, 10, 20, 10)
        return spinner
    }

    private fun getPatternDescription(): String {
        val systemAreaName = getSystemAreas().find { it.first == selectedSystemArea }?.second ?: "StatusBar"
        val fillMethodName = getFillOptions().find { it.first == selectedFillMethod }?.second ?: "fillSpace()"
        val endMethodName = getEndOptions().find { it.first == selectedEndMethod }?.second ?: "handleEdgeToEdge()"

        return "root.awayFrom(SystemArea.$systemAreaName).$fillMethodName.$endMethodName"
    }

    private fun updatePatternText(textView: TextView) {
        textView.text = getPatternDescription()
    }

    private fun createSectionTitle(title: String): TextView = TextView(this).apply {
        text = title
        textSize = 16f
        setTextColor(Color.BLACK)
        setPadding(20, 20, 20, 5)
        setTypeface(typeface, android.graphics.Typeface.BOLD)
    }

    private fun createTitleText(text: String): TextView = TextView(this).apply {
        this.text = text
        textSize = 20f
        setTextColor(Color.BLACK)
        setPadding(20, 30, 20, 20)
        setTypeface(typeface, android.graphics.Typeface.BOLD)
    }

    private fun createDescriptionText(text: String): TextView = TextView(this).apply {
        this.text = text
        textSize = 14f
        setTextColor(Color.GRAY)
        setPadding(20, 10, 20, 20)
    }
}
