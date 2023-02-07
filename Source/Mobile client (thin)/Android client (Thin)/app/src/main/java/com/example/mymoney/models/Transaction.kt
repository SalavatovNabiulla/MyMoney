package com.example.mymoney.models

import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject

class Transaction(server_url : String){
    var server_url = server_url
    var http_client = OkHttpClient()
    //
    var id: Int? = null
    var sum: Int? = null
    var type_id: Int? = null
    var wallet_id: Int? = null
    var cost_item_id: Int? = null
    var revenue_item_id: Int? = null
    var created_time: String? = null
    //
    fun update(){
        //
    }
    fun delete(){
        //
    }
    fun create(){
        //
    }
    //
    companion object{
        fun get_transactions(server_url: String): ArrayList<Transaction> {
            var http_client = OkHttpClient()
            var transactions = ArrayList<Transaction>()
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_transactions/")
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            var json_data = JSONArray(response?.body?.string().toString())
            for(i in 0..json_data.length()-1){
                var current_object = JSONObject(json_data.get(i).toString())
                var new_transaction = Transaction(server_url)
                new_transaction.id = current_object.getInt("id")
                new_transaction.sum = current_object.getInt("sum")
                new_transaction.type_id = current_object.getInt("type_id")
                new_transaction.wallet_id = current_object.getInt("wallet_id")
                if(current_object.isNull("cost_item_id") == false){
                    new_transaction.cost_item_id = current_object.getInt("cost_item_id")
                }
                if(current_object.isNull("revenue_item_id") == false ){
                    new_transaction.revenue_item_id = current_object.getInt("revenue_item_id")
                }
                new_transaction.created_time = current_object.getString("created_time")
                transactions.add(new_transaction)
            }
            return transactions
        }
    }
}