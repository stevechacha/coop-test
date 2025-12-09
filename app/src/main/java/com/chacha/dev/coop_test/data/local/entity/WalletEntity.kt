package com.chacha.dev.coop_test.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val cardId: String,
    val currency: String,
    val flag: String,
    val balance: Double
)

