package com.chacha.dev.coop_test.data.repo

import com.chacha.dev.coop_test.data.local.dao.CardDao
import com.chacha.dev.coop_test.data.local.dao.TransactionDao
import com.chacha.dev.coop_test.data.local.dao.UserDao
import com.chacha.dev.coop_test.data.local.entity.CardEntity
import com.chacha.dev.coop_test.data.mapper.toDomain
import com.chacha.dev.coop_test.data.mapper.toEntity
import com.chacha.dev.coop_test.data.remote.api.NetworkApi
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.UserModel
import com.chacha.dev.coop_test.domain.repo.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepositoryImpl @Inject constructor(
    private val networkApi: NetworkApi,
    private val cardDao: CardDao,
    private val transactionDao: TransactionDao,
    private val userDao: UserDao
) : CardRepository {

    override fun observeCards(): Flow<List<CardModel>> =
        cardDao.observeCards().flatMapLatest { cards ->
            if (cards.isEmpty()) {
                flowOf(emptyList())
            } else {
                val flows = cards.map { cardEntity ->
                    cardDao.observeWallets(cardEntity.id).map { wallets ->
                        cardEntity.toDomain(wallets)
                    }
                }
                if (flows.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(flows) { it.toList() }
                }
            }
        }

    override fun observeCard(id: String): Flow<CardModel?> =
        cardDao.observeCardById(id).flatMapLatest { card ->
            if (card == null) {
                flowOf(null)
            } else {
                cardDao.observeWallets(card.id).map { wallets ->
                    card.toDomain(wallets)
                }
            }
        }

    override fun observeTransactions(cardId: String, limit: Int): Flow<List<TransactionModel>> =
        transactionDao.observeForCard(cardId, limit).map { list -> list.map { it.toDomain() } }

    override fun observeUser(): Flow<UserModel?> =
        userDao.observeUser().map { it?.toDomain() }

    override suspend fun refreshAll() = withContext(Dispatchers.IO) {
        val cardsDto = networkApi.getCards()
        val transactionsDto = networkApi.getTransactions()
        val userDto = networkApi.getUser().user

        userDao.upsert(userDto.toEntity())
        val cardEntities: List<CardEntity> = cardsDto.cards.map { it.toEntity() }
        cardDao.upsertCards(cardEntities)

        cardEntities.forEach { cardEntity ->
            val wallets = cardsDto.cards
                .firstOrNull { it.id == cardEntity.id }
                ?.wallets
                ?.map { it.toEntity(cardEntity.id) }
                .orEmpty()
            cardDao.deleteWallets(cardEntity.id)
            if (wallets.isNotEmpty()) {
                cardDao.upsertWallets(wallets)
            }
        }

        val transactionEntities = transactionsDto.transactions.map { it.toEntity() }
        transactionDao.upsert(transactionEntities)
    }

    override suspend fun toggleBlock(cardId: String) = withContext(Dispatchers.IO) {
        val current = cardDao.getStatus(cardId) ?: return@withContext
        val next = if (current.uppercase() == "ACTIVE") "BLOCKED" else "ACTIVE"
        cardDao.updateStatus(cardId, next)
    }
}

