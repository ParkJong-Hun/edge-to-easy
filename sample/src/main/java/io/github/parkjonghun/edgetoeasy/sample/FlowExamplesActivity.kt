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
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.graphics.Insets
import androidx.lifecycle.lifecycleScope
import io.github.parkjonghun.edgetoeasy.core.extension.awayFrom
import io.github.parkjonghun.edgetoeasy.core.model.FillDirection
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import io.github.parkjonghun.edgetoeasy.flow.extension.insetsChannel
import io.github.parkjonghun.edgetoeasy.flow.extension.insetsChannelFlow
import io.github.parkjonghun.edgetoeasy.flow.extension.insetsFlow
import io.github.parkjonghun.edgetoeasy.flow.extension.insetsStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Activity demonstrating various ways to monitor WindowInsets using Flow, StateFlow, and Channel APIs
 * from the edge-to-easy-flow library.
 */
class FlowExamplesActivity : ComponentActivity() {
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

        setupFlowExamples(mainContainer)
    }

    private fun setupFlowExamples(container: LinearLayout) {
        // Title
        container.addView(createTitleText("Flow Examples"))

        // 1. Basic Flow example
        container.addView(createSectionTitle("1. Basic Flow (insetsFlow)"))
        val flowStatusText = createStatusText("Status: Waiting for insets...")
        container.addView(flowStatusText)

        // Monitor all insets with basic Flow
        container.insetsFlow()
            .onEach { insets ->
                flowStatusText.text = "Flow - All Insets: $insets"
            }
            .launchIn(lifecycleScope)

        // 2. StateFlow example
        container.addView(createSectionTitle("2. StateFlow (insetsStateFlow)"))
        val stateFlowStatusText = createStatusText("Status: Waiting for insets...")
        container.addView(stateFlowStatusText)

        // Monitor keyboard insets with StateFlow
        val keyboardStateFlow = container.insetsStateFlow(SystemArea.Ime)
        stateFlowStatusText.text = "StateFlow - Current Keyboard: ${keyboardStateFlow.value}"

        keyboardStateFlow
            .map { it.bottom > 0 }
            .distinctUntilChanged()
            .onEach { isKeyboardVisible ->
                val keyboardHeight = keyboardStateFlow.value.bottom
                stateFlowStatusText.text =
                    "StateFlow - Keyboard ${if (isKeyboardVisible) "VISIBLE" else "HIDDEN"} (height: $keyboardHeight)"
            }
            .launchIn(lifecycleScope)

        // 3. Channel example
        container.addView(createSectionTitle("3. Channel (insetsChannel)"))
        val channelStatusText = createStatusText("Status: Waiting for insets...")
        container.addView(channelStatusText)

        // Monitor navigation bar with Channel
        val navChannel = container.insetsChannel(SystemArea.NavigationBar)
        lifecycleScope.launch {
            for (navInsets in navChannel) {
                channelStatusText.text = "Channel - Navigation Bar: $navInsets"
            }
        }

        // 4. Channel Flow example
        container.addView(createSectionTitle("4. Channel Flow (insetsChannelFlow)"))
        val channelFlowStatusText = createStatusText("Status: Waiting for insets...")
        container.addView(channelFlowStatusText)

        // Monitor status bar with Channel Flow
        container.insetsChannelFlow(SystemArea.StatusBar)
            .onEach { statusInsets ->
                channelFlowStatusText.text = "ChannelFlow - Status Bar: $statusInsets"
            }
            .launchIn(lifecycleScope)

        // 5. Combined example
        container.addView(createSectionTitle("5. System Bars Combined"))
        val combinedStatusText = createStatusText("Status: Waiting for insets...")
        container.addView(combinedStatusText)

        container.insetsFlow(SystemArea.SystemBar)
            .onEach { systemInsets ->
                val totalHeight = systemInsets.top + systemInsets.bottom
                combinedStatusText.text = "Combined - System Bars Total Height: $totalHeight px"
            }
            .launchIn(lifecycleScope)

        // 6. Real-time all areas monitoring
        container.addView(createSectionTitle("6. All System Areas Monitor"))
        addSystemAreaMonitor(container, "Status Bar", SystemArea.StatusBar)
        addSystemAreaMonitor(container, "Navigation Bar", SystemArea.NavigationBar)
        addSystemAreaMonitor(container, "IME", SystemArea.Ime)
        addSystemAreaMonitor(container, "Cutout", SystemArea.Cutout)

        // Add some padding at bottom
        container.addView(
            TextView(this).apply {
                text = ""
                textSize = 12f
                setPadding(0, 50, 0, 50)
            },
        )
    }

    private fun addSystemAreaMonitor(container: LinearLayout, label: String, systemArea: SystemArea) {
        val statusText = createStatusText("$label: Waiting...")
        container.addView(statusText)

        container.insetsFlow(systemArea)
            .onEach { insets ->
                statusText.text = "$label: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)
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
        setPadding(20, 5, 20, 5)
        setBackgroundColor(Color.rgb(240, 248, 255))
    }
}
