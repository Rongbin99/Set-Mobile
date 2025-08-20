/*
##################################################
Copyright (c) 2024, 2025 - Rongbin Gu
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

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rongbin99.set_mobile.R

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var mActivity: AppCompatActivity
    private lateinit var mContext: android.content.Context

    private lateinit var mThemeSwitch: ImageSwitcher
    private lateinit var mRestart: Button
    private lateinit var mShuffle: Button
    private lateinit var mCurrentScore: TextView
    private lateinit var mHighScore: TextView
    private lateinit var mCardsLeft: TextView
    private lateinit var mMatchesFound: TextView
    private lateinit var mBoard: RecyclerView
    private lateinit var mBoardTiles: View
    private lateinit var mGameManager: GameManager
    private lateinit var mAdapter: BoardAdapter
    private val mSelectedPositions = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mActivity = this
        mContext = applicationContext
        Log.i(TAG, "onCreate: $mActivity")

        mRestart = findViewById(R.id.restart_button)
        mShuffle = findViewById(R.id.shuffle_button)
        mCurrentScore = findViewById(R.id.current_score)
        mHighScore = findViewById(R.id.high_score)
        mCardsLeft = findViewById(R.id.cards_left)
        mMatchesFound = findViewById(R.id.matches_found)
        mBoard = findViewById(R.id.content_recycler_view)
//        mBoardTiles = findViewById(R.layout.game_board_card_item)
        mThemeSwitch = findViewById(R.id.theme_switcher)

        mThemeSwitch.setFactory {
            val imageView = ImageView(mContext)
            val currentThemeDark = isDarkTheme(resources.configuration)
            if (currentThemeDark) {
                Log.i(TAG, "mThemeSwitch: Dark theme")
                mThemeSwitch.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_dark_mode))
            } else {
                Log.i(TAG, "mThemeSwitch: Light theme")
                mThemeSwitch.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_light_mode))
            }
            imageView
        }

        mThemeSwitch.setOnClickListener {
            val currentThemeDark = isDarkTheme(resources.configuration)
            if (currentThemeDark) {
                Log.i(TAG, "mThemeSwitch: Switching to light theme")
//                setTheme(R.style.Theme_SetApk_Light)
                mThemeSwitch.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_light_mode))
            } else {
                Log.i(TAG, "mThemeSwitch: Switching to dark theme")
//                setTheme(R.style.Theme_SetApk_Dark)
                mThemeSwitch.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_dark_mode))
            }
        }

        if (!mBoard.isVisible) {
            setBoard()
            setHighScore()
            setCardsLeft()
        }

        mRestart.setOnClickListener {
            Log.d(TAG, "onCreate: Restart button clicked")
            restartGame()
        }
        mShuffle.setOnClickListener {
            Log.d(TAG, "onCreate: Shuffle button clicked")
            shuffleCards()
        }
    }

    /**
     * Initializes the game board and inflates grid recycler view
     */
    private fun setBoard() {
        Log.d(TAG, "setBoard: ")
        // Configure layout manager
        val spanCount = resources.getInteger(R.integer.span_count)
        mBoard.layoutManager = GridLayoutManager(this, spanCount)

        // TODO: 
        // Build a fake deck of 81 items by repeating the 27 base combos 3x for now
        val deck = generatePlaceholderTiles(27).toMutableList()
        deck.addAll(generatePlaceholderTiles(27))
        deck.addAll(generatePlaceholderTiles(27))

        mGameManager = GameManager(deck)

        // Deal 12 cards
        val initial = mGameManager.dealInitial(12)

        // Set adapter
        mAdapter = BoardAdapter(initial, onTileClicked = { position, _ -> onTileClicked(position) })
        mBoard.adapter = mAdapter
        mBoard.visibility = View.VISIBLE
    }

    private fun onTileClicked(position: Int) {
        if (position !in mSelectedPositions) {
            mSelectedPositions += position
        } else {
            mSelectedPositions.remove(position)
        }

        if (mSelectedPositions.size == 3) {
            // TODO: Implement real SET validation. For now, treat any 3 as a set.
            val replacements = mGameManager.draw(3)
            mAdapter.replaceAt(mSelectedPositions.toList(), replacements)
            mSelectedPositions.clear()
            updateScore()
            updateCardsLeft()
            updateMatchesFound()
        }
    }

    private fun shuffleCards() {
        Log.d(TAG, "shuffleCards: ")
        // TODO: Can we leverage the existing mGameManager to avoid rebuilding the deck?
        // Rebuild deck with the remaining cards + currently visible cards, then deal 12 again
        val currentTiles = (0 until (mAdapter.itemCount)).map { idx ->
            // A bit hacky: access via reflection avoided; store tiles externally if needed.
            // For now we just regenerate a fresh deck to simulate shuffle.
            idx
        }
        // Fresh deck and deal 12
        val newDeck = buildNewDeck()
        mGameManager = GameManager(newDeck)
        val dealt = mGameManager.dealInitial(12)
        mAdapter.setAll(dealt)
        mSelectedPositions.clear()
        setCardsLeft()
    }

    private fun restartGame() {
        Log.d(TAG, "restartGame: ")
        // New deck, reset scores and UI counters
        val newDeck = buildNewDeck()
        mGameManager = GameManager(newDeck)
        val dealt = mGameManager.dealInitial(12)
        mAdapter.setAll(dealt)
        mSelectedPositions.clear()
        setScore(0)
        setMatchesFound()
        setCardsLeft()
    }

    private fun updateScore() {
        val currentScore = getScore()
        setScore(currentScore + 1)
        Log.d(TAG, "updateScore: $currentScore")
    }

    private fun setScore(score: Int) {
        Log.d(TAG, "setScore: $score")
        mCurrentScore.text = score.toString()
    }

    private fun getScore(): Int {
        Log.d(TAG, "getScore: ")
        return mCurrentScore.text.toString().toInt()
    }

    private fun updateHighScore() {
        Log.d(TAG, "updateHighScore: ")
    }

    private fun setHighScore() {
        Log.d(TAG, "setHighScore: ")
    }

    private fun getHighScore(): Int {
        Log.d(TAG, "getHighScore: ")
        return mHighScore.text.toString().toInt()
    }

    private fun updateCardsLeft() {
        Log.d(TAG, "updateCardsLeft: ")
    }

    private fun setCardsLeft() {
        mCardsLeft.text = mGameManager.remaining().toString()
        Log.d(TAG, "setCardsLeft: ${mCardsLeft.text}")
    }

    private fun getCardsLeft(): Int {
        Log.d(TAG, "getCardsLeft: ")
        return mCardsLeft.text.toString().toInt()
    }

    private fun updateMatchesFound() {
        Log.d(TAG, "updateMatchesFound: ")
    }

    private fun setMatchesFound(found: Int = 0) {
        Log.d(TAG, "setMatchesFound: $found")
        mMatchesFound.text = found.toString()
    }

    private fun getMatchesFound(): Int {
        Log.d(TAG, "getMatchesFound: ")
        return mMatchesFound.text.toString().toInt()
    }

    /**
     * Returns true if the current theme is dark
     */
    private fun isDarkTheme(configuration: Configuration): Boolean {
        return configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}

private fun buildNewDeck(): MutableList<BoardTile> {
    val base = generatePlaceholderTiles(27)
    val deck = mutableListOf<BoardTile>()
    repeat(3) { deck.addAll(base) }
    deck.shuffle()
    return deck
}

private fun generatePlaceholderTiles(count: Int): List<BoardTile> {
    val candidates = listOf(
        listOf(
            R.drawable.card_green_diamond_empty,
            R.drawable.card_green_diamond_filled,
            R.drawable.card_green_diamond_shaded
        ),
        listOf(
            R.drawable.card_green_oval_empty,
            R.drawable.card_green_oval_filled,
            R.drawable.card_green_oval_shaded
        ),
        listOf(
            R.drawable.card_green_squiggle_empty,
            R.drawable.card_green_squiggle_filled,
            R.drawable.card_green_squiggle_shaded
        ),
        listOf(
            R.drawable.card_purple_diamond_empty,
            R.drawable.card_purple_diamond_filled,
            R.drawable.card_purple_diamond_shaded
        ),
        listOf(
            R.drawable.card_purple_oval_empty,
            R.drawable.card_purple_oval_filled,
            R.drawable.card_purple_oval_shaded
        ),
        listOf(
            R.drawable.card_purple_squiggle_empty,
            R.drawable.card_purple_squiggle_filled,
            R.drawable.card_purple_squiggle_shaded
        ),
        listOf(
            R.drawable.card_red_diamond_empty,
            R.drawable.card_red_diamond_filled,
            R.drawable.card_red_diamond_shaded
        ),
        listOf(
            R.drawable.card_red_oval_empty,
            R.drawable.card_red_oval_filled,
            R.drawable.card_red_oval_shaded
        ),
        listOf(
            R.drawable.card_red_squiggle_empty,
            R.drawable.card_red_squiggle_filled,
            R.drawable.card_red_squiggle_shaded
        )
    )

    return List(count) { index ->
        val triple = candidates[index % candidates.size]
        // Cycle 1..3 images to mimic different counts
        val numImages = (index % 3) + 1
        BoardTile(triple.take(numImages))
    }
}
