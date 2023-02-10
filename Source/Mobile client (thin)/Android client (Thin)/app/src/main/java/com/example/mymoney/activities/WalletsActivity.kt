package com.example.mymoney.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymoney.adapters.WalletAdapter
import com.example.mymoney.databinding.ActivityWalletsBinding
import com.example.mymoney.models.Wallet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WalletsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWalletsBinding
    lateinit var sharedPref : SharedPreferences
    var wallets_list = ArrayList<Wallet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletsBinding.inflate(layoutInflater)
        binding.createWalletButton.setOnClickListener {
            Intent(this,WalletActivity::class.java).also {
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
        binding.wallets.adapter = WalletAdapter(wallets_list).also {
            it.server_ip = sharedPref.getString("server_ip","0.0.0.0").toString()
            it.context = this
        }
        binding.wallets.layoutManager = LinearLayoutManager(this@WalletsActivity)
    }

    fun update_data(){
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        GlobalScope.launch {
            wallets_list = Wallet.Companion.get_wallets(server_url = sharedPref.getString("server_ip","0.0.0.0").toString())
            runOnUiThread {
                update_list()
            }
        }

    }
}