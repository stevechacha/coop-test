package com.chacha.dev.coop_test.presentation.screen.mycards.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.CardType
import com.chacha.dev.coop_test.domain.model.WalletModel


@Composable
fun CardItem(card: CardModel, onClick: () -> Unit) {
    val colors = when (card.type) {
        CardType.CREDIT -> listOf(Color(0xFF0B8B5D), Color(0xFF04684A))
        CardType.DEBIT -> listOf(Color(0xFF0A5C3C), Color(0xFF094730))
        CardType.MULTI_CURRENCY -> listOf(Color(0xFF2F9E44), Color(0xFF0E6B2C))
        CardType.PREPAID -> listOf(Color(0xFF2E2E2E), Color(0xFF101010))
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        tonalElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(colors))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = card.type.name.replace("_", " ").lowercase()
                                .replaceFirstChar { it.uppercase() },
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = card.status.lowercase().replaceFirstChar { it.uppercase() },
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "VISA",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "**** **** **** ${card.maskedNumber.takeLast(4)}",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = card.holderName,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = card.name,
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "KES ${"%,.2f".format(card.balance ?: card.creditLimit ?: 0.0)}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        val walletInfo = if (card.type == CardType.MULTI_CURRENCY) {
                            "${card.wallets.size} wallets"
                        } else card.currency ?: ""
                        Text(
                            text = walletInfo,
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}