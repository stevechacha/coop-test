package com.chacha.dev.coop_test.presentation.screen.details

import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.WalletModel


data class CardDetailsUiState(
    val card: CardModel? = null,
    val selectedWallet: WalletModel? = null,
    val transactions: List<TransactionModel> = emptyList(),
    val allTransactions: List<TransactionModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
