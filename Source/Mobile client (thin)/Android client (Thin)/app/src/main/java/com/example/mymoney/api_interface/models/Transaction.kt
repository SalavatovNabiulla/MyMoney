package com.example.mymoney.api_interface.models

import okhttp3.*
import java.io.IOException


class Transaction (server_ip: String) {

    private var server_ip = server_ip
    private var okHttpClient: OkHttpClient = OkHttpClient()

    public val transaction_id: Int? = null
    public var created_time: String? = null
    public val revenue_item_id: Int? = null
    public var cost_item_id: Int? = null
    public val sum: Int? = null
    public val type_id: Int? = null
    public val wallet_id: Int? = null

    public fun get_all_transactions(){
        val request: Request = Request.Builder().url(server_ip+"/api/get_transactions/").build()
        var transactions = ArrayList<Transaction>()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                //
            }

            override fun onResponse(call: Call, response: Response) {
                //
            }
        })
    }
}