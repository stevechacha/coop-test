package com.chacha.dev.coop_test.presentation.screen.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.CardType
import com.chacha.dev.coop_test.domain.model.WalletModel
import com.chacha.dev.coop_test.presentation.screen.details.CardDetailsUiState

@Composable
fun CardDetailsTopBar(
    card: CardModel,
    state: CardDetailsUiState,
    balanceVisible: Boolean,
    showCurrencyMenu: Boolean,
    onBack: () -> Unit,
    onBalanceVisibilityToggle: () -> Unit,
    onCurrencyMenuToggle: (Boolean) -> Unit,
    onSelectWallet: (WalletModel) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2E7D32))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
            
            if (card.type == CardType.MULTI_CURRENCY && card.wallets.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                CurrencySelector(
                    card = card,
                    state = state,
                    showCurrencyMenu = showCurrencyMenu,
                    onCurrencyMenuToggle = onCurrencyMenuToggle,
                    onSelectWallet = onSelectWallet
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            CardBalanceSection(
                card = card,
                state = state,
                balanceVisible = balanceVisible,
                onBalanceVisibilityToggle = onBalanceVisibilityToggle
            )
        }
    }
}


