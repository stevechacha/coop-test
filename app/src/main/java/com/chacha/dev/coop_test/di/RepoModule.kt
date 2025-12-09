package com.chacha.dev.coop_test.di

import com.chacha.dev.coop_test.data.repo.CardRepositoryImpl
import com.chacha.dev.coop_test.domain.repo.CardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun bindCardRepository(impl: CardRepositoryImpl): CardRepository
}