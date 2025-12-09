package com.chacha.dev.coop_test.domain.model

data class UserModel(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val avatarUrl: String,
    val street: String,
    val city: String,
    val country: String,
    val postalCode: String
) {
    val fullName: String get() = "$firstName $lastName"
}

