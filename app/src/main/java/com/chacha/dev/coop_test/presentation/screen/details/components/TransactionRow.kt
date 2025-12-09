package com.chacha.dev.coop_test.presentation.screen.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chacha.dev.coop_test.R
import com.chacha.dev.coop_test.domain.model.TransactionModel

@Composable
fun TransactionRow(transaction: TransactionModel) {
    val isCredit = transaction.type == "CREDIT"
    val amountColor = if (isCredit) Color(0xFF4CAF50) else Color(0xFFF44336)
    val amountPrefix = if (isCredit) "+" else "-"
    val merchantIcon = getMerchantIcon(transaction.merchant)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (merchantIcon != null) {
                            Image(
                                painter = painterResource(id = merchantIcon),
                                contentDescription = transaction.merchant,
                                modifier = Modifier.size(40.dp),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                transaction.merchant.firstOrNull()?.uppercase() ?: "T",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        transaction.merchant,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        transaction.date.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            Text(
                "$amountPrefix ${transaction.currency} ${"%,.2f".format(transaction.amount)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}

private fun getMerchantIcon(merchant: String): Int? {
    val merchantLower = merchant.lowercase()
    return when {
        merchantLower.contains("naivas") -> R.drawable.naivas
        merchantLower.contains("kfc") -> R.drawable.kfc
        merchantLower.contains("m-pesa") || merchantLower.contains("mpesa") -> R.drawable.m_pesa
        merchantLower.contains("netflix") -> R.drawable.ic_netflix
        merchantLower.contains("withdraw") -> R.drawable.ic_withdraw
        merchantLower.contains("uber") -> R.drawable.ic_withdraw
        merchantLower.contains("java house") -> R.drawable.ic_withdraw
        merchantLower.contains("carrefour") -> R.drawable.naivas
        merchantLower.contains("total") -> R.drawable.ic_withdraw
        merchantLower.contains("airtime") -> R.drawable.m_pesa
        merchantLower.contains("bank") || merchantLower.contains("topup") -> R.drawable.ic_withdraw
        merchantLower.contains("amazon") -> R.drawable.ic_netflix
        merchantLower.contains("starbucks") -> R.drawable.ic_withdraw
        merchantLower.contains("zara") -> R.drawable.ic_withdraw
        merchantLower.contains("tesco") -> R.drawable.naivas
        merchantLower.contains("7-eleven") -> R.drawable.ic_withdraw
        merchantLower.contains("woolworths") -> R.drawable.naivas
        merchantLower.contains("tim hortons") -> R.drawable.ic_withdraw
        merchantLower.contains("jumia") -> R.drawable.ic_netflix
        merchantLower.contains("train") || merchantLower.contains("sncf") -> R.drawable.ic_withdraw
        else -> null
    }
}