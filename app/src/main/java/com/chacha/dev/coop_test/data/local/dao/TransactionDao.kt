package com.chacha.dev.coop_test.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chacha.dev.coop_test.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query(
        """
            SELECT * FROM transactions 
            WHERE cardId = :cardId 
            ORDER BY date DESC 
            LIMIT :limit
        """
    )
    fun observeForCard(cardId: String, limit: Int = 10): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(transactions: List<TransactionEntity>)
}

