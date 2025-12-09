package com.chacha.dev.coop_test.presentation.screen.mycards.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chacha.dev.coop_test.R
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.CardType


@Composable
fun CardItem(card: CardModel, onClick: () -> Unit) {
    val cardBackground = when (card.type) {
        CardType.CREDIT -> R.drawable.credit_card
        CardType.DEBIT -> R.drawable.debit_card_active
        CardType.MULTI_CURRENCY -> R.drawable.multi_currency_card
        CardType.PREPAID -> R.drawable.prepaid_card
    }

    val cardTypeText = when (card.type) {
        CardType.CREDIT -> "CREDIT CARD"
        CardType.DEBIT -> "DEBIT CARD"
        CardType.MULTI_CURRENCY -> "MULTI-CURRENCY"
        CardType.PREPAID -> "PREPAID CARD"
    }

    val aspectRatio = when (card.type) {
        CardType.DEBIT -> 344f / 220f
        CardType.CREDIT -> 368f / 238f
        CardType.PREPAID -> 332f / 212f
        CardType.MULTI_CURRENCY -> 332f / 212f
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = cardBackground),
                contentDescription = cardTypeText,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Text(
                        text = cardTypeText,
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 0.2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = card.status,
                        color = if (card.status == "ACTIVE") Color(0xFF4CAF50) else Color(0xFFF44336),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        fontSize = 7.sp
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = formatCardNumber(card.cardNumber),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 2.5.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "MONTH/YEAR",
                                color = Color.White.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 6.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = card.expiryDate,
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "CARD CVV",
                                color = Color.White.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 6.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "XXX",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (card.type == CardType.MULTI_CURRENCY) {
                            card.holderName.uppercase()
                        } else {
                            card.name.uppercase()
                        },
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun formatCardNumber(cardNumber: String): String {
    val cleaned = cardNumber.replace(" ", "")
    return if (cleaned.length >= 4) {
        val last4 = cleaned.takeLast(4)
        "0441 XXXX XXXX $last4"
    } else {
        "0441 XXXX XXXX XXXX"
    }
}