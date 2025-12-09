package com.chacha.dev.coop_test.domain.repo

import com.chacha.dev.coop_test.domain.common.Resource
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun getCards(): Flow<Resource<List<CardModel>>>
    fun getCard(id: String): Flow<Resource<CardModel?>>
    fun getTransactions(cardId: String, limit: Int = 10): Flow<Resource<List<TransactionModel>>>
    fun getUser(): Flow<Resource<UserModel?>>

    suspend fun refreshAll(): Resource<Unit>
    suspend fun toggleBlock(cardId: String)
}

