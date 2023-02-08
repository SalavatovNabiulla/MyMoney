package com.example.mymoney.models

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class Transaction_type(server_url : String){
    var server_url = server_url
    var http_client = OkHttpClient()
    //
    var id: Int? = null
    var title: String? = null
    //
    fun delete(){
        var json_data = JSONObject()
        json_data.put("id",this.id)
        //
        val http_request: Request = Request.Builder()
            .url(server_url + "/api/delete_transactions_type/")
            .post(json_data.toString().toRequestBody("application/json".toMediaType()))
            .build()
        var call = http_client.newCall(http_request)
        var response = call.execute()
    }

    fun create(){
        var json_data = JSONObject()
        json_data.put("title",this.title)
        //
        val http_request: Request = Request.Builder()
            .url(server_url + "/api/create_transactions_type/")
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

        fun get_transactions_types(server_url: String): ArrayList<Transaction_type> {
            var transactions_types = ArrayList<Transaction_type>()
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_transactions_types/")
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            var json_data = JSONArray(response?.body?.string().toString())
            for(i in 0..json_data.length()-1){
                var current_object = JSONObject(json_data.get(i).toString())
                var transactions_type = Transaction_type(server_url)
                transactions_type.id = current_object.getInt("id")
                transactions_type.title = current_object.getString("title")
                transactions_types.add(transactions_type)
            }
            return transactions_types
        }

        fun get_transactions_type(server_url: String, id: Int?): Transaction_type {
            var transactions_type = Transaction_type(server_url)
            //
            var json = JSONObject()
            json.put("id",id)
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_transactions_type/")
                .post(json.toString().toRequestBody("application/json".toMediaType()))
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            var json_data = JSONObject(response?.body?.string().toString())
            transactions_type.id = json_data.getInt("id")
            transactions_type.title = json_data.getString("title")
            return transactions_type
        }
    }
}