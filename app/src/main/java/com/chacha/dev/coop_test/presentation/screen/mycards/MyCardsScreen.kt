package com.chacha.dev.coop_test.presentation.screen.mycards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.CardType
import com.chacha.dev.coop_test.domain.model.WalletModel
import com.chacha.dev.coop_test.presentation.screen.mycards.components.CardItem

@Composable
fun MyCardsScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onCardClick: (String) -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    MyCardsContent(
        state = state,
        modifier = modifier,
        onCardClick = onCardClick,
        onProfileClick = onProfileClick,
        onRefresh = { viewModel.refresh() }
    )
}

@Composable
private fun MyCardsContent(
    state: MyCardsUiState,
    modifier: Modifier = Modifier,
    onCardClick: (String) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    when {
        state.isLoading -> Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }

        state.error != null -> Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = state.error ?: "Unknown error", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Tap to retry",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onRefresh() }
            )
        }

        else -> LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("My Cards", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "Profile",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onProfileClick() }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(state.cards) { card ->
                CardItem(card = card, onClick = { onCardClick(card.id) })
            }
            if (state.cards.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No cards yet", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Refresh",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { onRefresh() }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
private fun MyCardsScreenPreview() {
    val mockCards = listOf(
        CardModel(
            id = "card_001",
            userId = "user_12345",
            type = CardType.PREPAID,
            name = "Safari Travel Card",
            cardNumber = "4111 1111 1111 1234",
            holderName = "Wanjiku Kimani",
            expiryDate = "12/26",
            status = "ACTIVE",
            balance = 5000.00,
            currency = "KES",
            currentSpend = null,
            creditLimit = null,
            dueDate = null,
            linkedAccountName = null,
            wallets = emptyList()
        ),
        CardModel(
            id = "card_002",
            userId = "user_12345",
            type = CardType.CREDIT,
            name = "Platinum Rewards",
            cardNumber = "5500 0000 0000 5678",
            holderName = "Wanjiku Kimani",
            expiryDate = "08/28",
            status = "ACTIVE",
            balance = null,
            currency = "KES",
            currentSpend = 12500.45,
            creditLimit = 150000.00,
            dueDate = "2025-12-05",
            linkedAccountName = null,
            wallets = emptyList()
        ),
        CardModel(
            id = "card_003",
            userId = "user_12345",
            type = CardType.MULTI_CURRENCY,
            name = "Global Account",
            cardNumber = "3782 8224 6310 0005",
            holderName = "Wanjiku Kimani",
            expiryDate = "01/27",
            status = "BLOCKED",
            balance = null,
            currency = null,
            currentSpend = null,
            creditLimit = null,
            dueDate = null,
            linkedAccountName = null,
            wallets = listOf(
                WalletModel("KES", "🇰🇪", 50000.00),
                WalletModel("USD", "🇺🇸", 500.00),
                WalletModel("EUR", "🇪🇺", 1200.50)
            )
        ),
        CardModel(
            id = "card_004",
            userId = "user_12345",
            type = CardType.DEBIT,
            name = "Everyday Checking",
            cardNumber = "4000 1234 5678 9010",
            holderName = "Wanjiku Kimani",
            expiryDate = "11/29",
            status = "ACTIVE",
            balance = 84500.20,
            currency = "KES",
            currentSpend = null,
            creditLimit = null,
            dueDate = null,
            linkedAccountName = "Checking Account (...8899)",
            wallets = emptyList()
        )
    )
    MyCardsContent(
        state = MyCardsUiState(cards = mockCards, isLoading = false, error = null),
        onCardClick = {},
        onProfileClick = {},
        onRefresh = {}
    )
}