package com.example.data

import kotlinx.coroutines.flow.Flow

class TrustRepository(private val trustDao: TrustDao) {
    val allHistory: Flow<List<TrustHistory>> = trustDao.getAllHistory()

    suspend fun insert(history: TrustHistory) {
        trustDao.insertHistory(history)
    }

    suspend fun deleteById(id: Long) {
        trustDao.deleteHistoryById(id)
    }

    suspend fun clearAll() {
        trustDao.clearAllHistory()
    }
}
