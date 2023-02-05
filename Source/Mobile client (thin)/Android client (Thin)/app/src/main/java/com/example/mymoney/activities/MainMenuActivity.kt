package com.example.mymoney.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymoney.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        binding.settingsButton.setOnClickListener {
            Intent(this, SettingsActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.transactionsButton.setOnClickListener {
            Intent(this,TransactionsActivity::class.java).also {
                startActivity(it)
            }
        }
        setContentView(binding.root)
    }
}