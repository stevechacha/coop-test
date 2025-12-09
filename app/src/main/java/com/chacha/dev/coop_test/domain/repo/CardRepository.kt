package com.chacha.dev.coop_test.domain.repo

import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun observeCards(): Flow<List<CardModel>>
    fun observeCard(id: String): Flow<CardModel?>
    fun observeTransactions(cardId: String, limit: Int = 10): Flow<List<TransactionModel>>
    fun observeUser(): Flow<UserModel?>

    suspend fun refreshAll()
    suspend fun toggleBlock(cardId: String)
}

