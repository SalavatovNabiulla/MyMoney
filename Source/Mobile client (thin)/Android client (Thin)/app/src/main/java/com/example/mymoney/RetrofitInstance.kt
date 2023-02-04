package com.example.mymoney

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: TransactionsApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://nabiulla.pythonanywhere.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TransactionsApi::class.java)
    }
}