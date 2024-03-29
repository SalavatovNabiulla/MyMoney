package com.example.mymoney.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoney.R
import com.example.mymoney.activities.TransactionActivity
import com.example.mymoney.models.Transaction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TransactionAdapter(
    private val dataSet: List<Transaction>
    ) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    var server_ip = "0.0.0.0"


    lateinit var context: Context

    class ViewHolder(view: View,server_ip : String = "") : RecyclerView.ViewHolder(view) {
        val id_text: TextView
        val type_text : TextView
        val wallet_text : TextView
        val sum_text : TextView
        val cost_revenue_item_text : TextView
        var server_ip = server_ip
        var last_tap : Long = 0
        init {
            id_text = view.findViewById(R.id.id_text)
            type_text = view.findViewById(R.id.type_text)
            wallet_text = view.findViewById(R.id.wallet_text)
            sum_text = view.findViewById(R.id.sum_text)
            cost_revenue_item_text = view.findViewById(R.id.cost_revenue_item_text)
        }
    }

    fun onItemClick(view : ViewHolder){

        var current_tap = System.currentTimeMillis()
        var duration = (current_tap - view.last_tap)
        view.last_tap = current_tap
        if(duration < 500){double_click_listener(view)}else{one_click_listener(view)}

    }

    fun one_click_listener(view: ViewHolder){
        //
    }

    fun double_click_listener(view: ViewHolder){
        var id = view.id_text.text.toString()
        GlobalScope.launch {
            var Intent = Intent(context,TransactionActivity::class.java)
            Intent.putExtra("EXTRA_ID",id.toInt())
            context.startActivity(Intent)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_transaction, viewGroup, false)

        var new_viewholder = ViewHolder(view,server_ip)

        view.setOnClickListener {onItemClick(new_viewholder)}

        return new_viewholder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var element = dataSet[position]
        viewHolder.id_text.text = element.id.toString()
        viewHolder.type_text.text = element.type.title.toString()
        viewHolder.wallet_text.text = element.wallet.title.toString()
        viewHolder.sum_text.text = element.sum.toString()
        if(element.type.title == "income"){
            if(element.revenue_item.id != 0){
                viewHolder.cost_revenue_item_text.text = element.revenue_item.title.toString()
            }
        }else{
            if(element.cost_item.id != 0){
                viewHolder.cost_revenue_item_text.text = element.cost_item.title.toString()
            }
        }
    }

    override fun getItemCount() = dataSet.size

}
