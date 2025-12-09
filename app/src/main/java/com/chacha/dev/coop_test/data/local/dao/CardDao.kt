package com.chacha.dev.coop_test.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.chacha.dev.coop_test.data.local.entity.CardEntity
import com.chacha.dev.coop_test.data.local.entity.WalletEntity
import kotlinx.coroutines.flow.Flow

data class CardWithWallets(
    val card: CardEntity,
    val wallets: List<WalletEntity>
)

@Dao
interface CardDao {
    @Transaction
    @Query("SELECT * FROM cards")
    fun observeCards(): Flow<List<CardEntity>>

    @Transaction
    @Query("SELECT * FROM cards WHERE id = :id LIMIT 1")
    fun observeCardById(id: String): Flow<CardEntity?>

    @Query("SELECT * FROM wallets WHERE cardId = :cardId")
    fun observeWallets(cardId: String): Flow<List<WalletEntity>>

    @Query("SELECT status FROM cards WHERE id = :cardId LIMIT 1")
    suspend fun getStatus(cardId: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCards(cards: List<CardEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWallets(wallets: List<WalletEntity>)

    @Query("DELETE FROM wallets WHERE cardId = :cardId")
    suspend fun deleteWallets(cardId: String)

    @Query("UPDATE cards SET status = :status WHERE id = :cardId")
    suspend fun updateStatus(cardId: String, status: String)
}

