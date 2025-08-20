/*
##################################################
Copyright (c) 2025 - Rongbin Gu
All rights reserved. See LICENSE file for details.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
##################################################
*/

package com.rongbin99.set_mobile

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageView1: ImageView = itemView.findViewById(R.id.card_img_1)
    private val imageView2: ImageView = itemView.findViewById(R.id.card_img_2)
    private val imageView3: ImageView = itemView.findViewById(R.id.card_img_3)

    fun bind(tile: BoardTile) {
        val targets = listOf(imageView1, imageView2, imageView3)
        targets.forEach { it.visibility = View.GONE }
        for (i in tile.imageResIds.indices) {
            val resId = tile.imageResIds[i]
            val targetView = targets.getOrNull(i) ?: continue
            targetView.setImageResource(resId)
            targetView.visibility = View.VISIBLE
        }
    }
}
