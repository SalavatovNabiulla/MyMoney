package com.example.mymoney.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    public var server_api = "0.0.0.0"
    val api: MyMoneyApi by lazy {
        Retrofit.Builder()
            .baseUrl(server_api.toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyMoneyApi::class.java)
    }

}