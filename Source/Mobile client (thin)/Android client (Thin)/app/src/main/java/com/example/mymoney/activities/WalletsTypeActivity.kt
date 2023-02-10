package com.example.mymoney.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymoney.databinding.ActivityWalletBinding
import com.example.mymoney.databinding.ActivityWalletsTypeBinding
import com.example.mymoney.models.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WalletsTypeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWalletsTypeBinding
    lateinit var current_wallets_type: Wallets_type
    lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletsTypeBinding.inflate(layoutInflater)
        binding.saveWalletsType.setOnClickListener {
            if (forms_filled()){
                save_wallets_type()
            }else{
                Toast.makeText(this,"Необходимо заполнить данные!",Toast.LENGTH_LONG).show()
            }

        }
        binding.deleteWalletsType.setOnClickListener {
            if(forms_filled()){
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Удаление типа кошельков")
                alertDialog.setMessage("Подвердите удаление типа кошельков!")
                alertDialog.setPositiveButton("Да"){dialog,i ->
                    Toast.makeText(this,"Тип кошельков успешно удалён !",Toast.LENGTH_LONG).show()
                    delete_wallet_type()
                }
                alertDialog.setNegativeButton("Нет"){dialog,i ->
                    //
                }
                alertDialog.show()
            }else{
                Toast.makeText(this,"Необходимо заполнить данные!",Toast.LENGTH_LONG).show()
            }
        }
        update_data()
        setContentView(binding.root)
    }

    fun forms_filled() : Boolean{
        var valid = true
        if (binding.walletsTypeTitleEdit.text.isBlank()){valid = false}
        return valid
    }

    fun save_wallets_type(){
        GlobalScope.launch {
            current_wallets_type.title = binding.walletsTypeTitleEdit.text.toString()
            if (current_wallets_type.id != 0){
                current_wallets_type.update()
            }else{
                current_wallets_type.create()
            }
            runOnUiThread{
                finish()
            }
        }
    }

    fun delete_wallet_type(){
        GlobalScope.launch {
            current_wallets_type.delete()
            runOnUiThread {
                finish()
            }
        }
    }

    fun get_current_wallets_type(){
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        current_wallets_type = Wallets_type.get_wallets_type(server_url = sharedPref.getString("server_ip","0.0.0.0").toString(),id=intent.getIntExtra("EXTRA_ID",0))
    }

    fun set_availability(){
        binding.deleteWalletsType.isEnabled = current_wallets_type.id != 0
    }

    fun update_data(){
        GlobalScope.launch{
            get_current_wallets_type()
            runOnUiThread{
                if(current_wallets_type.id != 0){
                    binding.walletsTypeIdEdit.setText(current_wallets_type.id.toString())
                    binding.walletsTypeTitleEdit.setText(current_wallets_type.title)
                }
                set_availability()
            }
        }
    }

}