package com.chacha.dev.coop_test.presentation.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.chacha.dev.coop_test.presentation.screen.details.CardDetailsScreen
import com.chacha.dev.coop_test.presentation.screen.mycards.MyCardsScreen
import com.chacha.dev.coop_test.presentation.screen.profile.UserProfileScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        startDestination = Screen.HomeDashBoard,
        navController = navController
    ) {
        composable<Screen.HomeDashBoard> {
            MyCardsScreen(
                onCardClick = { id -> navController.navigate(Screen.CardDetails(id)) },
                onProfileClick = { navController.navigate(Screen.UserProfile) }
            )
        }

        composable<Screen.CardDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.CardDetails>()
            CardDetailsScreen(
                cardId = args.id,
                onBack = { navController.popBackStack() },
                modifier = Modifier
            )
        }

        composable<Screen.UserProfile> {
            UserProfileScreen(
                onBack = { navController.popBackStack() },
                )
        }
    }
}