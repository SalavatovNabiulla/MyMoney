package com.example.mymoney.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance(server_ip : String) {
    val api : MyMoneyApi by lazy {
        Retrofit.Builder()
            .baseUrl(server_ip)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyMoneyApi::class.java)
    }
}
