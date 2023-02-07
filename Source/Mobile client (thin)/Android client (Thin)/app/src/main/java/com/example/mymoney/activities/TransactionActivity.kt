package com.example.mymoney.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mymoney.api.RetrofitInstance
import com.example.mymoney.databinding.ActivityTransactionBinding
import com.example.mymoney.models.Cost_item
import com.example.mymoney.models.Revenue_item
import com.example.mymoney.models.Transaction_type
import com.example.mymoney.models.Wallet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTransactionBinding
    var transaction_types = ArrayList<Transaction_type>()
    var wallets = ArrayList<Wallet>()
    var revenue_items = ArrayList<Revenue_item>()
    var cost_items = ArrayList<Cost_item>()
    var retrofitInstance = RetrofitInstance("0.0.0.0")
    lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        binding.selectTransactionType.setOnClickListener {select_transaction_type()}
        binding.selectWallet.setOnClickListener { select_wallet() }
        binding.selectItem.setOnClickListener { select_item() }
        update_data()
        setContentView(binding.root)
    }
    fun select_item(){
        var options = arrayListOf<String>()
        var current_item = binding.transactionItemEdit.text.toString()
        if(current_item == "income"){
            for(i in revenue_items){
                options.add(i.title)
            }
        }else{
            for(i in cost_items){
                options.add(i.title)
            }
        }
        var type_dialog = AlertDialog.Builder(this)
        var index = 0
        type_dialog.setTitle("Выберите тип транзакции")
        type_dialog.setSingleChoiceItems(options.toTypedArray(),0,) {dialogInterface, i ->
            index = i
        }
        type_dialog.setPositiveButton("Подтвердить"){_,_ ->
            binding.transactionItemEdit.setText(options[index])
        }
        type_dialog.setNegativeButton("Отмена"){_,_ ->
            //
        }
        type_dialog.create()
        type_dialog.show()
    }
    fun select_wallet(){
        var options = arrayListOf<String>()
        for(i in wallets){
            options.add(i.title.toString() +" - "+ i.balance.toString())
        }
        var type_dialog = AlertDialog.Builder(this)
        var index = 0
        type_dialog.setTitle("Выберите тип транзакции")
        type_dialog.setSingleChoiceItems(options.toTypedArray(),0,) {dialogInterface, i ->
            index = i
        }
        type_dialog.setPositiveButton("Подтвердить"){_,_ ->
            binding.transactionWalletEdit.setText(options[index])
        }
        type_dialog.setNegativeButton("Отмена"){_,_ ->
            //
        }
        type_dialog.create()
        type_dialog.show()
    }
    fun select_transaction_type(){
        var options = arrayListOf<String>()
        for(i in transaction_types){
            options.add(i.title)
        }
        var type_dialog = AlertDialog.Builder(this)
        var index = 0
        type_dialog.setTitle("Выберите тип транзакции")
        type_dialog.setSingleChoiceItems(options.toTypedArray(),0,) {dialogInterface, i ->
            index = i
        }
        type_dialog.setPositiveButton("Подтвердить"){_,_ ->
            binding.transactionTypeEdit.setText(options[index])
        }
        type_dialog.setNegativeButton("Отмена"){_,_ ->
            //
        }
        type_dialog.create()
        type_dialog.show()
    }
    fun get_transaction_types(){
        transaction_types.clear()
        lifecycleScope.launchWhenCreated {
            try {
                var response = retrofitInstance.api.getTransactionsTypes()
                for(i in response.body()!!){
                    transaction_types.add(i)
                }
            }catch (e: Exception){
                //
            }
        }
    }
    fun get_wallets(){
        wallets.clear()
//        lifecycleScope.launchWhenCreated {
//            try {
//                var response = retrofitInstance.api.getWallets()
//                for(i in response.body()!!){
//                    wallets.add(i)
//                }
//            }catch (e: Exception){
//                //
//            }
//        }
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        GlobalScope.launch {
            wallets = Wallet.get_wallets(sharedPref.getString("server_ip","0.0.0.0").toString())
        }
    }
    fun get_revenue_items(){
        revenue_items.clear()
        lifecycleScope.launchWhenCreated {
            try {
                var response = retrofitInstance.api.getRevenueItems()
                for(i in response.body()!!){
                    revenue_items.add(i)
                }
            }catch (e: Exception){
                //
            }
        }
    }
    fun get_cost_items(){
        cost_items.clear()
        lifecycleScope.launchWhenCreated {
            try {
                var response = retrofitInstance.api.getCostItems()
                for(i in response.body()!!){
                    cost_items.add(i)
                }
            }catch (e: Exception){
                //
            }
        }
    }
    fun update_data(){
        update_ip()
        get_transaction_types()
        get_wallets()
        get_revenue_items()
        get_cost_items()

    }
    fun update_ip(){
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        retrofitInstance = RetrofitInstance(sharedPref.getString("server_ip","0.0.0.0").toString())
    }

}