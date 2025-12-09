package com.chacha.dev.coop_test.data.remote.responses

data class TransactionDto(
    val amount: Double,
    val cardId: String,
    val currency: String,
    val date: String,
    val id: String,
    val merchant: String,
    val type: String
)