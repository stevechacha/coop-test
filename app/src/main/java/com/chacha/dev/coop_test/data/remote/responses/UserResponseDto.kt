package com.chacha.dev.coop_test.data.remote.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponseDto(
    val user: UserDto
)

