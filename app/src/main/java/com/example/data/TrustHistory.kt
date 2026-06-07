package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trust_history")
data class TrustHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val analysisType: String,
    val inputText: String,
    val riskScore: Int,
    val threatLevel: String,
    val explanation: String,
    val redFlagsRaw: String, // Newline separated red flags
    val recommendedActionsRaw: String, // Newline separated guidelines
    val finalVerdict: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun getRedFlagsList(): List<String> {
        return if (redFlagsRaw.isBlank()) emptyList() else redFlagsRaw.split("\n").filter { it.isNotBlank() }
    }

    fun getRecommendedActionsList(): List<String> {
        return if (recommendedActionsRaw.isBlank()) emptyList() else recommendedActionsRaw.split("\n").filter { it.isNotBlank() }
    }
}
