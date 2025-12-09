package com.chacha.dev.coop_test.presentation.screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chacha.dev.coop_test.domain.model.CardType
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.WalletModel

@Composable
fun CardDetailsScreen(
    cardId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CardDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val card = state.card

    if (card == null) {
        Text("Loading...", modifier = modifier.padding(24.dp))
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                Text(card.name, style = MaterialTheme.typography.titleLarge)
            }
        }
        item {
            Surface(
                tonalElevation = 6.dp,
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Card Number", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(card.cardNumber, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Holder", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(card.holderName, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Expiry", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(card.expiryDate, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Status: ${card.status}", style = MaterialTheme.typography.bodyMedium)
                    Button(
                        onClick = { viewModel.onToggleBlock() },
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text(if (card.status.equals("ACTIVE", true)) "Block Card" else "Unblock Card")
                    }
                }
            }
        }

        if (card.type == CardType.MULTI_CURRENCY && card.wallets.isNotEmpty()) {
            item {
                Text("Wallets", style = MaterialTheme.typography.titleMedium)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    card.wallets.forEach { wallet ->
                        WalletChip(
                            wallet = wallet,
                            selected = wallet.currency == state.selectedWallet?.currency,
                            onClick = { viewModel.onSelectWallet(wallet) }
                        )
                    }
                }
            }
        }

        item {
            Text("Last 10 Transactions", style = MaterialTheme.typography.titleMedium)
        }

        items(state.transactions) { transaction ->
            TransactionRow(transaction)
        }
    }
}

@Composable
private fun WalletChip(wallet: WalletModel, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val content = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = MaterialTheme.shapes.small,
        color = background,
        tonalElevation = if (selected) 4.dp else 0.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(wallet.flag, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(wallet.currency, color = content, fontWeight = FontWeight.SemiBold)
                Text("Balance: ${wallet.balance}", color = content.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun TransactionRow(transaction: TransactionModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(transaction.merchant, fontWeight = FontWeight.SemiBold)
            Text(transaction.currency, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("${transaction.currency} ${"%,.2f".format(transaction.amount)}", fontWeight = FontWeight.Bold)
            Text(transaction.date.toString(), color = Color.Gray, style = MaterialTheme.typography.bodySmall)
        }
    }
}