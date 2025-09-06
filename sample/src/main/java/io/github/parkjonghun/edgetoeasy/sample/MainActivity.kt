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
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.parkjonghun.edgetoeasy.sample.ui.theme.EdgeToEasySampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            EdgeToEasySampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen(
                        onTraditionalClick = {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    TraditionalActivity::class.java,
                                ),
                            )
                        },
                        onFragmentClick = {
                            startActivity(Intent(this@MainActivity, FragmentActivity::class.java))
                        },
                        onFlowExamplesClick = {
                            startActivity(Intent(this@MainActivity, FlowExamplesActivity::class.java))
                        },
                        onStatusBarExamplesClick = {
                            startActivity(Intent(this@MainActivity, StatusBarExamplesActivity::class.java))
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    onTraditionalClick: () -> Unit,
    onFragmentClick: () -> Unit,
    onFlowExamplesClick: () -> Unit = {},
    onStatusBarExamplesClick: () -> Unit = {},
) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Edge to Easy Sample",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Text(
            text = "This sample app demonstrates how to use the Edge to Easy library to enable edge-to-edge display in your Android app.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        Button(
            onClick = onTraditionalClick,
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            Text("Traditional Activity")
        }

        Button(
            onClick = onFragmentClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            Text("Fragment Activity")
        }

        Button(
            onClick = onFlowExamplesClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            Text("Flow Examples")
        }

        Button(
            onClick = onStatusBarExamplesClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Status Bar Examples")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    EdgeToEasySampleTheme {
        MainScreen(
            onTraditionalClick = {},
            onFragmentClick = {},
            onFlowExamplesClick = {},
            onStatusBarExamplesClick = {},
        )
    }
}
