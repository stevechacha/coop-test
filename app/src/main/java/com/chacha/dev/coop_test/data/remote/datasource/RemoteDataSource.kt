package com.chacha.dev.coop_test.data.remote.datasource

import com.chacha.dev.coop_test.data.mapper.toDomain
import com.chacha.dev.coop_test.data.remote.api.NetworkApi
import com.chacha.dev.coop_test.domain.common.Resource
import com.chacha.dev.coop_test.domain.model.Address
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.UserModel
import javax.inject.Inject
import org.json.JSONObject

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
        return runCatching { api.getUser().user.toDomain() }
            .getOrElse { parseLenientUser() }
    }

    private suspend fun parseLenientUser(): UserModel {
        val rawBody = api.getUserRaw().string()
        val cleaned = rawBody.trim().let { text ->
            if (text.startsWith("\"") && text.endsWith("\"")) {
                text.substring(1, text.length - 1)
            } else text
        }
        val normalized = if (cleaned.trim().startsWith("{")) cleaned else "{${cleaned}}"
        val root = org.json.JSONObject(normalized)
        val userObj = root.optJSONObject("user") ?: root
        val addressObj = userObj.optJSONObject("address")

        return UserModel(
            id = userObj.optString("id"),
            firstName = userObj.optString("firstName"),
            lastName = userObj.optString("lastName"),
            email = userObj.optString("email"),
            phone = userObj.optString("phone"),
            avatarUrl = userObj.optString("avatarUrl"),
            address = com.chacha.dev.coop_test.domain.model.Address(
                street = addressObj?.optString("street").orEmpty(),
                city = addressObj?.optString("city").orEmpty(),
                country = addressObj?.optString("country").orEmpty(),
                postalCode = addressObj?.optString("postalCode").orEmpty()
            )
        )
    }
}
