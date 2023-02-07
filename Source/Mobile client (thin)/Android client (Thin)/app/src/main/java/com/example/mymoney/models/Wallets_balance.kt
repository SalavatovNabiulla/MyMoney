package com.example.mymoney.models

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class Wallets_balance(server_url : String){
    var server_url = server_url
    var http_client = OkHttpClient()
    //
    var id: Int? = null
    var balance: Int? = null
    var wallet_id: Int? = null
    //
    //
    companion object{
        fun get_wallets_balances(server_url: String): ArrayList<Wallets_balance> {
            var http_client = OkHttpClient()
            var wallets_balances = ArrayList<Wallets_balance>()
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_wallets_balances/")
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            var json_data = JSONArray(response?.body?.string().toString())
            for(i in 0..json_data.length()-1){
                var current_object = JSONObject(json_data.get(i).toString())
                var new_wallets_balance = Wallets_balance(server_url)
                new_wallets_balance.id = current_object.getInt("id")
                new_wallets_balance.balance = current_object.getInt("balance")
                new_wallets_balance.wallet_id = current_object.getInt("wallet_id")
                wallets_balances.add(new_wallets_balance)
            }
            return wallets_balances
        }
    }
}