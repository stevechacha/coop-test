package com.chacha.dev.coop_test.domain.usecases

import com.chacha.dev.coop_test.domain.common.Resource
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.repo.CardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCardsUseCase @Inject constructor(
    private val repository: CardRepository
) {
    operator fun invoke(): Flow<Resource<List<CardModel>>> = repository.getCards()
}

