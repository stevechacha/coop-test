package com.chacha.dev.coop_test.presentation.screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chacha.dev.coop_test.domain.model.CardType
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.WalletModel
import com.chacha.dev.coop_test.presentation.screen.details.components.CardBalanceSection
import com.chacha.dev.coop_test.presentation.screen.details.components.CardDetailsCard
import com.chacha.dev.coop_test.presentation.screen.details.components.CardDetailsTopBar
import com.chacha.dev.coop_test.presentation.screen.details.components.CurrencySelector
import com.chacha.dev.coop_test.presentation.screen.details.components.OverlappingBoxes
import com.chacha.dev.coop_test.presentation.screen.details.components.QuickActionButtons
import com.chacha.dev.coop_test.presentation.screen.details.components.RecentTransfersHeader
import com.chacha.dev.coop_test.presentation.screen.details.components.TransactionsBottomSheet
import java.time.Instant


@Composable
fun CardDetailsScreen(
    cardId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CardDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(cardId) {
        viewModel.loadCardDetails(cardId)
    }

    if (state.card == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    CardDetailsContent(
        state = state,
        onBack = onBack,
        onToggleBlock = { viewModel.onToggleBlock() },
        onSelectWallet = { viewModel.onSelectWallet(it) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardDetailsContent(
    state: CardDetailsUiState,
    onBack: () -> Unit,
    onToggleBlock: () -> Unit,
    onSelectWallet: (WalletModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val card = state.card ?: return
    var balanceVisible by remember { mutableStateOf(true) }
    var showTransactionsSheet by remember { mutableStateOf(true) }
    var showCurrencyMenu by remember { mutableStateOf(false) }
    val isEmpty = remember { state.transactions.isEmpty() }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { targetValue ->
            val isCurrentlyEmpty = state.transactions.isEmpty()
            !isCurrentlyEmpty
        }
    )

    LaunchedEffect(Unit) {
        try {
            sheetState.show()
        } catch (e: Exception) {
        }
    }

    val cardColors = when (card.type) {
        CardType.CREDIT -> listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
        CardType.DEBIT -> listOf(Color(0xFF00695C), Color(0xFF00897B))
        CardType.MULTI_CURRENCY -> listOf(Color(0xFF4A148C), Color(0xFF7B1FA2))
        CardType.PREPAID -> listOf(Color(0xFF1A1A1A), Color(0xFF2D2D2D))
    }

    Scaffold(
        topBar = {
            CardDetailsTopBar(
                card = card,
                state = state,
                balanceVisible = balanceVisible,
                showCurrencyMenu = showCurrencyMenu,
                onBack = onBack,
                onBalanceVisibilityToggle = { balanceVisible = !balanceVisible },
                onCurrencyMenuToggle = { showCurrencyMenu = !showCurrencyMenu },
                onSelectWallet = onSelectWallet
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OverlappingBoxes(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color(0xFF2E7D32))
                )

                CardDetailsCard(
                    card = card,
                    cardColors = cardColors
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            QuickActionButtons(
                cardStatus = card.status,
                onToggleBlock = onToggleBlock
            )

            Spacer(modifier = Modifier.height(24.dp))

            RecentTransfersHeader()
        }
    }

    if (showTransactionsSheet) {
        TransactionsBottomSheet(
            transactions = state.transactions,
            sheetState = sheetState,
            onDismiss = { showTransactionsSheet = false }
        )
    }
}



@Composable
private fun CardDetailsScreenPreview() {
    val mockCard = com.chacha.dev.coop_test.domain.model.CardModel(
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
    )

    val mockTx = listOf(
        TransactionModel("tx_01", "card_001", 1500.00, "KES", java.time.Instant.parse("2023-11-19T14:30:00Z"), "Naivas Supermarket", "DEBIT"),
        TransactionModel("tx_02", "card_001", 200.00, "KES", java.time.Instant.parse("2023-11-18T09:15:00Z"), "Java House", "DEBIT"),
        TransactionModel("tx_03", "card_001", 5000.00, "KES", java.time.Instant.parse("2023-11-15T11:00:00Z"), "M-PESA Topup", "CREDIT"),
        TransactionModel("tx_04", "card_001", 350.00, "KES", java.time.Instant.parse("2023-11-14T18:45:00Z"), "Uber Ride", "DEBIT"),
        TransactionModel("tx_05", "card_001", 1200.00, "KES", java.time.Instant.parse("2023-11-12T13:20:00Z"), "Carrefour", "DEBIT"),
        TransactionModel("tx_06", "card_001", 100.00, "KES", java.time.Instant.parse("2023-11-10T08:00:00Z"), "Airtime Purchase", "DEBIT"),
        TransactionModel("tx_07", "card_001", 2500.00, "KES", java.time.Instant.parse("2023-11-08T16:30:00Z"), "Total Station", "DEBIT"),
        TransactionModel("tx_08", "card_001", 450.00, "KES", java.time.Instant.parse("2023-11-05T12:10:00Z"), "KFC", "DEBIT"),
        TransactionModel("tx_09", "card_001", 3000.00, "KES", java.time.Instant.parse("2023-11-01T10:00:00Z"), "Load via Bank", "CREDIT"),
        TransactionModel("tx_10", "card_001", 900.00, "KES", java.time.Instant.parse("2023-10-28T19:00:00Z"), "Netflix Subscription", "DEBIT"),
        TransactionModel("tx_11", "card_003", 12.50, "USD", java.time.Instant.parse("2023-11-20T08:00:00Z"), "Amazon US", "DEBIT"),
        TransactionModel("tx_12", "card_003", 5.00, "USD", java.time.Instant.parse("2023-11-19T15:00:00Z"), "Starbucks NYC", "DEBIT"),
        TransactionModel("tx_13", "card_003", 45.00, "EUR", java.time.Instant.parse("2023-11-18T12:00:00Z"), "Zara Paris", "DEBIT"),
        TransactionModel("tx_14", "card_003", 10.00, "GBP", java.time.Instant.parse("2023-11-17T09:30:00Z"), "Tesco London", "DEBIT"),
        TransactionModel("tx_15", "card_003", 2000.00, "JPY", java.time.Instant.parse("2023-11-16T18:00:00Z"), "7-Eleven Tokyo", "DEBIT"),
        TransactionModel("tx_16", "card_003", 100.00, "USD", java.time.Instant.parse("2023-11-15T10:00:00Z"), "Wallet Topup", "CREDIT"),
        TransactionModel("tx_17", "card_003", 50.00, "AUD", java.time.Instant.parse("2023-11-14T14:20:00Z"), "Woolworths Sydney", "DEBIT"),
        TransactionModel("tx_18", "card_003", 25.00, "CAD", java.time.Instant.parse("2023-11-13T11:10:00Z"), "Tim Hortons", "DEBIT"),
        TransactionModel("tx_19", "card_003", 1500.00, "KES", java.time.Instant.parse("2023-11-12T09:00:00Z"), "Jumia Kenya", "DEBIT"),
        TransactionModel(
            "tx_20",
            "card_003",
            60.00,
            "EUR",
            Instant.parse("2023-11-10T16:45:00Z"),
            "Train Ticket (SNCF)",
            "PREPAID"
        )
    )

    CardDetailsContent(
        state = CardDetailsUiState(
            card = mockCard,
            transactions = mockTx.filter { it.cardId == "card_001" },
            allTransactions = mockTx.filter { it.cardId == "card_001" },
            selectedWallet = null,
            isLoading = false,
            error = null
        ),
        onBack = {},
        onToggleBlock = {},
        onSelectWallet = {}
    )
}
