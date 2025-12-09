package com.chacha.dev.coop_test.presentation.screen.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chacha.dev.coop_test.R

@Composable
fun QuickActionButtons(
    cardStatus: String,
    onToggleBlock: () -> Unit
) {
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
            label = if (cardStatus == "ACTIVE") "Block Card" else "Unblock Card",
            color = Color(0xFF4CAF50),
            onClick = onToggleBlock
        )
    }
}

