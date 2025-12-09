package com.chacha.dev.coop_test.presentation.screen.mycards

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.CardType

@Composable
fun MyCardsScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onCardClick: (String) -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

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
                modifier = Modifier.clickable { viewModel.refresh() }
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
                            modifier = Modifier.clickable { viewModel.refresh() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardItem(card: CardModel, onClick: () -> Unit) {
    val colors = when (card.type) {
        CardType.CREDIT -> listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
        CardType.DEBIT -> listOf(Color(0xFF00695C), Color(0xFF00897B))
        CardType.MULTI_CURRENCY -> listOf(Color(0xFF4A148C), Color(0xFF7B1FA2))
        CardType.PREPAID -> listOf(Color(0xFFBF360C), Color(0xFFE64A19))
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .background(Brush.linearGradient(colors))
                .padding(16.dp)
        ) {
            Text(card.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${card.type.name.lowercase().replaceFirstChar { it.uppercase() }} • ${card.status}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "**** ${card.maskedNumber}",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Holder", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
                    Text(card.holderName, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Balance/Limit", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
                    val amount = when {
                        card.creditLimit != null -> card.creditLimit
                        else -> card.balance
                    } ?: 0.0
                    Text("KES ${"%,.2f".format(amount)}", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}