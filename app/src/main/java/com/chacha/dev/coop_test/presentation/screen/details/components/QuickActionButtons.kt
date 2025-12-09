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
            onClick = { }
        )
        QuickActionButton(
            iconRes = R.drawable.deposit,
            label = "Deposit",
            onClick = { }
        )
        QuickActionButton(
            iconRes = R.drawable.ic_withdraw,
            label = "Withdraw",
            onClick = { }
        )
        QuickActionButton(
            iconRes = R.drawable.blocked,
            label = if (cardStatus == "ACTIVE") "Block Card" else "Unblock Card",
            onClick = onToggleBlock
        )
    }
}

