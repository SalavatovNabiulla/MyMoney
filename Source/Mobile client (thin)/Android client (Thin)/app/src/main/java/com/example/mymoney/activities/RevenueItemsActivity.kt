package com.example.mymoney.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymoney.adapters.CostItemAdapter
import com.example.mymoney.adapters.RevenueItemAdapter
import com.example.mymoney.adapters.WalletsTypeAdapter
import com.example.mymoney.databinding.ActivityCostItemsBinding
import com.example.mymoney.databinding.ActivityRevenueItemBinding
import com.example.mymoney.databinding.ActivityRevenueItemsBinding
import com.example.mymoney.databinding.ActivityWalletsTypesBinding
import com.example.mymoney.models.Cost_item
import com.example.mymoney.models.Revenue_item
import com.example.mymoney.models.Wallets_type
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RevenueItemsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRevenueItemsBinding
    lateinit var sharedPref : SharedPreferences
    var revenue_items_list = ArrayList<Revenue_item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRevenueItemsBinding.inflate(layoutInflater)
        binding.createRevenueItemButton.setOnClickListener {
            Intent(this,RevenueItemActivity::class.java).also {
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
        binding.revenueItems.adapter = RevenueItemAdapter(revenue_items_list).also {
            it.server_ip = sharedPref.getString("server_ip","0.0.0.0").toString()
            it.context = this
        }
        binding.revenueItems.layoutManager = LinearLayoutManager(this@RevenueItemsActivity)
    }

    fun update_data(){
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        GlobalScope.launch {
            revenue_items_list = Revenue_item.Companion.get_revenue_items(server_url = sharedPref.getString("server_ip","0.0.0.0").toString())
            runOnUiThread {
                update_list()
            }
        }

    }
}