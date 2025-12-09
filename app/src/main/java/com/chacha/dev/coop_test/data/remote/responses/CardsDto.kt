package com.chacha.dev.coop_test.data.remote.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardsDto(
    val cards: List<CardDto>
)