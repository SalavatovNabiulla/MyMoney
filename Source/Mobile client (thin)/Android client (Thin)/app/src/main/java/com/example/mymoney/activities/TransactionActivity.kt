package com.example.mymoney.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings.Global
import android.widget.Toast
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
    lateinit var current_transaction: Transaction
    lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        binding.selectTransactionType.setOnClickListener {select_transaction_type()}
        binding.selectWallet.setOnClickListener { select_wallet() }
        binding.selectItem.setOnClickListener { select_item() }
        binding.saveTransaction.setOnClickListener { save_transaction() }
        binding.deleteTransaction.setOnClickListener {
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Удаление транзакции")
            alertDialog.setMessage("Подвердите удаление транзакции!")
            alertDialog.setPositiveButton("Да"){dialog,i ->
                Toast.makeText(this,"Транзакция успешно удалена!",Toast.LENGTH_LONG).show()
                delete_transaction()
            }
            alertDialog.setNegativeButton("Нет"){dialog,i ->
                //
            }
            alertDialog.show()
        }
        update_data()
        setContentView(binding.root)
    }

    fun save_transaction(){
        GlobalScope.launch {
            sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
            var server_url = sharedPref.getString("server_ip","0.0.0.0").toString()
            current_transaction.type = Transaction_type.get_transactions_type(server_url = server_url, title = binding.transactionTypeEdit.text.toString())
            current_transaction.wallet = Wallet.get_wallet(server_url = server_url, title = binding.transactionWalletEdit.text.toString())
            current_transaction.sum = binding.transactionSumEdit.text.toString().toInt()
            current_transaction.cost_item = Cost_item(server_url=server_url)
            current_transaction.revenue_item = Revenue_item(server_url = server_url)
            if (binding.transactionItemEdit.text.toString() == "income"){
                current_transaction.revenue_item = Revenue_item.get_revenue_item(server_url=server_url, title = binding.transactionItemEdit.text.toString())
            }else if (binding.transactionItemEdit.text.toString() == "expense"){
                current_transaction.cost_item = Cost_item.get_cost_item(server_url=server_url, title = binding.transactionItemEdit.text.toString())
            }
            if (current_transaction.id != 0){
                current_transaction.update()
            }else{
                current_transaction.create()
            }
            finish()
        }
    }

    fun delete_transaction(){
        GlobalScope.launch {
            current_transaction.delete()
            runOnUiThread {
                finish()
            }
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
            options.add(i.title.toString())
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
        current_transaction = Transaction.get_transaction(server_url = sharedPref.getString("server_ip","0.0.0.0").toString(),id=intent.getIntExtra("EXTRA_ID",0))
    }

    fun update_data(){
        GlobalScope.launch{
            get_transaction_types()
            get_wallets()
            get_revenue_items()
            get_cost_items()
            get_current_transaction()
            runOnUiThread{
                if(current_transaction.id != 0){
                    binding.transactionIdEdit.setText(current_transaction.id.toString())
                    binding.transactionTypeEdit.setText(current_transaction.type.title.toString())
                    binding.transactionWalletEdit.setText(current_transaction.wallet.title.toString())
                    binding.transactionSumEdit.setText(current_transaction.sum.toString())
                }
            }
        }
    }


}