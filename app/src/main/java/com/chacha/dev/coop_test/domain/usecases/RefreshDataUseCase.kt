package com.chacha.dev.coop_test.domain.usecases

import com.chacha.dev.coop_test.domain.repo.CardRepository
import javax.inject.Inject

class RefreshDataUseCase @Inject constructor(
    private val repository: CardRepository
) {
    suspend operator fun invoke() = repository.refreshAll()
}

