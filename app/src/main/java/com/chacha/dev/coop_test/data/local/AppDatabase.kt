package com.chacha.dev.coop_test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chacha.dev.coop_test.data.local.dao.CardDao
import com.chacha.dev.coop_test.data.local.dao.TransactionDao
import com.chacha.dev.coop_test.data.local.dao.UserDao
import com.chacha.dev.coop_test.data.local.entity.CardEntity
import com.chacha.dev.coop_test.data.local.entity.TransactionEntity
import com.chacha.dev.coop_test.data.local.entity.UserEntity
import com.chacha.dev.coop_test.data.local.entity.WalletEntity

@Database(
    entities = [
        CardEntity::class,
        WalletEntity::class,
        TransactionEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun transactionDao(): TransactionDao
    abstract fun userDao(): UserDao
}

