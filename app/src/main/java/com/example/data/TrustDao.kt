package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TrustDao {
    @Query("SELECT * FROM trust_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<TrustHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: TrustHistory)

    @Query("DELETE FROM trust_history WHERE id = :id")
    suspend fun deleteHistoryById(id: Long)

    @Query("DELETE FROM trust_history")
    suspend fun clearAllHistory()
}
