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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BoardAdapter(private val tiles: MutableList<BoardTile>, private val onTileClicked: (position: Int, view: View) -> Unit) : RecyclerView.Adapter<BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_board_card_item, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(tiles[position])
        holder.itemView.setOnClickListener { v -> onTileClicked(position, v) }
    }

    override fun getItemCount(): Int = tiles.size

    fun replaceAt(indices: List<Int>, replacements: List<BoardTile>) {
        val sorted = indices.sorted()
        for ((i, idx) in sorted.withIndex()) {
            if (idx in tiles.indices) {
                val replacement = replacements.getOrNull(i)
                if (replacement != null) {
                    tiles[idx] = replacement
                    notifyItemChanged(idx)
                }
            }
        }
    }

    fun setAll(newTiles: List<BoardTile>) {
        tiles.clear()
        tiles.addAll(newTiles)
        notifyDataSetChanged()
    }
}
