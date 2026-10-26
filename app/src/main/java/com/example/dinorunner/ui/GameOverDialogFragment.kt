package com.example.dinorunner.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameOverDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val score = requireArguments().getInt("score")
        val best  = requireArguments().getInt("best")

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Игра окончена!")
            .setMessage("Ваш счет: $score\nРекорд: $best")
            .setCancelable(false) // Запрещаем закрывать кликом мимо окна
            .setPositiveButton("Заново") { _, _ ->
                (activity as? GameActivity)?.restartGame()
            }
            .setNegativeButton("В меню") { _, _ ->
                startActivity(Intent(requireContext(), com.example.dinorunner.StartActivity::class.java))
                activity?.finish()
            }
            .create()
    }

    // Тот самый блок, которого не хватало:
    companion object {
        fun newInstance(score: Int, best: Int): GameOverDialogFragment {
            val fragment = GameOverDialogFragment()
            val args = Bundle()
            args.putInt("score", score)
            args.putInt("best", best)
            fragment.arguments = args
            return fragment
        }
    }
}