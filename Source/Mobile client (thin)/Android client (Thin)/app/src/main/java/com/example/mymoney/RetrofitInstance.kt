package com.example.mymoney

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: TransactionsApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.187")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TransactionsApi::class.java)
    }
}