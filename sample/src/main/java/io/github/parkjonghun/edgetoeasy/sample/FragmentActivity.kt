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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.parkjonghun.edgetoeasy.core.extension.addSpaceForLastItem
import io.github.parkjonghun.edgetoeasy.core.extension.addSystemAreaSpacer
import io.github.parkjonghun.edgetoeasy.core.extension.awayFrom
import io.github.parkjonghun.edgetoeasy.core.extension.enableAutoStatusBarAppearance
import io.github.parkjonghun.edgetoeasy.core.model.FillDirection
import io.github.parkjonghun.edgetoeasy.core.model.FillVerticalDirection
import io.github.parkjonghun.edgetoeasy.core.model.SystemArea
import io.github.parkjonghun.edgetoeasy.flow.extension.insetsStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Fragment Activity demonstrating how to use the Edge to Easy library
 * within Fragment-based architecture.
 */
class FragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, SampleFragment())
                .commit()
        }
    }
}

/**
 * Sample fragment demonstrating various edge-to-edge techniques
 * using the Edge to Easy library within a Fragment.
 */
class SampleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val mainContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(248, 248, 255))
        }

        val scrollView = ScrollView(requireContext()).apply {
            addView(mainContainer)
        }

        return scrollView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableEdgeToEdge()

        val scrollView = view as ScrollView
        val mainContainer = scrollView.getChildAt(0) as LinearLayout

        setupFragmentExamples(mainContainer)

        // Apply edge-to-edge handling
        scrollView.awayFrom(SystemArea.Everything)
            .fillSpace()
            .handleEdgeToEdge()
    }

    private fun setupFragmentExamples(container: LinearLayout) {
        // Title
        container.addView(createTitleText("Fragment Examples"))

        // Description
        container.addView(
            createDescriptionText(
                "This fragment demonstrates edge-to-edge handling within Fragment architecture " +
                    "using the Edge to Easy library.",
            ),
        )

        // Section 1: Fragment with Auto Status Bar
        container.addView(createSectionTitle("1. Fragment Auto Status Bar"))

        val autoStatusBarView = createColoredSection("Auto Status Bar Fragment", Color.rgb(100, 149, 237))
        container.addView(autoStatusBarView)

        container.addView(
            Button(requireContext()).apply {
                text = "Enable Auto Status Bar"
                setOnClickListener {
                    autoStatusBarView.enableAutoStatusBarAppearance()
                }
            },
        )

        // Section 2: RecyclerView with Edge-to-Edge
        container.addView(createSectionTitle("2. RecyclerView Edge-to-Edge"))

        val recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SampleAdapter()
            setBackgroundColor(Color.WHITE)
        }

        // Add space for last item to avoid navigation bar overlap
        recyclerView.addSpaceForLastItem(FillVerticalDirection.Bottom)

        val recyclerViewContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            addView(
                recyclerView,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    400,
                ),
            )
        }

        container.addView(recyclerViewContainer)

        // Section 3: Fragment Insets Monitoring
        container.addView(createSectionTitle("3. Fragment Insets Monitoring"))

        val insetsStatusText = TextView(requireContext()).apply {
            text = "Monitoring fragment insets..."
            setPadding(20, 20, 20, 20)
            setBackgroundColor(Color.rgb(255, 248, 220))
        }
        container.addView(insetsStatusText)

        // Monitor insets in fragment using StateFlow
        view?.insetsStateFlow(SystemArea.Everything)
            ?.onEach { insets ->
                insetsStatusText.text = "Fragment Insets: L=${insets.left}, T=${insets.top}, R=${insets.right}, B=${insets.bottom}"
            }
            ?.launchIn(viewLifecycleOwner.lifecycleScope)

        // Section 4: System Area Spacers in Fragment
        container.addView(createSectionTitle("4. Fragment System Spacers"))

        container.addView(
            TextView(requireContext()).apply {
                text = "Before Navigation Bar Spacer"
                setPadding(20, 10, 20, 10)
            },
        )

        // Add navigation bar spacer
        container.addSystemAreaSpacer(SystemArea.NavigationBar)

        container.addView(
            TextView(requireContext()).apply {
                text = "After Navigation Bar Spacer"
                setPadding(20, 10, 20, 10)
                setBackgroundColor(Color.rgb(240, 255, 240))
            },
        )

        // Section 5: Multiple Fragments Simulation
        container.addView(createSectionTitle("5. Fragment Chain Simulation"))

        val headerFragment = createColoredSection("Header Fragment Area", Color.rgb(255, 182, 193))
        val bodyFragment = createColoredSection("Body Fragment Area", Color.rgb(173, 216, 230))
        val footerFragment = createColoredSection("Footer Fragment Area", Color.rgb(144, 238, 144))

        headerFragment.awayFrom(SystemArea.StatusBar)
            .fillSpace(FillDirection.Vertical)
            .then(footerFragment).awayFrom(SystemArea.NavigationBar)
            .fillSpace(FillDirection.Vertical)
            .handleEdgeToEdge()

        container.addView(headerFragment)
        container.addView(bodyFragment)
        container.addView(footerFragment)

        // Add bottom padding
        container.addView(
            TextView(requireContext()).apply {
                text = ""
                setPadding(0, 50, 0, 50)
            },
        )
    }

    private fun createTitleText(text: String): TextView = TextView(requireContext()).apply {
        this.text = text
        textSize = 20f
        setTextColor(Color.BLACK)
        setPadding(20, 30, 20, 20)
        setTypeface(typeface, android.graphics.Typeface.BOLD)
    }

    private fun createSectionTitle(text: String): TextView = TextView(requireContext()).apply {
        this.text = text
        textSize = 16f
        setTextColor(Color.DKGRAY)
        setPadding(20, 20, 20, 10)
        setTypeface(typeface, android.graphics.Typeface.BOLD)
    }

    private fun createDescriptionText(text: String): TextView = TextView(requireContext()).apply {
        this.text = text
        textSize = 14f
        setTextColor(Color.GRAY)
        setPadding(20, 10, 20, 20)
    }

    private fun createColoredSection(text: String, color: Int): TextView = TextView(requireContext()).apply {
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

/**
 * Simple RecyclerView adapter for demonstrating edge-to-edge with lists
 */
class SampleAdapter : RecyclerView.Adapter<SampleAdapter.ViewHolder>() {
    private val items = (1..20).map { "RecyclerView Item $it" }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = TextView(parent.context).apply {
            text = ""
            textSize = 16f
            setPadding(20, 20, 20, 20)
        }
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position]
        holder.textView.setBackgroundColor(
            if (position % 2 == 0) Color.rgb(245, 245, 245) else Color.WHITE,
        )
    }

    override fun getItemCount() = items.size
}
