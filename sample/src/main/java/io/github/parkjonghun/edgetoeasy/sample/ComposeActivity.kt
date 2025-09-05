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
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.Insets
import io.github.parkjonghun.edgetoeasy.core.extension.enableEdgeToEdgeWithAutoStatusBar
import io.github.parkjonghun.edgetoeasy.core.extension.setAutoStatusBarAppearance
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import io.github.parkjonghun.edgetoeasy.flow.extension.insetsFlow
import io.github.parkjonghun.edgetoeasy.flow.extension.insetsStateFlow
import io.github.parkjonghun.edgetoeasy.sample.ui.theme.EdgeToEasySampleTheme
import kotlinx.coroutines.flow.map

/**
 * Compose Activity demonstrating how to use the Edge to Easy library
 * within Jetpack Compose, including hybrid Compose-View integration.
 */
class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge with auto status bar
        enableEdgeToEdgeWithAutoStatusBar(androidx.compose.ui.graphics.Color.White.toArgb())

        setContent {
            EdgeToEasySampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    ComposeScreen()
                }
            }
        }
    }
}

@Composable
fun ComposeScreen() {
    val context = LocalContext.current as ComponentActivity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // Title
        Text(
            text = "Compose + Edge to Easy",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Text(
            text = "This demonstrates Compose integration with the Edge to Easy library.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        // Section 1: Status Bar Control
        SectionTitle("1. Auto Status Bar Control")

        var backgroundColor by remember { mutableStateOf(androidx.compose.ui.graphics.Color.White) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(backgroundColor),
        ) {
            Column(
                modifier = Modifier
                    .background(backgroundColor)
                    .padding(16.dp),
            ) {
                Text("Current Background Color")

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp),
                ) {
                    Button(
                        onClick = {
                            backgroundColor = androidx.compose.ui.graphics.Color.White
                            context.setAutoStatusBarAppearance(Color.WHITE)
                        },
                    ) {
                        Text("White")
                    }

                    Button(
                        onClick = {
                            backgroundColor = androidx.compose.ui.graphics.Color.Black
                            context.setAutoStatusBarAppearance(Color.BLACK)
                        },
                    ) {
                        Text("Black")
                    }

                    Button(
                        onClick = {
                            backgroundColor = androidx.compose.ui.graphics.Color.Blue
                            context.setAutoStatusBarAppearance(Color.BLUE)
                        },
                    ) {
                        Text("Blue")
                    }
                }
            }
        }

        // Section 2: Insets Monitoring in Compose
        SectionTitle("2. Insets Monitoring in Compose")

        AndroidView(
            factory = { context ->
                TextView(context).apply {
                    text = "Monitoring insets..."
                    setPadding(20, 20, 20, 20)
                    setBackgroundColor(Color.rgb(240, 248, 255))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        ) { textView ->
            // Monitor insets using StateFlow
            LaunchedEffect(textView) {
                textView.insetsStateFlow(SystemArea.Everything)
                    .collect { insets ->
                        textView.text = "All Insets: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
                    }
            }
        }

        // Section 3: Keyboard Monitoring
        SectionTitle("3. Keyboard (IME) Monitoring")

        var keyboardStatus by remember { mutableStateOf("Hidden") }
        var textInput by remember { mutableStateOf("") }

        AndroidView(
            factory = { context ->
                TextView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )
                    setPadding(20, 20, 20, 20)
                    setBackgroundColor(Color.rgb(255, 248, 220))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) { textView ->
            // Monitor keyboard with Flow
            LaunchedEffect(textView) {
                textView.insetsFlow(SystemArea.Ime)
                    .map { it.bottom > 0 }
                    .collect { isKeyboardVisible ->
                        keyboardStatus = if (isKeyboardVisible) "VISIBLE" else "HIDDEN"
                        textView.text = "Keyboard Status: $keyboardStatus"
                    }
            }
        }

        TextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("Type here to show keyboard") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )

        // Section 4: System Areas Monitoring
        SectionTitle("4. Individual System Areas")

        SystemAreaMonitor("Status Bar", SystemArea.StatusBar)
        SystemAreaMonitor("Navigation Bar", SystemArea.NavigationBar)
        SystemAreaMonitor("IME (Keyboard)", SystemArea.Ime)
        SystemAreaMonitor("Cutout", SystemArea.Cutout)

        // Section 5: LazyColumn with Insets
        SectionTitle("5. LazyColumn Example")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.1f)),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(items = (1..30).map { "LazyColumn Item $it" }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }

        // Add some bottom spacing
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 16.dp),
    )
}

@Composable
fun SystemAreaMonitor(label: String, systemArea: SystemArea) {
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                text = "$label: Waiting..."
                setPadding(16, 12, 16, 12)
                setBackgroundColor(Color.rgb(245, 245, 245))
                textSize = 12f
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
    ) { textView ->
        LaunchedEffect(textView) {
            textView.insetsStateFlow(systemArea)
                .collect { insets ->
                    textView.text = "$label: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComposeScreenPreview() {
    EdgeToEasySampleTheme {
        ComposeScreen()
    }
}
