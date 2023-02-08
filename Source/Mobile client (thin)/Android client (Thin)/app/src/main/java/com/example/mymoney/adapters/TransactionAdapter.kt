package com.example.mymoney.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoney.R
import com.example.mymoney.models.Transaction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TransactionAdapter(
    private val dataSet: List<Transaction>
    ) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    var server_ip = "0.0.0.0"

    class ViewHolder(view: View,server_ip : String) : RecyclerView.ViewHolder(view) {
        val id_text: TextView
        val type_text : TextView
        val wallet_text : TextView
        val sum_text : TextView
        val cost_revenue_item_text : TextView
        var server_ip = server_ip
        init {
            id_text = view.findViewById(R.id.id_text)
            type_text = view.findViewById(R.id.type_text)
            wallet_text = view.findViewById(R.id.wallet_text)
            sum_text = view.findViewById(R.id.sum_text)
            cost_revenue_item_text = view.findViewById(R.id.cost_revenue_item_text)
        }
    }

    fun onItemClick(view : ViewHolder){
        var id = view.id_text.text.toString()
        GlobalScope.launch {
            var transaction = Transaction.get_transaction(view.server_ip,id.toInt())
//            Toast.makeText(view.itemView.context,transaction.sum.toString(),Toast.LENGTH_SHORT).show()
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
        viewHolder.type_text.text = element.type_id.toString()
        viewHolder.wallet_text.text = element.wallet_id.toString()
        viewHolder.sum_text.text = element.sum.toString()
        if(element.cost_item_id == null){
            viewHolder.cost_revenue_item_text.text = element.revenue_item_id.toString()
        }else{
            viewHolder.cost_revenue_item_text.text = element.cost_item_id.toString()
        }
    }

    override fun getItemCount() = dataSet.size

}
