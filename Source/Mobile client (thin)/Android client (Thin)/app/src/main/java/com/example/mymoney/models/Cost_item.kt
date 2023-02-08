package com.example.mymoney.models

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class Cost_item(server_url : String){
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
            .url(server_url + "/api/update_cost_item")
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
            .url(server_url + "/api/delete_cost_item/")
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
            .url(server_url + "/api/create_cost_item/")
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

        fun get_cost_items(server_url: String): ArrayList<Cost_item> {
            var cost_items = ArrayList<Cost_item>()
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_cost_items/")
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            var json_data = JSONArray(response?.body?.string().toString())
            for(i in 0..json_data.length()-1){
                var current_object = JSONObject(json_data.get(i).toString())
                var new_cost_item = Cost_item(server_url)
                new_cost_item.id = current_object.getInt("id")
                new_cost_item.title = current_object.getString("title")
                cost_items.add(new_cost_item)
            }
            return cost_items
        }

        fun get_cost_item(server_url: String = "", id: Int = 0, title : String = ""): Cost_item {
            var cost_item = Cost_item(server_url)
            //
            var json = JSONObject()
            json.put("id",id)
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_cost_item/")
                .post(json.toString().toRequestBody("application/json".toMediaType()))
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            var json_data = JSONObject(response?.body?.string().toString())
            cost_item.id = json_data.getInt("id")
            cost_item.title = json_data.getString("title")
            return cost_item
        }
    }
}