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
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import io.github.parkjonghun.edgetoeasy.flow.extension.insetsChannel
import io.github.parkjonghun.edgetoeasy.flow.extension.insetsChannelFlow
import io.github.parkjonghun.edgetoeasy.flow.extension.insetsFlow
import io.github.parkjonghun.edgetoeasy.flow.extension.insetsStateFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
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
        // Try without enableEdgeToEdge() to see if it's interfering
        // enableEdgeToEdge()

        // Create main container
        val mainContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
        }

        val scrollView = ScrollView(this).apply {
            addView(mainContainer)
        }

        // SOLUTION: Don't intercept at DecorView level at all
        // Remove any existing insets listeners that might be consuming insets
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView, null)

        setContentView(scrollView)

        setupFlowExamples(mainContainer, scrollView)

        // Force initial insets dispatch to all views - try multiple times
        ViewCompat.requestApplyInsets(window.decorView)

        // Also try requesting on individual views
        mainContainer.post {
            ViewCompat.requestApplyInsets(mainContainer)
            ViewCompat.requestApplyInsets(scrollView)
        }
    }

    private fun setupFlowExamples(container: LinearLayout, scrollView: ScrollView) {
        // Title
        container.addView(createTitleText("Flow Examples"))

        // 1. Basic Flow example
        container.addView(createSectionTitle("1. Basic Flow (insetsFlow)"))
        val flowStatusText = createStatusText("Status: Waiting for insets...")
        container.addView(flowStatusText)

        // Monitor all insets with basic Flow - test different views
        // Test 1: LinearLayout (container) - debug what's happening
        val currentContainerInsets = ViewCompat.getRootWindowInsets(container)
            ?.getInsets(WindowInsetsCompat.Type.systemBars()) ?: androidx.core.graphics.Insets.NONE
        flowStatusText.text = "getRootWindowInsets: L=${currentContainerInsets.left}, T=${currentContainerInsets.top}, R=${currentContainerInsets.right}, B=${currentContainerInsets.bottom}"

        // Test using SharedInsetsFlowProvider instead of manual listener to avoid conflicts
        container.insetsFlow(SystemArea.Everything)
            .onEach { insets ->
                flowStatusText.text = "Fixed Flow(LinearLayout) - All Insets: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)

        // Remove manual listeners to prevent conflicts with SharedInsetsFlowProvider
        // All testing will now use the new shared system

        // Test 2: ScrollView
        val scrollViewTest = createStatusText("ScrollView Flow: Waiting...")
        container.addView(scrollViewTest)
        scrollView.insetsFlow()
            .onEach { insets ->
                scrollViewTest.text = "Flow(ScrollView) - All Insets: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)

        // Add debug info to see if Flow is working at all
        val debugText = createStatusText("Debug: Flow not triggered yet")
        container.addView(debugText)

        // Test SystemAreaInsetsMapper using insetsFlow instead of manual listener
        val mapperTest = createStatusText("SystemAreaMapper Test: Waiting...")
        container.addView(mapperTest)

        // Use insetsFlow instead of manual listener
        val mapperTestView = TextView(this)
        container.addView(mapperTestView)

        mapperTestView.insetsFlow(SystemArea.Everything)
            .onEach { insets ->
                mapperTest.text = "SystemAreaMapper: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)

        // debugText also use insetsFlow
        debugText.insetsFlow(SystemArea.Everything)
            .onEach { insets ->
                debugText.text = "Debug insetsFlow: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)

        // Test original edge-to-easy insetsFlow for comparison
        val originalFlowTest = createStatusText("Original insetsFlow: Waiting...")
        container.addView(originalFlowTest)

        // Use a fresh TextView to avoid listener conflicts
        val freshView = TextView(this)
        container.addView(freshView)

        // Add debugging to see what's different about freshView
        val freshViewDebug = createStatusText("FreshView Debug: Waiting...")
        container.addView(freshViewDebug)

        // Use insetsFlow on freshView instead of manual listener
        freshView.insetsFlow(SystemArea.Everything)
            .onEach { insets ->
                freshViewDebug.text = "FreshView insetsFlow: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)

        // Test if the issue is with multiple listeners on the same view
        // Comment out the manual listener and test insetsFlow
        /*
        freshView.insetsFlow()
            .onEach { insets ->
                originalFlowTest.text = "Original insetsFlow: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)
         */

        // Instead, test insetsFlow on a completely separate view
        val separateView = TextView(this)
        container.addView(separateView)
        separateView.insetsFlow()
            .onEach { insets ->
                originalFlowTest.text = "SeparateView insetsFlow: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)

        // Test with specific SystemAreas on TextView
        val systemAreaTest = createStatusText("SystemArea Tests: Waiting...")
        container.addView(systemAreaTest)

        debugText.insetsFlow(SystemArea.StatusBar)
            .onEach { insets ->
                val statusText = "StatusBar: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
                systemAreaTest.text = "TextView SystemArea Tests: $statusText"
            }
            .launchIn(lifecycleScope)

        // 2. StateFlow example
        container.addView(createSectionTitle("2. StateFlow (insetsStateFlow)"))
        val stateFlowStatusText = createStatusText("Status: Waiting for insets...")
        container.addView(stateFlowStatusText)

        // Monitor keyboard insets with StateFlow
        val keyboardStateFlow = container.insetsStateFlow(SystemArea.IME)
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
        addSystemAreaMonitor(container, "IME", SystemArea.IME)
        addSystemAreaMonitor(container, "Cutout", SystemArea.Cutout)

        // 7. Root View Test
        container.addView(createSectionTitle("7. Root View Direct Test"))
        val rootViewTest = createStatusText("Root View: Waiting...")
        container.addView(rootViewTest)

        // Test with window.decorView (absolute root)
        val decorView = window.decorView

        // Test 1: Direct callbackFlow without using edge-to-easy
        val directFlowTest = createStatusText("Direct callbackFlow: Waiting...")
        container.addView(directFlowTest)

        callbackFlow {
            val listener = OnApplyWindowInsetsListener { _, windowInsets ->
                val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                trySend(systemBars)
                windowInsets
            }

            ViewCompat.setOnApplyWindowInsetsListener(decorView, listener)

            // Send current insets immediately
            val currentInsets = ViewCompat.getRootWindowInsets(decorView)
                ?.getInsets(WindowInsetsCompat.Type.systemBars())
            trySend(currentInsets)

            awaitClose { ViewCompat.setOnApplyWindowInsetsListener(decorView, null) }
        }.onEach { insets ->
            directFlowTest.text = "Direct callbackFlow: L=${insets?.left}, T=${insets?.top}, R=${insets?.right}, B=${insets?.bottom}"
        }.launchIn(lifecycleScope)

        // Test 2: edge-to-easy insetsFlow for comparison
        decorView.insetsFlow()
            .onEach { insets ->
                rootViewTest.text = "DecorView Flow: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)

        // 8. Standard Android WindowInsets comparison
        container.addView(createSectionTitle("8. Standard Android WindowInsets (for comparison)"))
        val standardInsetsText = createStatusText("Standard: Waiting...")
        container.addView(standardInsetsText)

        // Add WindowInsets listener using standard Android API on scrollView
        ViewCompat.setOnApplyWindowInsetsListener(scrollView) { view, windowInsets ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = windowInsets.getInsets(WindowInsetsCompat.Type.ime())
            val statusBars = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBars = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())

            standardInsetsText.text = """Standard API:
                |SystemBars: L=${systemBars.left}, T=${systemBars.top}, R=${systemBars.right}, B=${systemBars.bottom}
                |StatusBar: L=${statusBars.left}, T=${statusBars.top}, R=${statusBars.right}, B=${statusBars.bottom}
                |NavBar: L=${navigationBars.left}, T=${navigationBars.top}, R=${navigationBars.right}, B=${navigationBars.bottom}
                |IME: L=${ime.left}, T=${ime.top}, R=${ime.right}, B=${ime.bottom}
            """.trimMargin()

            // Apply padding to container based on system bars to keep content visible
            container.setPadding(
                systemBars.left,
                systemBars.top + 20, // Add extra padding for better visibility
                systemBars.right,
                systemBars.bottom + 20,
            )

            windowInsets
        }

        // Final test: Simple listener on container itself
        val finalTest = createStatusText("Final Test: Waiting...")
        container.addView(finalTest)

        // Use insetsFlow instead of manual listener to avoid conflicts
        container.post {
            // Just request insets, no manual listener setup
            ViewCompat.requestApplyInsets(container)
        }

        // Use a separate view for this test to avoid conflicts
        val finalTestView = TextView(this)
        container.addView(finalTestView)

        finalTestView.insetsFlow(SystemArea.Everything)
            .onEach { insets ->
                finalTest.text = "Final Test insetsFlow: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)

        // TEST: Multiple flows on same view with new SharedInsetsFlowProvider
        container.addView(createSectionTitle("9. Multiple Flows on Same View Test"))

        val multiFlowTestView = TextView(this).apply {
            text = "Multi-flow test view"
            setPadding(20, 20, 20, 20)
            setBackgroundColor(Color.rgb(255, 248, 220))
        }
        container.addView(multiFlowTestView)

        val multiFlow1 = createStatusText("Flow 1 (Everything): Waiting...")
        container.addView(multiFlow1)

        val multiFlow2 = createStatusText("Flow 2 (StatusBar): Waiting...")
        container.addView(multiFlow2)

        val multiFlow3 = createStatusText("Flow 3 (NavigationBar): Waiting...")
        container.addView(multiFlow3)

        // Set up multiple flows on the same view - this should now work without conflicts
        multiFlowTestView.insetsFlow(SystemArea.Everything)
            .onEach { insets ->
                multiFlow1.text = "Flow 1 (Everything): L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)

        multiFlowTestView.insetsFlow(SystemArea.StatusBar)
            .onEach { insets ->
                multiFlow2.text = "Flow 2 (StatusBar): L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)

        multiFlowTestView.insetsFlow(SystemArea.NavigationBar)
            .onEach { insets ->
                multiFlow3.text = "Flow 3 (NavigationBar): L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            .launchIn(lifecycleScope)

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
