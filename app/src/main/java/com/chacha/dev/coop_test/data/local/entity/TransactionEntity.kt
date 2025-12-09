package com.chacha.dev.coop_test.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val cardId: String,
    val amount: Double,
    val currency: String,
    val date: String,
    val merchant: String,
    val type: String
)

