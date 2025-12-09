package com.chacha.dev.coop_test.di

import com.chacha.dev.coop_test.domain.repo.CardRepository
import com.chacha.dev.coop_test.domain.usecases.ObserveCardUseCase
import com.chacha.dev.coop_test.domain.usecases.ObserveCardsUseCase
import com.chacha.dev.coop_test.domain.usecases.ObserveTransactionsUseCase
import com.chacha.dev.coop_test.domain.usecases.ObserveUserUseCase
import com.chacha.dev.coop_test.domain.usecases.RefreshDataUseCase
import com.chacha.dev.coop_test.domain.usecases.ToggleCardStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideObserveCards(repository: CardRepository) = ObserveCardsUseCase(repository)

    @Provides
    fun provideObserveCard(repository: CardRepository) = ObserveCardUseCase(repository)

    @Provides
    fun provideTransactions(repository: CardRepository) = ObserveTransactionsUseCase(repository)

    @Provides
    fun provideUser(repository: CardRepository) = ObserveUserUseCase(repository)

    @Provides
    fun provideRefresh(repository: CardRepository) = RefreshDataUseCase(repository)

    @Provides
    fun provideToggleCard(repository: CardRepository) = ToggleCardStatusUseCase(repository)
}