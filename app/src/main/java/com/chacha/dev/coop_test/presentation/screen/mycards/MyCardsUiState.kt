package com.chacha.dev.coop_test.presentation.screen.mycards

import com.chacha.dev.coop_test.domain.model.CardModel


data class MyCardsUiState(
    val cards: List<CardModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

