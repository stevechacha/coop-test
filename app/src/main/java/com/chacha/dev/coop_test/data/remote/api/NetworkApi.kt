package com.chacha.dev.coop_test.data.remote.api

import com.chacha.dev.coop_test.data.remote.responses.CardsDto
import com.chacha.dev.coop_test.data.remote.responses.TransactionsDto
import com.chacha.dev.coop_test.data.remote.responses.UserResponseDto
import okhttp3.ResponseBody
import retrofit2.http.GET

interface NetworkApi {
    @GET("getUser")
    suspend fun getUser(): UserResponseDto


    @GET("getCards")
    suspend fun getCards(): CardsDto

    @GET("cardTransactions")
    suspend fun getTransactions(): TransactionsDto
}