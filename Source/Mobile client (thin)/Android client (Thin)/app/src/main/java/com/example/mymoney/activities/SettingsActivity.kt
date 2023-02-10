package com.example.mymoney.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymoney.databinding.ActivitySettingsBinding
import com.example.mymoney.models.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {
    lateinit var binding : ActivitySettingsBinding
    lateinit var sharedPref : SharedPreferences
    var deleting_in_process = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        var sharedPref = getSharedPreferences("settings",Context.MODE_PRIVATE)
        var sharedPrefEditor = sharedPref.edit()
        //Listeners
        binding.saveButton.setOnClickListener {

            var server_ip = binding.serverIpEdit.text.toString()
            sharedPrefEditor.apply{
                putString("server_ip",server_ip)
                apply()
            }
        }
        binding.clearBaseButton.setOnClickListener { full_clear() }

        //
        load_settings()
        //
        setContentView(binding.root)
    }

    fun confirm_delete(){
        GlobalScope.launch {
            deleting_in_process = true
            deleting_animation()
            sharedPref = getSharedPreferences("settings",Context.MODE_PRIVATE)
            var transactions = Transaction.get_transactions(server_url = sharedPref.getString("server_ip",null).toString())
            for (i in transactions){
                i.delete()
            }
            var wallets = Wallet.get_wallets(server_url = sharedPref.getString("server_ip",null).toString())
            for (i in wallets){
                i.delete()
            }
            var wallets_types = Wallets_type.get_wallets_types(server_url = sharedPref.getString("server_ip",null).toString())
            for (i in wallets_types){
                i.delete()
            }
            var cost_items = Cost_item.get_cost_items(server_url = sharedPref.getString("server_ip",null).toString())
            for (i in cost_items){
                i.delete()
            }
            var revenue_items = Revenue_item.get_revenue_items(server_url = sharedPref.getString("server_ip",null).toString())
            for (i in revenue_items){
                i.delete()
            }
            deleting_in_process = false
            runOnUiThread{
                Toast.makeText(this@SettingsActivity,"Все данные успешно удалены",Toast.LENGTH_LONG).show()

            }
        }
    }

    fun deleting_animation(){
        GlobalScope.launch {
            var flag = 0
            while (deleting_in_process){
                if (flag == 0){
                    flag += 1
                    runOnUiThread{
                        binding.clearBaseButton.setText(".")
                    }
                }else if(flag == 1){
                    flag += 1
                    runOnUiThread{
                        binding.clearBaseButton.setText("..")
                    }
                }else if(flag == 2){
                    flag = 0
                    runOnUiThread{
                        binding.clearBaseButton.setText("...")
                    }
                }
                delay(250)
            }
            runOnUiThread{
                binding.clearBaseButton.setText("ОЧИСТИТЬ БАЗУ")
            }
        }
    }

    fun full_clear(){
        var alertDialog = AlertDialog.Builder(this@SettingsActivity)
        alertDialog.setTitle("Очистка базы")
        alertDialog.setMessage("Подтвердите удаление всех данных из базы!")
        alertDialog.setPositiveButton("Да"){dialog,i ->
            confirm_delete()
        }
        alertDialog.setNegativeButton("Нет"){dialog,i ->
            //
        }
        alertDialog.show()

    }

    fun load_settings(){
        var sharedPref = getSharedPreferences("settings",Context.MODE_PRIVATE)
        var sharedPrefEditor = sharedPref.edit()
        var server_ip = sharedPref.getString("server_ip",null)
        binding.serverIpEdit.setText(server_ip)
    }

}