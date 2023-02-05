package com.example.mymoney.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoney.R
import com.example.mymoney.models.Transaction

class TransactionAdapter(
    private val dataSet: List<Transaction>
    ) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id_text: TextView
        val type_text : TextView
        val wallet_text : TextView
        val sum_text : TextView
        val cost_revenue_item_text : TextView
        init {
            id_text = view.findViewById(R.id.id_text)
            type_text = view.findViewById(R.id.type_text)
            wallet_text = view.findViewById(R.id.wallet_text)
            sum_text = view.findViewById(R.id.sum_text)
            cost_revenue_item_text = view.findViewById(R.id.cost_revenue_item_text)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_transaction, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var element = dataSet[position]
        viewHolder.id_text.text = element.id.toString()
        viewHolder.type_text.text = element.type_id.toString()
        viewHolder.wallet_text.text = element.wallet_id.toString()
        viewHolder.sum_text.text = element.sum.toString()
//        if(element.cost_item_id == null){
//            viewHolder.cost_revenue_item_text.text = element.revenue_item_id.toString()
//        }else{
//            viewHolder.cost_revenue_item_text.text = element.cost_item_id.toString()
//        }
    }

    override fun getItemCount() = dataSet.size

}
