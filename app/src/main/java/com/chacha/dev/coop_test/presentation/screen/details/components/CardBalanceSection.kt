package com.chacha.dev.coop_test.presentation.screen.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.CardType
import com.chacha.dev.coop_test.presentation.screen.details.CardDetailsUiState

@Composable
fun CardBalanceSection(
    card: CardModel,
    state: CardDetailsUiState,
    balanceVisible: Boolean,
    onBalanceVisibilityToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Card Balance",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        IconButton(onClick = onBalanceVisibilityToggle) {
            Icon(
                if (balanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = "Toggle visibility",
                tint = Color.White
            )
        }
    }
    
    val balance = when {
        card.type == CardType.MULTI_CURRENCY -> state.selectedWallet?.balance ?: card.wallets.firstOrNull()?.balance
        card.creditLimit != null -> card.creditLimit
        else -> card.balance
    } ?: 0.0
    val currency = if (card.type == CardType.MULTI_CURRENCY) {
        state.selectedWallet?.currency ?: card.wallets.firstOrNull()?.currency ?: "KES"
    } else {
        card.currency ?: "KES"
    }
    
    Text(
        if (balanceVisible) "$currency ${"%,.2f".format(balance)}" else "$currency ••••••",
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}


