package com.chacha.dev.coop_test.domain.model

import java.time.Instant

data class TransactionModel(
    val id: String,
    val cardId: String,
    val amount: Double,
    val currency: String,
    val date: Instant,
    val merchant: String,
    val type: String
)

