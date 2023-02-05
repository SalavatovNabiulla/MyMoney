package com.example.mymoney.api

import com.example.mymoney.api.models.Transaction
import retrofit2.Response
import retrofit2.http.GET

interface MyMoneyApi {
    @GET("/api/get_transactions/")
    suspend fun getTransactions(): Response<List<Transaction>>
}