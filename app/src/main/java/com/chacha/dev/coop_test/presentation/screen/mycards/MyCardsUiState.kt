package com.chacha.dev.coop_test.presentation.screen.mycards

import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.UserModel

data class MyCardsUiState(
    val cards: List<CardModel> = emptyList(),
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

