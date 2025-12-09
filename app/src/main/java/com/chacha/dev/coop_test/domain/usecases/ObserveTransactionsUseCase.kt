package com.chacha.dev.coop_test.domain.usecases

import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.repo.CardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTransactionsUseCase @Inject constructor(
    private val repository: CardRepository
) {
    operator fun invoke(cardId: String, limit: Int = 10): Flow<List<TransactionModel>> =
        repository.observeTransactions(cardId, limit)
}

