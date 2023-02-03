package com.example.mymoney

import retrofit2.Response
import retrofit2.http.GET

interface TransactionsApi {
    @GET("/api/get_transactions/")
    suspend fun getTransactions(): Response<List<Transaction>>
}