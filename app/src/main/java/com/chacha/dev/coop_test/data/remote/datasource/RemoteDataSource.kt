package com.chacha.dev.coop_test.data.remote.datasource

import com.chacha.dev.coop_test.data.mapper.toDomain
import com.chacha.dev.coop_test.data.remote.api.NetworkApi
import com.chacha.dev.coop_test.domain.common.Resource
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.UserModel
import javax.inject.Inject

interface CardRemoteDataSource {
    suspend fun fetchCards(): Resource<List<CardModel>>
    suspend fun fetchTransactions(): List<TransactionModel>
    suspend fun fetchUser(): UserModel
}

class CardRemoteDataSourceImpl @Inject constructor(
    private val api: NetworkApi
) : CardRemoteDataSource {
    override suspend fun fetchCards(): Resource<List<CardModel>> {
        return try {
            val cards = api.getCards().cards.map {  it.toDomain() }
            Resource.Success(cards)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error fetching cards")
        }
    }

    override suspend fun fetchTransactions(): List<TransactionModel> {
        return api.getTransactions().transactions.map { it.toDomain() }
    }

    override suspend fun fetchUser(): UserModel {
        return api.getUser().user.toDomain()
    }


}


