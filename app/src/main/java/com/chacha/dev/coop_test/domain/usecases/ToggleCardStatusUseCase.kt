package com.chacha.dev.coop_test.domain.usecases

import com.chacha.dev.coop_test.domain.repo.CardRepository
import javax.inject.Inject

class ToggleCardStatusUseCase @Inject constructor(
    private val repository: CardRepository
) {
    suspend operator fun invoke(cardId: String) = repository.toggleBlock(cardId)
}

