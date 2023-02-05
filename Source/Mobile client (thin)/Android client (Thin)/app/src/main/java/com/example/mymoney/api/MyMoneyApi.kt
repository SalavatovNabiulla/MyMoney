package com.example.mymoney.api

import com.example.mymoney.models.Transaction
import retrofit2.Response
import retrofit2.http.GET

interface MyMoneyApi {
    @GET("/api/get_transactions/")
    suspend fun getTransactions(): Response<List<Transaction>>
}