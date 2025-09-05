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
package io.github.parkjonghun.edgetoeasy.core.extension

import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import io.github.parkjonghun.edgetoeasy.core.model.FillVerticalDirection

/**
 * Adds space for the last item in the RecyclerView to avoid overlap with system UI elements
 * like the navigation bar or status bar.
 *
 * This function adds an ItemDecoration to the RecyclerView that adjusts the bottom offset
 * of the last item based on the specified [direction].
 *
 * @param direction The direction in which to add space for the last item. Default is [FillVerticalDirection.Bottom].
 *
 * Usage example:
 * ```kotlin
 * recyclerView.addSpaceForLastItem(FillVerticalDirection.Bottom)
 * ```
 */
fun RecyclerView.addSpaceForLastItem(direction: FillVerticalDirection = FillVerticalDirection.Bottom) {
    addItemDecoration(
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State,
            ) {
                val adapter = parent.adapter ?: return
                val position = parent.getChildAdapterPosition(view)

                // Only apply offset to the last item
                if (position == adapter.itemCount - 1) {
                    val rootWindowInsets = ViewCompat.getRootWindowInsets(view)
                    when (direction) {
                        FillVerticalDirection.Top -> {
                            val statusBarInsets =
                                rootWindowInsets?.getInsets(WindowInsetsCompat.Type.statusBars())
                            outRect.top = statusBarInsets?.top ?: 0
                        }

                        FillVerticalDirection.Bottom -> {
                            val navigationInsets =
                                rootWindowInsets?.getInsets(WindowInsetsCompat.Type.navigationBars())
                            outRect.bottom = navigationInsets?.bottom ?: 0
                        }

                        FillVerticalDirection.All -> {
                            val systemBarInsets =
                                rootWindowInsets?.getInsets(WindowInsetsCompat.Type.systemBars())
                            outRect.top = systemBarInsets?.top ?: 0
                            outRect.bottom = systemBarInsets?.bottom ?: 0
                            outRect.left = systemBarInsets?.left ?: 0
                            outRect.right = systemBarInsets?.right ?: 0
                        }

                        else -> {}
                    }
                }
            }
        },
    )
}
