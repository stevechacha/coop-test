package com.chacha.dev.coop_test.data.remote.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddressDto(
    val street: String,
    val city: String,
    val country: String,
    val postalCode: String
)

