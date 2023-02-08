package com.example.mymoney.models

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class Transaction(server_url : String = ""){
    var server_url = server_url
    var http_client = OkHttpClient()
    //
    var id: Int = 0
    var sum: Int = 0
    lateinit var type: Transaction_type
    lateinit var wallet: Wallet
    lateinit var cost_item: Cost_item
    lateinit var revenue_item: Revenue_item
    var created_time: String? = null
    //
    fun update(){
        var json_data = JSONObject()
        json_data.put("id",this.id)
        json_data.put("type_id",this.type.id)
        json_data.put("wallet_id",this.wallet.id)
        json_data.put("sum",this.sum)
        json_data.put("revenue_item_id",this.revenue_item.id)
        json_data.put("cost_item_id",this.cost_item.id)
        //
        val http_request: Request = Request.Builder()
            .url(server_url + "/api/update_transaction")
            .post(json_data.toString().toRequestBody("application/json".toMediaType()))
            .build()
        var call = http_client.newCall(http_request)
        var response = call.execute()
    }

    fun delete(){
        var json_data = JSONObject()
        json_data.put("id",this.id)
        //
        val http_request: Request = Request.Builder()
            .url(server_url + "/api/delete_transaction/")
            .post(json_data.toString().toRequestBody("application/json".toMediaType()))
            .build()
        var call = http_client.newCall(http_request)
        var response = call.execute()
    }

    fun create(){
        var json_data = JSONObject()
        json_data.put("type_id",this.type.id)
        json_data.put("wallet_id",this.wallet.id)
        json_data.put("sum",this.sum)
        json_data.put("revenue_item_id",this.revenue_item.id)
        json_data.put("cost_item_id",this.cost_item.id)
        //
        val http_request: Request = Request.Builder()
            .url(server_url + "/api/create_transaction/")
            .post(json_data.toString().toRequestBody("application/json".toMediaType()))
            .build()
        var call = Wallet.http_client.newCall(http_request)
        var response = call.execute()
        var json_result = JSONObject(response?.body?.string().toString())
        this.id = json_result.getInt("id")
    }
    //
    fun isCost_itemInitialised() = ::cost_item.isInitialized
    //
    companion object{
        var http_client = OkHttpClient()

        fun get_transactions(server_url: String = ""): ArrayList<Transaction> {
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
                var transaction = Transaction(server_url)
                transaction.id = current_object.getInt("id")
                transaction.sum = current_object.getInt("sum")
                transaction.type = Transaction_type.get_transactions_type(id=current_object.getInt("type_id"),server_url = server_url)
                transaction.wallet = Wallet.get_wallet(id=current_object.getInt("wallet_id"),server_url = server_url)
                if(current_object.isNull("cost_item_id") == false){
                    transaction.cost_item = Cost_item.get_cost_item(id=current_object.getInt("cost_item_id"),server_url = server_url)
                }
                if(current_object.isNull("revenue_item_id") == false ){
                    transaction.revenue_item = Revenue_item.get_revenue_item(id=current_object.getInt("revenue_item_id"),server_url = server_url)
                }
                transaction.created_time = current_object.getString("created_time")
                transactions.add(transaction)
            }
            return transactions
        }

        fun get_transaction(server_url: String = "", id: Int = 0): Transaction {
            var transaction = Transaction(server_url)
            //
            var json = JSONObject()
            json.put("id",id)
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_transaction/")
                .post(json.toString().toRequestBody("application/json".toMediaType()))
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            if(response.code == 200){
                var json_data = JSONObject(response?.body?.string().toString())
                transaction.id = json_data.getInt("id")
                transaction.sum = json_data.getInt("sum")
                transaction.type = Transaction_type.get_transactions_type(id=json_data.getInt("type_id"),server_url = server_url)
                transaction.wallet = Wallet.get_wallet(id=json_data.getInt("wallet_id"),server_url = server_url)
                if(json_data.isNull("cost_item_id") == false){
                    transaction.cost_item = Cost_item.get_cost_item(id=json_data.getInt("cost_item_id"),server_url = server_url)
                }
                if(json_data.isNull("revenue_item_id") == false ){
                    transaction.revenue_item = Revenue_item.get_revenue_item(id=json_data.getInt("revenue_item_id"),server_url = server_url)
                }
                transaction.created_time = json_data.getString("created_time")
            }
            return transaction
        }
    }
}