package com.example.dinorunner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dinorunner.databinding.ActivityStartBinding
import com.example.dinorunner.storage.RecordsStorage
import com.example.dinorunner.ui.GameActivity

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPlay.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        val best = RecordsStorage.getBest(this)
        binding.tvRecord.text = "Рекорд: $best"
    }
}