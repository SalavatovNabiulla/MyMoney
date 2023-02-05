package com.example.mymoney.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymoney.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding : ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        var sharedPref = getSharedPreferences("settings",Context.MODE_PRIVATE)
        var sharedPrefEditor = sharedPref.edit()
        //Listeners
        binding.saveButton.setOnClickListener {

            var server_ip = binding.serverIpEdit.text.toString()
            sharedPrefEditor.apply{
                putString("server_ip",server_ip)
                apply()
            }
        }
        //
        load_settings()
        //
        setContentView(binding.root)
    }
    fun load_settings(){
        var sharedPref = getSharedPreferences("settings",Context.MODE_PRIVATE)
        var sharedPrefEditor = sharedPref.edit()
        var server_ip = sharedPref.getString("server_ip",null)
        binding.serverIpEdit.setText(server_ip)
    }

}