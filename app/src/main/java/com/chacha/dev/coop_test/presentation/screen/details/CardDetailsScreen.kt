package com.chacha.dev.coop_test.presentation.screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import com.chacha.dev.coop_test.R
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.size.Scale
import com.chacha.dev.coop_test.domain.model.CardType
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.WalletModel
import com.chacha.dev.coop_test.presentation.screen.details.components.QuickActionButton
import com.chacha.dev.coop_test.presentation.screen.details.components.TransactionRow

private fun formatCardNumber(cardNumber: String): String {
    val cleaned = cardNumber.replace(" ", "")
    return if (cleaned.length >= 4) {
        val last4 = cleaned.takeLast(4)
        "0441 XXXX XXXX $last4"
    } else {
        "XXXX XXXX XXXX XXXX"
    }
}

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

    val cardColors = when (card.type) {
        CardType.CREDIT -> listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
        CardType.DEBIT -> listOf(Color(0xFF00695C), Color(0xFF00897B))
        CardType.MULTI_CURRENCY -> listOf(Color(0xFF4A148C), Color(0xFF7B1FA2))
        CardType.PREPAID -> listOf(Color(0xFF1A1A1A), Color(0xFF2D2D2D))
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2E7D32))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        "Hi ${card.holderName.split(" ").firstOrNull() ?: "User"}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Card Balance",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                        IconButton(onClick = { balanceVisible = !balanceVisible }) {
                            Icon(
                                if (balanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle visibility"
                            )
                        }
                    }
                    val balance = when {
                        card.creditLimit != null -> card.creditLimit
                        else -> card.balance
                    } ?: 0.0
                    Text(
                        if (balanceVisible) "KES ${"%,.2f".format(balance)}" else "KES ••••••",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(220.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.linearGradient(cardColors))
                            .padding(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        card.type.name.replace("_", " "),
                                        color = Color.White.copy(alpha = 0.9f),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Text(
                                        card.status,
                                        color = if (card.status == "ACTIVE") Color(0xFF4CAF50) else Color(
                                            0xFFF44336
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    "COOP BANK",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column {
                                Text(
                                    text = formatCardNumber(card.cardNumber),
                                    color = Color.White,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            "VALID THRU",
                                            color = Color.White.copy(alpha = 0.7f),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Text(
                                            card.expiryDate,
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            "CARD HOLDER",
                                            color = Color.White.copy(alpha = 0.7f),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Text(
                                            card.holderName.uppercase(),
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    card.name.uppercase(),
                                    color = Color.White.copy(alpha = 0.8f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    "VISA",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickActionButton(
                        iconRes = R.drawable.new_card,
                        label = "New Card",
                        color = Color(0xFF4CAF50),
                        onClick = { }
                    )
                    QuickActionButton(
                        iconRes = R.drawable.deposit,
                        label = "Deposit",
                        color = Color(0xFF2196F3),
                        onClick = { }
                    )
                    QuickActionButton(
                        iconRes = R.drawable.ic_withdraw,
                        label = "Withdraw",
                        color = Color(0xFF4CAF50),
                        onClick = { }
                    )
                    QuickActionButton(
                        iconRes = R.drawable.blocked,
                        label = if (card.status == "ACTIVE") "Block Card" else "Unblock Card",
                        color = Color(0xFF4CAF50),
                        onClick = onToggleBlock
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        "Recent Transfers",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Today",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            items(state.transactions.take(10)) { transaction ->
                TransactionRow(transaction)
            }
        }
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
        TransactionModel("tx_20", "card_003", 60.00, "EUR", java.time.Instant.parse("2023-11-10T16:45:00Z"), "Train Ticket (SNCF)", "PREPAID")
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