package com.example.mymoney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.example.mymoney.databinding.ActivityMainBinding
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    var transactions_list = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        lifecycleScope.launchWhenCreated {
            try {
                var response = RetrofitInstance.api.getTransactions()
                transactions_list.clear()
                for(i in response.body()!!){
                    transactions_list.add(i.sum.toString())
                }
            }catch (e: Exception){
                //
            }

        }
        binding.update.setOnClickListener {update()}
        setContentView(binding.root)
    }
    fun update(){
        binding.transactions.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,transactions_list)
    }
}