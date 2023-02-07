package com.example.mymoney.models

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject


class Wallet(server_url : String){
    var server_url = server_url
    var http_client = OkHttpClient()
    //
    var id: Int? = null
    var title: String? = null
    var type_id: Int? = null
    var balance: Int? = null
    //
    fun update_balance(){
        var wallets_balances = Wallets_balance.get_wallets_balances(server_url)
        for(i in wallets_balances){
            if(i.wallet_id.toString() == this.id.toString()){
                this.balance = i.balance
            }
        }
    }
    //
    companion object{
        fun get_wallets(server_url: String): ArrayList<Wallet> {
            var http_client = OkHttpClient()
            var wallets = ArrayList<Wallet>()
            //
            val http_request: Request = Request.Builder()
                .url(server_url + "/api/get_wallets/")
                .build()
            var call = http_client.newCall(http_request)
            var response = call.execute()
            var json_data = JSONArray(response?.body?.string().toString())
            for(i in 0..json_data.length()-1){
                var current_object = JSONObject(json_data.get(i).toString())
                var new_wallet = Wallet(server_url)
                new_wallet.id = current_object.getInt("id")
                new_wallet.title = current_object.getString("title")
                new_wallet.type_id = current_object.getInt("type_id")
                new_wallet.update_balance()
                wallets.add(new_wallet)
            }
            return wallets
        }
    }
}