package com.example.mymoney.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoney.R
import com.example.mymoney.activities.CostItemActivity
import com.example.mymoney.activities.WalletsTypeActivity
import com.example.mymoney.models.Cost_item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CostItemAdapter(
    private val dataSet: List<Cost_item>
    ) : RecyclerView.Adapter<CostItemAdapter.ViewHolder>() {

    var server_ip = "0.0.0.0"


    lateinit var context: Context

    class ViewHolder(view: View,server_ip : String = "") : RecyclerView.ViewHolder(view) {
        val id_text: TextView
        val title_text : TextView
        var last_tap : Long = 0
        init {
            id_text = view.findViewById(R.id.item_cost_item_id_text)
            title_text = view.findViewById(R.id.item_cost_item_title_text)
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
            var Intent = Intent(context,CostItemActivity::class.java)
            Intent.putExtra("EXTRA_ID",id.toInt())
            context.startActivity(Intent)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_cost_item, viewGroup, false)

        var new_viewholder = ViewHolder(view,server_ip)

        view.setOnClickListener {onItemClick(new_viewholder)}

        return new_viewholder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var element = dataSet[position]
        viewHolder.id_text.text = element.id.toString()
        viewHolder.title_text.text = element.title
    }

    override fun getItemCount() = dataSet.size

}
