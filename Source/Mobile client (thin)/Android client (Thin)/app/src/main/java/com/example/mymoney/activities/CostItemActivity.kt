package com.example.mymoney.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymoney.databinding.ActivityCostItemBinding
import com.example.mymoney.databinding.ActivityWalletBinding
import com.example.mymoney.databinding.ActivityWalletsTypeBinding
import com.example.mymoney.models.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CostItemActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCostItemBinding
    lateinit var current_cost_item: Cost_item
    lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCostItemBinding.inflate(layoutInflater)
        binding.saveCostItem.setOnClickListener {
            if (forms_filled()){
                save_cost_item()
            }else{
                Toast.makeText(this,"Необходимо заполнить данные!",Toast.LENGTH_LONG).show()
            }

        }
        binding.deleteCostItem.setOnClickListener {
            if(forms_filled()){
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Удаление статьи расходов")
                alertDialog.setMessage("Подвердите удаление статьи расходов!")
                alertDialog.setPositiveButton("Да"){dialog,i ->
                    Toast.makeText(this,"Статья расходов успешно удалена!",Toast.LENGTH_LONG).show()
                    delete_cost_item()
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
        if (binding.costItemTitleEdit.text.isBlank()){valid = false}
        return valid
    }

    fun save_cost_item(){
        GlobalScope.launch {
            current_cost_item.title = binding.costItemTitleEdit.text.toString()
            if (current_cost_item.id != 0){
                current_cost_item.update()
            }else{
                current_cost_item.create()
            }
            runOnUiThread{
                finish()
            }
        }
    }

    fun delete_cost_item(){
        GlobalScope.launch {
            current_cost_item.delete()
            runOnUiThread {
                finish()
            }
        }
    }

    fun get_current_cost_item(){
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        current_cost_item = Cost_item.get_cost_item(server_url = sharedPref.getString("server_ip","0.0.0.0").toString(),id=intent.getIntExtra("EXTRA_ID",0))
    }

    fun set_availability(){
        binding.deleteCostItem.isEnabled = current_cost_item.id != 0
    }

    fun update_data(){
        GlobalScope.launch{
            get_current_cost_item()
            runOnUiThread{
                if(current_cost_item.id != 0){
                    binding.costItemIdEdit.setText(current_cost_item.id.toString())
                    binding.costItemTitleEdit.setText(current_cost_item.title)
                }
                set_availability()
            }
        }
    }

}