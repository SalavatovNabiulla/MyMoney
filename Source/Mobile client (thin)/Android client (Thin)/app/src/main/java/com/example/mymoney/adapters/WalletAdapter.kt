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
import com.example.mymoney.activities.WalletActivity
import com.example.mymoney.models.Transaction
import com.example.mymoney.models.Wallet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WalletAdapter(
    private val dataSet: List<Wallet>
    ) : RecyclerView.Adapter<WalletAdapter.ViewHolder>() {

    var server_ip = "0.0.0.0"


    lateinit var context: Context

    class ViewHolder(view: View,server_ip : String = "") : RecyclerView.ViewHolder(view) {
        val id_text: TextView
        val title_text : TextView
        val type_text : TextView
        var balance_text: TextView
        var last_tap : Long = 0
        init {
            id_text = view.findViewById(R.id.item_wallet_id_text)
            title_text = view.findViewById(R.id.item_wallet_title_text)
            type_text = view.findViewById(R.id.item_wallet_type_text)
            balance_text = view.findViewById(R.id.item_wallet_balance_text)
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
            var Intent = Intent(context,WalletActivity::class.java)
            Intent.putExtra("EXTRA_ID",id.toInt())
            context.startActivity(Intent)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_wallet, viewGroup, false)

        var new_viewholder = ViewHolder(view,server_ip)

        view.setOnClickListener {onItemClick(new_viewholder)}

        return new_viewholder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var element = dataSet[position]
        viewHolder.id_text.text = element.id.toString()
        viewHolder.title_text.text = element.title
        viewHolder.type_text.text = element.type.title
        viewHolder.balance_text.text = element.balance.toString()
    }

    override fun getItemCount() = dataSet.size

}
