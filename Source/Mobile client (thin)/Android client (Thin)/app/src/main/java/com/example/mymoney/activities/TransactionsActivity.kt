package com.example.mymoney.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymoney.adapters.TransactionAdapter
import com.example.mymoney.api.RetrofitInstance
import com.example.mymoney.databinding.ActivityTransactionsBinding
import com.example.mymoney.models.Transaction
import java.lang.Exception

class TransactionsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTransactionsBinding
    lateinit var sharedPref : SharedPreferences
    var transactions_list = ArrayList<Transaction>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        update_list()
        setContentView(binding.root)
    }


    fun update_list(){
        update_ip()
        transactions_list.clear()
        lifecycleScope.launchWhenCreated {
            try {
                var response = RetrofitInstance.api.getTransactions()
                for(i in response.body()!!){
                    transactions_list.add(i)
                }
                binding.transactions.adapter = TransactionAdapter(transactions_list)
                binding.transactions.layoutManager = LinearLayoutManager(this@TransactionsActivity)
            }catch (e: Exception){
                //
            }
        }

    }
    fun update_ip(){
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        RetrofitInstance.server_ip = sharedPref.getString("server_ip","").toString()
    }

}