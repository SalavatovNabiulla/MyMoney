package com.example.mymoney.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    public var server_ip = "0.0.0.0"
    val api : MyMoneyApi by lazy {
        Retrofit.Builder()
            .baseUrl(server_ip)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyMoneyApi::class.java)
    }
}