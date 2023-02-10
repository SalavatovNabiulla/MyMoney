package com.example.mymoney.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymoney.adapters.WalletsTypeAdapter
import com.example.mymoney.databinding.ActivityWalletsTypesBinding
import com.example.mymoney.models.Wallets_type
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WalletsTypesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWalletsTypesBinding
    lateinit var sharedPref : SharedPreferences
    var wallets_types_list = ArrayList<Wallets_type>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletsTypesBinding.inflate(layoutInflater)
        binding.createWalletsTypeButton.setOnClickListener {
            Intent(this,WalletsTypeActivity::class.java).also {
                it.putExtra("EXTRA_ID",0)
                startActivity(it)
            }
        }
        update_data()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        update_data()
    }

    fun update_list(){
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        binding.walletsTypes.adapter = WalletsTypeAdapter(wallets_types_list).also {
            it.server_ip = sharedPref.getString("server_ip","0.0.0.0").toString()
            it.context = this
        }
        binding.walletsTypes.layoutManager = LinearLayoutManager(this@WalletsTypesActivity)
    }

    fun update_data(){
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        GlobalScope.launch {
            wallets_types_list = Wallets_type.Companion.get_wallets_types(server_url = sharedPref.getString("server_ip","0.0.0.0").toString())
            runOnUiThread {
                update_list()
            }
        }

    }
}