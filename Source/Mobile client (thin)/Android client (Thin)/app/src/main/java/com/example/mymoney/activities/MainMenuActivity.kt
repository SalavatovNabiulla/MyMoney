package com.example.mymoney.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymoney.databinding.ActivityMainMenuBinding
import com.example.mymoney.models.Wallet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainMenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainMenuBinding
    lateinit var sharedPref : SharedPreferences
    lateinit var wallets : ArrayList<Wallet>
    var last_tap : Long = 0
    var current_tap : Long = 0

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
        binding.walletsButton.setOnClickListener {
            Intent(this,WalletsActivity::class.java).also {
                startActivity(it)
            }
        }
        //
        setContentView(binding.root)
    }
    fun double_click_listener(view: View){
        Toast.makeText(this,"Двойное нажатие!",Toast.LENGTH_SHORT).show()
    }
}