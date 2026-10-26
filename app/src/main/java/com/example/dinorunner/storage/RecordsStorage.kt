package com.example.dinorunner.storage

import android.content.Context

object RecordsStorage {
    private const val PREFS_NAME = "dino_prefs"
    private const val KEY_BEST = "best_score"

    fun getBest(ctx: Context): Int {
        return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(KEY_BEST, 0)
    }

    fun updateBest(ctx: Context, score: Int) {
        if (score > getBest(ctx)) {
            ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putInt(KEY_BEST, score).apply()
        }
    }
}