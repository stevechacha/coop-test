package com.chacha.dev.coop_test.domain.model

data class CardModel(
    val id: String,
    val userId: String,
    val type: CardType,
    val name: String,
    val cardNumber: String,
    val holderName: String,
    val expiryDate: String,
    val status: String,
    val balance: Double?,
    val currency: String?,
    val currentSpend: Double?,
    val creditLimit: Double?,
    val dueDate: String?,
    val linkedAccountName: String?,
    val wallets: List<WalletModel> = emptyList()
) {
    val maskedNumber: String
        get() = cardNumber.takeLast(4).padStart(cardNumber.length.coerceAtLeast(4), '*')
}

