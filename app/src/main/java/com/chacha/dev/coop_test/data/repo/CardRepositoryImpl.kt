package com.chacha.dev.coop_test.data.repo

import com.chacha.dev.coop_test.data.local.datasource.LocalDataSource
import com.chacha.dev.coop_test.data.remote.datasource.CardRemoteDataSource
import com.chacha.dev.coop_test.domain.common.Resource
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.UserModel
import com.chacha.dev.coop_test.domain.repo.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepositoryImpl @Inject constructor(
    private val remoteDataSource: CardRemoteDataSource,
    private val localDataSource: LocalDataSource
) : CardRepository {

    override fun getCards(): Flow<Resource<List<CardModel>>> = flow {
        // First, try to emit cached data if available
        try {
            val cachedCards = localDataSource.getCards().first()
            if (cachedCards.isNotEmpty()) {
                emit(Resource.Success(cachedCards))
            }
        } catch (e: Exception) {
            // If no cache, continue to fetch from remote
        }

        // Emit loading state
        emit(Resource.Loading())

        // Then fetch from remote and update cache
        val result = remoteDataSource.fetchCards()
        when (result) {
            is Resource.Success -> {
                val remoteCards = result.data ?: emptyList()
                // Save to local
                withContext(Dispatchers.IO) {
                    localDataSource.insertCards(remoteCards)
                }
                emit(Resource.Success(remoteCards))
            }
            is Resource.Error -> {
                // If remote fails, try to return cached data
                try {
                    val cachedCards = localDataSource.getCards().first()
                    if (cachedCards.isNotEmpty()) {
                        emit(Resource.Success(cachedCards))
                    } else {
                        emit(Resource.Error(result.message ?: "An error occurred"))
                    }
                } catch (cacheException: Exception) {
                    emit(Resource.Error(result.message ?: "An error occurred"))
                }
            }
            is Resource.Loading -> {
                // Already emitted loading, do nothing
            }
        }
    }

    override fun getCard(id: String): Flow<Resource<CardModel?>> = flow {
        // First, try to get from cache
        try {
            val cachedCard = localDataSource.getCard(id).first()
            if (cachedCard != null) {
                emit(Resource.Success(cachedCard))
                return@flow
            }
        } catch (e: Exception) {
            // Continue to fetch from remote
        }

        // Emit loading state
        emit(Resource.Loading())

        // For now, return cached data or null
        // Remote fetch for single card would need to be added to remote data source
        try {
            val cachedCard = localDataSource.getCard(id).first()
            emit(Resource.Success(cachedCard))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to fetch card"))
        }
    }

    override fun getTransactions(cardId: String, limit: Int): Flow<Resource<List<TransactionModel>>> = flow {
        // First, try to emit cached data if available
        try {
            val cachedTransactions = localDataSource.getTransactions(cardId, limit).first()
            if (cachedTransactions.isNotEmpty()) {
                emit(Resource.Success(cachedTransactions))
            }
        } catch (e: Exception) {
            // If no cache, continue to fetch from remote
        }

        // Emit loading state
        emit(Resource.Loading())

        // Fetch from remote and update cache
        try {
            val remoteTransactions = remoteDataSource.fetchTransactions()
            withContext(Dispatchers.IO) {
                localDataSource.insertTransactions(remoteTransactions)
            }
            emit(Resource.Success(remoteTransactions))
        } catch (e: Exception) {
            // If remote fails, try to return cached data
            try {
                val cachedTransactions = localDataSource.getTransactions(cardId, limit).first()
                if (cachedTransactions.isNotEmpty()) {
                    emit(Resource.Success(cachedTransactions))
                } else {
                    emit(Resource.Error(e.message ?: "An error occurred"))
                }
            } catch (cacheException: Exception) {
                emit(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }

    override fun getUser(): Flow<Resource<UserModel?>> = flow {
        // First, try to get from cache
        try {
            val cachedUser = localDataSource.getUser().first()
            if (cachedUser != null) {
                emit(Resource.Success(cachedUser))
                return@flow
            }
        } catch (e: Exception) {
            // Continue to fetch from remote
        }

        // Emit loading state
        emit(Resource.Loading())

        // Fetch from remote and update cache
        try {
            val remoteUser = remoteDataSource.fetchUser()
            withContext(Dispatchers.IO) {
                localDataSource.insertUser(remoteUser)
            }
            emit(Resource.Success(remoteUser))
        } catch (e: Exception) {
            // If remote fails, try to return cached data
            try {
                val cachedUser = localDataSource.getUser().first()
                if (cachedUser != null) {
                    emit(Resource.Success(cachedUser))
                } else {
                    emit(Resource.Error(e.message ?: "An error occurred"))
                }
            } catch (cacheException: Exception) {
                emit(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }

    override suspend fun refreshAll(): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val cardsDomain = when (val cardsRes = remoteDataSource.fetchCards()) {
                is Resource.Success -> cardsRes.data ?: emptyList()
                is Resource.Error -> throw IllegalStateException(cardsRes.message ?: "Failed to fetch cards")
                is Resource.Loading -> emptyList()
            }

            val transactionsDomain = remoteDataSource.fetchTransactions()
            val userDomain = remoteDataSource.fetchUser()

            localDataSource.insertUser(userDomain)
            localDataSource.insertCards(cardsDomain)
            localDataSource.insertTransactions(transactionsDomain)
            Resource.Success(Unit)
        } catch (e: Exception) {
            // Fallback to cached data if present
            try {
                val cachedCards = localDataSource.getCards().first()
                val hasCache = cachedCards.isNotEmpty()
                if (hasCache) {
                    Resource.Success(Unit)
                } else {
                    Resource.Error(message = e.localizedMessage ?: "Failed to refresh", data = null)
                }
            } catch (cacheException: Exception) {
                Resource.Error(message = e.localizedMessage ?: "Failed to refresh", data = null)
            }
        }
    }

    override suspend fun toggleBlock(cardId: String) = withContext(Dispatchers.IO) {
        val current = localDataSource.getStatus(cardId) ?: return@withContext
        val next = if (current.uppercase() == "ACTIVE") "BLOCKED" else "ACTIVE"
        localDataSource.updateStatus(cardId, next)
    }
}

