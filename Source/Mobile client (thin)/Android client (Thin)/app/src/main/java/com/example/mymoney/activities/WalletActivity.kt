package com.example.mymoney.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymoney.databinding.ActivityWalletBinding
import com.example.mymoney.models.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WalletActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWalletBinding
    var wallet_types = ArrayList<Wallets_type>()
    lateinit var current_wallet: Wallet
    lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        binding.selectWalletType.setOnClickListener {select_wallet_type()}
        binding.saveWallet.setOnClickListener {
            if (forms_filled()){
                save_wallet()
            }else{
                Toast.makeText(this,"Необходимо заполнить данные!",Toast.LENGTH_LONG).show()
            }

        }
        binding.deleteWallet.setOnClickListener {
            if(forms_filled()){
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Удаление кошелька")
                alertDialog.setMessage("Подвердите удаление кошелька!")
                alertDialog.setPositiveButton("Да"){dialog,i ->
                    Toast.makeText(this,"Кошелёк успешно удалён !",Toast.LENGTH_LONG).show()
                    delete_wallet()
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
        if (binding.walletTypeEdit.text.isBlank()){valid = false}
        if (binding.walletTitleEdit.text.isBlank()){valid = false}
        return valid
    }

    fun save_wallet(){
        GlobalScope.launch {
            sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
            var server_url = sharedPref.getString("server_ip","0.0.0.0").toString()
            current_wallet.title = binding.walletTitleEdit.text.toString()
            if (current_wallet.id != 0){
                current_wallet.update()
            }else{
                current_wallet.create()
            }
            runOnUiThread{
                finish()
            }
        }
    }

    fun delete_wallet(){
        GlobalScope.launch {
            current_wallet.delete()
            runOnUiThread {
                finish()
            }
        }
    }

    fun select_wallet_type(){
        var options = arrayListOf<String>()
        for(i in wallet_types){
            options.add(i.title.toString())
        }
        var type_dialog = AlertDialog.Builder(this)
        var index = 0
        type_dialog.setTitle("Выберите тип кошелька")
        type_dialog.setSingleChoiceItems(options.toTypedArray(),0,) {dialogInterface, i ->
            index = i
        }
        type_dialog.setPositiveButton("Подтвердить"){_,_ ->
            binding.walletTypeEdit.setText(options[index])
            current_wallet.type = wallet_types[index]
            set_availability()
        }
        type_dialog.setNegativeButton("Отмена"){_,_ ->
            //
        }
        type_dialog.create()
        type_dialog.show()
    }

    fun get_wallets_types(){
        wallet_types.clear()
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        wallet_types = Wallets_type.get_wallets_types(server_url = sharedPref.getString("server_ip","0.0.0.0").toString())
    }

    fun get_current_wallet(){
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        current_wallet = Wallet.get_wallet(server_url = sharedPref.getString("server_ip","0.0.0.0").toString(),id=intent.getIntExtra("EXTRA_ID",0))
    }

    fun set_availability(){
        binding.deleteWallet.isEnabled = current_wallet.id != 0
        binding.selectWalletType.isEnabled = wallet_types.isNotEmpty()
    }

    fun update_data(){
        GlobalScope.launch{
            get_current_wallet()
            get_wallets_types()
            runOnUiThread{
                if(current_wallet.id != 0){
                    binding.walletIdEdit.setText(current_wallet.id.toString())
                    binding.walletTitleEdit.setText(current_wallet.title.toString())
                    binding.walletTypeEdit.setText(current_wallet.type.title.toString())
                }
                set_availability()
            }
        }
    }

}