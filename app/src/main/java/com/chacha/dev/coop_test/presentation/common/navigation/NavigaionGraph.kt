package com.chacha.dev.coop_test.presentation.common.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable data object HomeDashBoard : Screen
    @Serializable data class CardDetails(val id: String) : Screen
    @Serializable data object UserProfile : Screen
}