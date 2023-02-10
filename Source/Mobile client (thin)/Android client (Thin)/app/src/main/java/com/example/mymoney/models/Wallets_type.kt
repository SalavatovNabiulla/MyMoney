package com.example.mymoney.models

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class Wallets_type(server_url : String){
    var server_url = server_url
    var http_client = OkHttpClient()
    //
    var id: Int = 0
    lateinit var title: String
    //
    fun update(){
        var json_data = JSONObject()
        json_data.put("id",this.id)
        json_data.put("title",this.title)
        //
        val http_request: Request = Request.Builder()
            .url(server_url + "/api/update_wallets_type/")
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
            .url(server_url + "/api/delete_wallets_type/")
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
            .url(server_url + "/api/create_wallets_type/")
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

        fun get_wallets_types(server_url: String = ""): ArrayList<Wallets_type> {
            var wallets_types = ArrayList<Wallets_type>()
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_wallets_types/")
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            if(response.code == 200) {
                var json_data = JSONArray(response?.body?.string().toString())
                for (i in 0..json_data.length() - 1) {
                    var current_object = JSONObject(json_data.get(i).toString())
                    var new_wallets_type = Wallets_type(server_url)
                    new_wallets_type.id = current_object.getInt("id")
                    new_wallets_type.title = current_object.getString("title")
                    wallets_types.add(new_wallets_type)
                }
            }
            return wallets_types
        }

        fun get_wallets_type(server_url: String = "", id: Int = 0,title: String = ""): Wallets_type {
            var wallets_type = Wallets_type(server_url)
            //
            var json = JSONObject()
            json.put("id",id)
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_wallets_type/")
                .post(json.toString().toRequestBody("application/json".toMediaType()))
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            if(response.code == 200) {
                var json_data = JSONObject(response?.body?.string().toString())
                wallets_type.id = json_data.getInt("id")
                wallets_type.title = json_data.getString("title")
            }
            return wallets_type
        }
    }
}