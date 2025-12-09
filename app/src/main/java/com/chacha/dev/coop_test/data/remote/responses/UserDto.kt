package com.chacha.dev.coop_test.data.remote.responses

data class UserDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val avatarUrl: String,
    val address: AddressDto
)

