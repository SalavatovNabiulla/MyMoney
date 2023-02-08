package com.example.mymoney.models

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class Revenue_item(server_url : String){
    var server_url = server_url
    var http_client = OkHttpClient()
    //
    var id: Int? = null
    var title: String? = null
    //
    fun update(){
        var json_data = JSONObject()
        json_data.put("id",this.id)
        json_data.put("title",this.title)
        //
        val http_request: Request = Request.Builder()
            .url(server_url + "/api/update_revenue_item")
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
            .url(server_url + "/api/delete_revenue_item/")
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
            .url(server_url + "/api/create_revenue_item/")
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

        fun get_revenue_items(server_url: String): ArrayList<Revenue_item> {
            var revenue_items = ArrayList<Revenue_item>()
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_revenue_items/")
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            var json_data = JSONArray(response?.body?.string().toString())
            for(i in 0..json_data.length()-1){
                var current_object = JSONObject(json_data.get(i).toString())
                var new_revenue_item = Revenue_item(server_url)
                new_revenue_item.id = current_object.getInt("id")
                new_revenue_item.title = current_object.getString("title")
                revenue_items.add(new_revenue_item)
            }
            return revenue_items
        }

        fun get_revenue_item(server_url: String, id: Int?): Revenue_item {
            var revenue_item = Revenue_item(server_url)
            //
            var json = JSONObject()
            json.put("id",id)
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_revenue_item/")
                .post(json.toString().toRequestBody("application/json".toMediaType()))
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            var json_data = JSONObject(response?.body?.string().toString())
            revenue_item.id = json_data.getInt("id")
            revenue_item.title = json_data.getString("title")
            return revenue_item
        }
    }
}