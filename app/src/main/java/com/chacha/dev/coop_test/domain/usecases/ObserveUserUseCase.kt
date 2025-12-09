package com.chacha.dev.coop_test.domain.usecases

import com.chacha.dev.coop_test.domain.common.Resource
import com.chacha.dev.coop_test.domain.model.UserModel
import com.chacha.dev.coop_test.domain.repo.CardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserUseCase @Inject constructor(
    private val repository: CardRepository
) {
    operator fun invoke(): Flow<Resource<UserModel?>> = repository.getUser()
}

