package com.chacha.dev.coop_test.presentation.screen.details.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.WalletModel
import com.chacha.dev.coop_test.presentation.screen.details.CardDetailsUiState

@Composable
fun CurrencySelector(
    card: CardModel,
    state: CardDetailsUiState,
    showCurrencyMenu: Boolean,
    onCurrencyMenuToggle: (Boolean) -> Unit,
    onSelectWallet: (WalletModel) -> Unit
) {
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCurrencyMenuToggle(true) },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.selectedWallet?.flag ?: card.wallets.firstOrNull()?.flag ?: "🇰🇪",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = state.selectedWallet?.currency ?: card.wallets.firstOrNull()?.currency ?: "KES",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(end = 8.dp)
            )
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "Select Currency",
                tint = Color.White
            )
        }
        DropdownMenu(
            expanded = showCurrencyMenu,
            onDismissRequest = { onCurrencyMenuToggle(false) }
        ) {
            card.wallets.forEach { wallet ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = wallet.flag,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = wallet.currency)
                        }
                    },
                    onClick = {
                        onSelectWallet(wallet)
                        onCurrencyMenuToggle(false)
                    }
                )
            }
        }
    }
}

