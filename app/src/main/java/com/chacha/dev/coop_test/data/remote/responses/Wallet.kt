package com.chacha.dev.coop_test.data.remote.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WalletDto(
    val balance: Double,
    val currency: String,
    val flag: String
)