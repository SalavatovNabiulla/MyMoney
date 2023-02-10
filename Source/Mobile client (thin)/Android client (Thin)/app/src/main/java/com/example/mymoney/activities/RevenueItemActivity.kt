package com.example.mymoney.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymoney.databinding.ActivityCostItemBinding
import com.example.mymoney.databinding.ActivityRevenueItemBinding
import com.example.mymoney.databinding.ActivityWalletBinding
import com.example.mymoney.databinding.ActivityWalletsTypeBinding
import com.example.mymoney.models.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RevenueItemActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRevenueItemBinding
    lateinit var current_revenue_item: Revenue_item
    lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRevenueItemBinding.inflate(layoutInflater)
        binding.saveRevenueItem.setOnClickListener {
            if (forms_filled()){
                save_revenue_item()
            }else{
                Toast.makeText(this,"Необходимо заполнить данные!",Toast.LENGTH_LONG).show()
            }

        }
        binding.deleteRevenueItem.setOnClickListener {
            if(forms_filled()){
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Удаление статьи доходов")
                alertDialog.setMessage("Подвердите удаление статьи доходов!")
                alertDialog.setPositiveButton("Да"){dialog,i ->
                    Toast.makeText(this,"Статья доходов успешно удалена!",Toast.LENGTH_LONG).show()
                    delete_revenue_item()
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
        if (binding.revenueItemTitleEdit.text.isBlank()){valid = false}
        return valid
    }

    fun save_revenue_item(){
        GlobalScope.launch {
            current_revenue_item.title = binding.revenueItemTitleEdit.text.toString()
            if (current_revenue_item.id != 0){
                current_revenue_item.update()
            }else{
                current_revenue_item.create()
            }
            runOnUiThread{
                finish()
            }
        }
    }

    fun delete_revenue_item(){
        GlobalScope.launch {
            current_revenue_item.delete()
            runOnUiThread {
                finish()
            }
        }
    }

    fun get_current_cost_item(){
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        current_revenue_item = Revenue_item.get_revenue_item(server_url = sharedPref.getString("server_ip","0.0.0.0").toString(),id=intent.getIntExtra("EXTRA_ID",0))
    }

    fun set_availability(){
        binding.deleteRevenueItem.isEnabled = current_revenue_item.id != 0
    }

    fun update_data(){
        GlobalScope.launch{
            get_current_cost_item()
            runOnUiThread{
                if(current_revenue_item.id != 0){
                    binding.revenueItemIdEdit.setText(current_revenue_item.id.toString())
                    binding.revenueItemTitleEdit.setText(current_revenue_item.title)
                }
                set_availability()
            }
        }
    }

}