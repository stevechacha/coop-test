package com.chacha.dev.coop_test.di

import android.content.Context
import androidx.room.Room
import com.chacha.dev.coop_test.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "coop-db").build()

    @Provides
    fun provideCardDao(db: AppDatabase) = db.cardDao()

    @Provides
    fun provideTransactionDao(db: AppDatabase) = db.transactionDao()

    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()
}