package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.TrustHistory
import com.example.api.GeminiClient
import com.example.api.TrustAnalysisResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Landing : Screen()
    object Login : Screen()
    object SignUp : Screen()
    object ForgotPassword : Screen()
    object Dashboard : Screen()
    object ScamMessage : Screen()
    object JobOffer : Screen()
    object InvestmentScam : Screen()
    object WebsiteRisk : Screen()
    object DocumentFraud : Screen()
    object AdminPanel : Screen()
}

data class UserProfile(
    val name: String,
    val email: String
)

class TrustViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val trustDao = database.trustDao()

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Landing)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    val activeInputText = MutableStateFlow("")

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analysisResult = MutableStateFlow<TrustAnalysisResult?>(null)
    val analysisResult: StateFlow<TrustAnalysisResult?> = _analysisResult.asStateFlow()

    val allHistory: StateFlow<List<TrustHistory>> = trustDao.getAllHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val securityScore: Flow<Int> = allHistory.map { historyList ->
        if (historyList.isEmpty()) 100
        else {
            val highRiskCount = historyList.count { it.riskScore >= 50 }
            val deduction = highRiskCount * 8
            (100 - deduction).coerceIn(10, 100)
        }
    }

    val totalScans: Flow<Int> = allHistory.map { historyList ->
        historyList.size + 128
    }

    val threatsDetected: Flow<Int> = allHistory.map { historyList ->
        historyList.count { it.riskScore >= 50 } + 34
    }

    val threatDistribution: Flow<Map<String, Float>> = allHistory.map { historyList ->
        val lowCount = historyList.count { it.riskScore < 30 } + 120
        val medCount = historyList.count { it.riskScore in 30..50 } + 42
        val highCount = historyList.count { it.riskScore > 50 } + 34
        val sum = (lowCount + medCount + highCount).toFloat()
        if (sum == 0f) mapOf("Low" to 1f, "Medium" to 0f, "High" to 0f)
        else mapOf(
            "Low" to (lowCount / sum),
            "Medium" to (medCount / sum),
            "High" to (highCount / sum)
        )
    }

    private val _adminTotalUsers = MutableStateFlow(248)
    val adminTotalUsers: StateFlow<Int> = _adminTotalUsers.asStateFlow()

    val adminMockAnalyses: StateFlow<Int> = allHistory.map { it.size + 1284 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1284)

    val adminAlertCount: StateFlow<Int> = allHistory.map { it.count { item -> item.riskScore >= 50 } + 88 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 88)

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
        activeInputText.value = ""
        _analysisResult.value = null
    }

    fun login(email: String) {
        viewModelScope.launch {
            _currentUser.value = UserProfile(
                name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = email
            )
            navigateTo(Screen.Dashboard)
        }
    }

    fun signUp(name: String, email: String) {
        viewModelScope.launch {
            _currentUser.value = UserProfile(name = name, email = email)
            navigateTo(Screen.Dashboard)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _currentUser.value = null
            navigateTo(Screen.Landing)
        }
    }

    fun executeAnalysis(type: String, input: String) {
        if (input.isBlank()) return
        viewModelScope.launch {
            _isAnalyzing.value = true
            _analysisResult.value = null
            try {
                val apiOutput = GeminiClient.analyzeText(type, input)
                _analysisResult.value = apiOutput

                val history = TrustHistory(
                    analysisType = type,
                    inputText = input,
                    riskScore = apiOutput.riskScore,
                    threatLevel = apiOutput.threatLevel,
                    explanation = apiOutput.explanation,
                    redFlagsRaw = apiOutput.redFlags.joinToString("\n"),
                    recommendedActionsRaw = apiOutput.recommendedActions.joinToString("\n"),
                    finalVerdict = apiOutput.finalVerdict
                )
                trustDao.insertHistory(history)
            } catch (e: Exception) {
                val fallback = TrustAnalysisResult(
                    riskScore = 50,
                    threatLevel = "MEDIUM",
                    explanation = "An error occurred during analysis scan: ${e.localizedMessage}",
                    redFlags = listOf("Failed to contact Gemini engine. Run network trace checks."),
                    recommendedActions = listOf("Try executing scanner again later."),
                    finalVerdict = "CONGESTION_ALERT"
                )
                _analysisResult.value = fallback
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun deleteHistory(id: Long) {
        viewModelScope.launch {
            trustDao.deleteHistoryById(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            trustDao.clearAllHistory()
            _analysisResult.value = null
        }
    }
}
