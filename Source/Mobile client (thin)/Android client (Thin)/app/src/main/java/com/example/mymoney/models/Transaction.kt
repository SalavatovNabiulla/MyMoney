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
    var type: Transaction_type
    var wallet: Wallet
    var cost_item: Cost_item
    var revenue_item: Revenue_item
    var created_time: String? = null
    //
    init {
        type = Transaction_type(server_url=server_url)
        wallet = Wallet(server_url=server_url)
        cost_item = Cost_item(server_url=server_url)
        revenue_item = Revenue_item(server_url=server_url)
    }


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
            .url(server_url + "/api/update_transaction/")
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
                transaction.type = Transaction_type(server_url = server_url)
                var json_transaction_type = JSONObject(current_object.getJSONObject("type").toString())
                transaction.type.id = json_transaction_type.getInt("id")
                transaction.type.title = json_transaction_type.getString("title")
                var json_transaction_wallet = JSONObject(current_object.getJSONObject("wallet").toString())
                transaction.wallet = Wallet(server_url = server_url)
                transaction.wallet.id = json_transaction_wallet.getInt("id")
                transaction.wallet.title = json_transaction_wallet.getString("title")
                transaction.wallet.type = Wallets_type(server_url=server_url)
                var json_transaction_wallet_type = JSONObject(json_transaction_wallet.getJSONObject("type").toString())
                transaction.wallet.type.id = json_transaction_wallet_type.getInt("id")
                transaction.wallet.type.title = json_transaction_wallet_type.getString("title")
                var json_transaction_revenue_item = JSONObject(current_object.getJSONObject("revenue_item").toString())
                if(json_transaction_revenue_item.length() != 0){
                    transaction.revenue_item = Revenue_item(server_url = server_url)
                    transaction.revenue_item.id = json_transaction_revenue_item.getInt("id")
                    transaction.revenue_item.title = json_transaction_revenue_item.getString("title")
                }
                var json_transaction_cost_item = JSONObject(current_object.getJSONObject("cost_item").toString())
                if(json_transaction_cost_item.length() != 0){
                    transaction.cost_item = Cost_item(server_url = server_url)
                    transaction.cost_item.id = json_transaction_cost_item.getInt("id")
                    transaction.cost_item.title = json_transaction_cost_item.getString("title")
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
                var current_object = JSONObject(response?.body?.string().toString())
                transaction.id = current_object.getInt("id")
                transaction.sum = current_object.getInt("sum")
                transaction.type = Transaction_type(server_url = server_url)
                var json_transaction_type = JSONObject(current_object.getJSONObject("type").toString())
                transaction.type.id = json_transaction_type.getInt("id")
                transaction.type.title = json_transaction_type.getString("title")
                var json_transaction_wallet = JSONObject(current_object.getJSONObject("wallet").toString())
                transaction.wallet = Wallet(server_url = server_url)
                transaction.wallet.id = json_transaction_wallet.getInt("id")
                transaction.wallet.title = json_transaction_wallet.getString("title")
                transaction.wallet.type = Wallets_type(server_url=server_url)
                var json_transaction_wallet_type = JSONObject(json_transaction_wallet.getJSONObject("type").toString())
                transaction.wallet.type.id = json_transaction_wallet_type.getInt("id")
                transaction.wallet.type.title = json_transaction_wallet_type.getString("title")
                var json_transaction_revenue_item = JSONObject(current_object.getJSONObject("revenue_item").toString())
                if(json_transaction_revenue_item.length() != 0){
                    transaction.revenue_item = Revenue_item(server_url = server_url)
                    transaction.revenue_item.id = json_transaction_revenue_item.getInt("id")
                    transaction.revenue_item.title = json_transaction_revenue_item.getString("title")
                }
                var json_transaction_cost_item = JSONObject(current_object.getJSONObject("cost_item").toString())
                if(json_transaction_cost_item.length() != 0){
                    transaction.cost_item = Cost_item(server_url = server_url)
                    transaction.cost_item.id = json_transaction_cost_item.getInt("id")
                    transaction.cost_item.title = json_transaction_cost_item.getString("title")
                }
                transaction.created_time = current_object.getString("created_time")
            }
            return transaction
        }
    }
}