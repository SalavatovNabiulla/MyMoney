package com.example.mymoney.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymoney.databinding.ActivityTransactionBinding
import com.example.mymoney.models.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTransactionBinding
    var transaction_types = ArrayList<Transaction_type>()
    var wallets = ArrayList<Wallet>()
    var revenue_items = ArrayList<Revenue_item>()
    var cost_items = ArrayList<Cost_item>()
    lateinit var transaction: Transaction
    lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        binding.selectTransactionType.setOnClickListener {select_transaction_type()}
        binding.selectWallet.setOnClickListener { select_wallet() }
        binding.selectItem.setOnClickListener { select_item() }
        binding.saveTransaction.setOnClickListener { save_transaction() }
        update_data()
        setContentView(binding.root)
    }

    fun save_transaction(){
        if(transaction.id != 0){
            //
        }else{
//            binding.transactionTypeEdit.setText(transaction.type_id.toString())
//            binding.transactionWalletEdit.setText(transaction.wallet_id.toString())
//            binding.transactionSumEdit.setText(transaction.sum.toString())
//            binding.transactionItemEdit.setText(transaction.type_id.toString())
        }
    }

    fun select_item(){
        var options = arrayListOf<String>()
        var current_item = binding.transactionItemEdit.text.toString()
        if(current_item == "income"){
            for(i in revenue_items){
                options.add(i.title.toString())
            }
        }else{
            for(i in cost_items){
                options.add(i.title.toString())
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
            options.add(i.title.toString())
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
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        GlobalScope.launch {
            transaction_types = Transaction_type.get_transactions_types(server_url = sharedPref.getString("server_ip","0.0.0.0").toString())
        }
    }

    fun get_wallets(){
        wallets.clear()
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        GlobalScope.launch {
            wallets = Wallet.get_wallets(server_url = sharedPref.getString("server_ip","0.0.0.0").toString())
        }
    }

    fun get_revenue_items(){
        revenue_items.clear()
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        GlobalScope.launch {
            revenue_items = Revenue_item.get_revenue_items(server_url = sharedPref.getString("server_ip","0.0.0.0").toString())
        }
    }

    fun get_cost_items(){
        cost_items.clear()
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        GlobalScope.launch {
            cost_items = Cost_item.get_cost_items(server_url = sharedPref.getString("server_ip","0.0.0.0").toString())
        }
    }

    fun get_current_transaction(){
        transaction = Transaction.get_transaction(server_url = sharedPref.getString("server_ip","0.0.0.0").toString(),id=intent.getIntExtra("EXTRA_ID",0))
    }

    fun update_data(){
        GlobalScope.launch{
            get_transaction_types()
            get_wallets()
            get_revenue_items()
            get_cost_items()
            get_current_transaction()
            runOnUiThread{
                if(transaction.id != 0){
                    binding.transactionIdEdit.setText(transaction.id.toString())
                    binding.transactionTypeEdit.setText(transaction.type.title.toString())
                    binding.transactionWalletEdit.setText(transaction.wallet.title.toString())
                    binding.transactionSumEdit.setText(transaction.sum.toString())
                }
            }
        }
    }


}