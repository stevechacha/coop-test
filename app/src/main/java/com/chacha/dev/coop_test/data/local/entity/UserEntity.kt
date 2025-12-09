package com.chacha.dev.coop_test.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val avatarUrl: String,
    val street: String,
    val city: String,
    val country: String,
    val postalCode: String
)

