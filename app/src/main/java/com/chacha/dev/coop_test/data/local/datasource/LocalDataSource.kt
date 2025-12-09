package com.chacha.dev.coop_test.data.local.datasource

import com.chacha.dev.coop_test.data.local.dao.CardDao
import com.chacha.dev.coop_test.data.local.dao.TransactionDao
import com.chacha.dev.coop_test.data.local.dao.UserDao
import com.chacha.dev.coop_test.data.mapper.toDomain
import com.chacha.dev.coop_test.data.mapper.toEntity
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.UserModel
import com.chacha.dev.coop_test.domain.model.WalletModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

interface LocalDataSource {
    fun getCards(): Flow<List<CardModel>>
    fun getCard(id: String): Flow<CardModel?>
    fun getTransactions(cardId: String, limit: Int): Flow<List<TransactionModel>>
    fun getUser(): Flow<UserModel?>

    suspend fun insertCards(cards: List<CardModel>)
    suspend fun insertUser(user: UserModel)
    suspend fun insertTransactions(transactions: List<TransactionModel>)
    suspend fun deleteWallets(cardId: String)

    suspend fun getStatus(cardId: String): String?
    suspend fun updateStatus(cardId: String, status: String)
}

class LocalDataSourceImpl @Inject constructor(
    private val cardDao: CardDao,
    private val transactionDao: TransactionDao,
    private val userDao: UserDao
) : LocalDataSource {
    override fun getCards(): Flow<List<CardModel>> {
        return cardDao.observeCards().flatMapLatest { entities ->
            if (entities.isEmpty()) {
                flowOf(emptyList())
            } else {
                val flows = entities.map { entity ->
                    cardDao.observeWallets(entity.id).map { wallets ->
                        entity.toDomain(wallets)
                    }
                }
                combine(flows) { it.toList() }
            }
        }
    }

    override fun getCard(id: String): Flow<CardModel?> {
        return cardDao.observeCardById(id).flatMapLatest { entity ->
            if (entity == null) {
                flowOf(null)
            } else {
                cardDao.observeWallets(id).map { wallets ->
                    entity.toDomain(wallets)
                }
            }
        }
    }

    override fun getTransactions(cardId: String, limit: Int): Flow<List<TransactionModel>> {
        return transactionDao.observeForCard(cardId, limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getUser(): Flow<UserModel?> {
        return userDao.observeUser().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun insertCards(cards: List<CardModel>) {
        val cardEntities = cards.map { it.toEntity() }
        cardDao.upsertCards(cardEntities)

        cards.forEach { card ->
            cardDao.deleteWallets(card.id)
            if (card.wallets.isNotEmpty()) {
                val walletEntities = card.wallets.map { it.toEntity(card.id) }
                cardDao.upsertWallets(walletEntities)
            }
        }
    }

    override suspend fun insertUser(user: UserModel) {
        userDao.upsert(user.toEntity())
    }

    override suspend fun insertTransactions(transactions: List<TransactionModel>) {
        val transactionEntities = transactions.map { it.toEntity() }
        transactionDao.upsert(transactionEntities)
    }

    override suspend fun deleteWallets(cardId: String) {
        cardDao.deleteWallets(cardId)
    }

    override suspend fun getStatus(cardId: String): String? = cardDao.getStatus(cardId)

    override suspend fun updateStatus(cardId: String, status: String) {
        cardDao.updateStatus(cardId, status)
    }
}

