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

class GameManager(private val deck: MutableList<BoardTile>) {
    fun dealInitial(count: Int): MutableList<BoardTile> {
        val dealt = mutableListOf<BoardTile>()
        repeat(count) {
            if (deck.isNotEmpty()) {
                dealt += deck.removeAt(0)
            }
        }
        return dealt
    }

    fun draw(count: Int): List<BoardTile> {
        val drawn = mutableListOf<BoardTile>()
        repeat(count) {
            if (deck.isNotEmpty()) {
                drawn += deck.removeAt(0)
            }
        }
        return drawn
    }

    fun remaining(): Int = deck.size
}
