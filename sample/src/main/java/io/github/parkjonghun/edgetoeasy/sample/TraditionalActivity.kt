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

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TraditionalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Create a simple layout programmatically
        val rootView =
            TextView(this).apply {
                text =
                    "Traditional Activity\n\nThis is a traditional Android activity that uses the Edge to Easy library. The content should properly handle system insets and display edge-to-edge."
                textSize = 16f
                setPadding(32, 32, 32, 32)
            }

        setContentView(rootView)

        // Apply edge-to-edge insets using ViewCompat
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left + 32,
                systemBars.top + 32,
                systemBars.right + 32,
                systemBars.bottom + 32,
            )
            insets
        }
    }
}
