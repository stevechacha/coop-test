package com.chacha.dev.coop_test.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val type: String,
    val name: String,
    val cardNumber: String,
    val holderName: String,
    val expiryDate: String,
    val status: String,
    val balance: Double?,
    val currency: String?,
    val currentSpend: Double?,
    val creditLimit: Double?,
    val dueDate: String?,
    val linkedAccountName: String?
)

