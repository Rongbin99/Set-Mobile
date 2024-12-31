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

package com.rongbin99.set_apk

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
import androidx.recyclerview.widget.RecyclerView

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
//    private lateinit var mBoardTiles: CardAdapter

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
//        mBoardTiles =
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
//        mBoardTiles = CardAdapter(mActivity)
        mBoard.visibility = View.VISIBLE
    }

    private fun shuffleCards() {
        Log.d(TAG, "shuffleCards: ")
    }

    private fun restartGame() {
        Log.d(TAG, "restartGame: ")
    }

    private fun updateScore() {
        Log.d(TAG, "updateScore: ")
    }

    private fun updateHighScore() {
        Log.d(TAG, "updateHighScore: ")
    }

    private fun updateCardsLeft() {
        Log.d(TAG, "updateCardsLeft: ")
    }

    private fun updateMatchesFound() {
        Log.d(TAG, "updateMatchesFound: ")
    }

    /**
     * Returns true if the current theme is dark
     */
    private fun isDarkTheme(configuration: Configuration): Boolean {
        return configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}