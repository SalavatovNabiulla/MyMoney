package com.example.mymoney.models

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject


class Wallet(server_url : String){
    var server_url = server_url
    var http_client = OkHttpClient()
    //
    var id: Int = 0
    var title: String = ""
    lateinit var type : Wallets_type
    var balance: Int = 0
    //
    fun update(){
        var json_data = JSONObject()
        json_data.put("id",this.id)
        json_data.put("title",this.title)
        json_data.put("type_id",this.type.id)
        //
        val http_request: Request = Request.Builder()
            .url(server_url + "/api/update_wallet/")
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
            .url(server_url + "/api/delete_wallet/")
            .post(json_data.toString().toRequestBody("application/json".toMediaType()))
            .build()
        var call = http_client.newCall(http_request)
        var response = call.execute()
    }

    fun create(){
        var json_data = JSONObject()
        json_data.put("title",this.title)
        json_data.put("type_id",this.type.id)
        //
        val http_request: Request = Request.Builder()
            .url(server_url + "/api/create_wallet/")
            .post(json_data.toString().toRequestBody("application/json".toMediaType()))
            .build()
        var call = http_client.newCall(http_request)
        var response = call.execute()
        var json_result = JSONObject(response?.body?.string().toString())
        this.id = json_result.getInt("id")
        this.update_balance()
    }

    fun update_balance() {
        var wallets_balance = Wallets_balance.get_wallets_balance(server_url = server_url, wallet_id = this.id)
        this.balance = wallets_balance.balance
    }

    companion object{
        var http_client = OkHttpClient()
        fun get_wallets(server_url: String = ""): ArrayList<Wallet> {
            var wallets = ArrayList<Wallet>()
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_wallets/")
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            if(response.code == 200) {
                var json_data = JSONArray(response?.body?.string().toString())
                for (i in 0..json_data.length() - 1) {
                    var current_object = JSONObject(json_data.get(i).toString())
                    var new_wallet = Wallet(server_url)
                    new_wallet.id = current_object.getInt("id")
                    new_wallet.title = current_object.getString("title")
                    //
                    var json_wallets_type =
                        JSONObject(current_object.getJSONObject("type").toString())
                    new_wallet.type = Wallets_type(server_url = server_url)
                    new_wallet.type.id = json_wallets_type.getInt("id")
                    new_wallet.type.title = json_wallets_type.getString("title")
                    //
                    new_wallet.update_balance()
                    wallets.add(new_wallet)
                }
            }
            return wallets
        }
        fun get_wallet(server_url: String = "", id : Int = 0, title : String = ""): Wallet {
            var wallet = Wallet(server_url)
            //
            var json = JSONObject()
            json.put("id",id)
            json.put("title",title)
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_wallet/")
                .post(json.toString().toRequestBody("application/json".toMediaType()))
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            if(response.code == 200) {
                var json_data = JSONObject(response?.body?.string().toString())
                wallet.id = json_data.getInt("id")
                wallet.title = json_data.getString("title")
                var json_wallets_type = JSONObject(json_data.getJSONObject("type").toString())
                wallet.type = Wallets_type(server_url = server_url)
                wallet.type.id = json_wallets_type.getInt("id")
                wallet.type.title = json_wallets_type.getString("title")
                //
                wallet.update_balance()
            }
            return wallet
        }
    }
}