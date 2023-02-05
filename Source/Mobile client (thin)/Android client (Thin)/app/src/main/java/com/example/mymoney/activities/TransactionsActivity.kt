package com.example.mymoney.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.example.mymoney.api.RetrofitInstance
import com.example.mymoney.databinding.ActivityTransactionsBinding
import java.lang.Exception

class TransactionsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTransactionsBinding
    var transactions_list = ArrayList<String>()
    lateinit var sharedPref : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        update_list()
        setContentView(binding.root)
    }
    fun update_list(){
        update_ip()
        lifecycleScope.launchWhenCreated {
            try {
                var response = RetrofitInstance.api.getTransactions()
                transactions_list.clear()
                for(i in response.body()!!){
                    transactions_list.add(i.id.toString())
                }
                update_list()
            }catch (e: Exception){
                //
            }
        }
        binding.transactions.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,transactions_list)
    }
    fun update_ip(){
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        RetrofitInstance.server_api = sharedPref.getString("server_ip","").toString()
    }

    override fun onResume() {
        super.onResume()
        update_list()
    }

    override fun onRestart() {
        super.onRestart()
        update_list()
    }

    override fun onPause() {
        super.onPause()
        update_list()
    }
}