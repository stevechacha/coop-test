package com.chacha.dev.coop_test.data.remote.responses

data class Card(
    val balance: Double? = null,
    val cardNumber: String,
    val creditLimit: Double? = null,
    val currency: String? = null,
    val currentSpend: Double? = null,
    val dueDate: String? = null,
    val expiryDate: String,
    val holderName: String,
    val id: String,
    val linkedAccountName: String? = null,
    val name: String,
    val status: String,
    val type: String,
    val userId: String,
    val wallets: List<Wallet> = emptyList()
)