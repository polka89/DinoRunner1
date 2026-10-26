package com.example.dinorunner.ui

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.dinorunner.databinding.ActivityGameBinding
import com.example.dinorunner.storage.RecordsStorage

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gameView.setOnTouchListener { v, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    val half = v.width / 2f
                    if (e.x < half) binding.gameView.dinoJump()
                    else binding.gameView.dinoDuck(true)
                    v.performClick()
                }
                MotionEvent.ACTION_UP -> {
                    binding.gameView.dinoDuck(false)
                    v.performClick()
                }
            }
            true
        }

        binding.gameView.onGameOverListener = { score ->
            runOnUiThread {
                val best = RecordsStorage.getBest(this)
                RecordsStorage.updateBest(this, score)
                showGameOverDialog(score, maxOf(score, best))
            }
        }
    }

    fun showGameOverDialog(score: Int, best: Int) {
        if (supportFragmentManager.findFragmentByTag("gameover") == null) {
            GameOverDialogFragment.newInstance(score, best)
                .show(supportFragmentManager, "gameover")
        }
    }

    fun restartGame() {
        binding.gameView.restart()
    }
}