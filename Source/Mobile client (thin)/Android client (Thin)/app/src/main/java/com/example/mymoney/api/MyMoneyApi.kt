package com.example.mymoney.api

import com.example.mymoney.models.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MyMoneyApi {

    @GET("/api/get_wallets_types/")
    suspend fun getWalletsTypes(): Response<List<Wallets_type>>

    @GET("/api/get_wallets/")
    suspend fun getWallets(): Response<List<Wallet>>

    @GET("/api/get_wallets_balances/")
    suspend fun getWalletsBalances(): Response<List<Wallets_balance>>

    @GET("/api/get_transactions_types/")
    suspend fun getTransactionsTypes(): Response<List<Transaction_type>>

    //++Transactions
    @GET("/api/get_transactions/")
    suspend fun getTransactions(): Response<List<Transaction>>

    //--Transactions
    @GET("/api/get_revenue_items/")
    suspend fun getRevenueItems(): Response<List<Revenue_item>>

    @GET("/api/get_cost_items/")
    suspend fun getCostItems(): Response<List<Cost_item>>

}