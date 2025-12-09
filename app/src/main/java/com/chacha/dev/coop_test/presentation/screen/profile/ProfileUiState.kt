package com.chacha.dev.coop_test.presentation.screen.profile

import com.chacha.dev.coop_test.domain.model.UserModel

data class ProfileUiState(
    val user: UserModel? = null,
    val isLoading: Boolean = true
)
