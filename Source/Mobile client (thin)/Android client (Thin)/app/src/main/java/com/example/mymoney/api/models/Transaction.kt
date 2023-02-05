package com.example.mymoney.api.models

data class Transaction(
    val cost_item_id: Int,
    val created_time: String,
    val id: Int,
    val revenue_item_id: Int,
    val sum: Int,
    val type_id: Int,
    val wallet_id: Int
)