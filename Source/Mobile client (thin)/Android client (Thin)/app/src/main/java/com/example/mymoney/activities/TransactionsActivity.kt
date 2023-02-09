package com.example.mymoney.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymoney.adapters.TransactionAdapter
import com.example.mymoney.databinding.ActivityTransactionsBinding
import com.example.mymoney.models.Transaction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TransactionsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTransactionsBinding
    lateinit var sharedPref : SharedPreferences
    var transactions_list = ArrayList<Transaction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        binding.createTransactionButton.setOnClickListener {
            Intent(this,TransactionActivity::class.java).also {
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
        binding.transactions.adapter = TransactionAdapter(transactions_list).also {
            it.server_ip = sharedPref.getString("server_ip","0.0.0.0").toString()
            it.context = this
        }
        binding.transactions.layoutManager = LinearLayoutManager(this@TransactionsActivity)
    }

    fun update_data(){
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        GlobalScope.launch {
            transactions_list = Transaction.Companion.get_transactions(server_url = sharedPref.getString("server_ip","0.0.0.0").toString())
            runOnUiThread {
                update_list()
            }
        }

    }
}