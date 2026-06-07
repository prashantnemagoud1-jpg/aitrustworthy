package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.Screen
import com.example.viewmodel.TrustViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        setContent {
            MyApplicationTheme {
                val viewModel: TrustViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
                
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (val screen = currentScreen) {
                            is Screen.Landing -> {
                                LandingScreen(viewModel = viewModel)
                            }
                            is Screen.Login -> {
                                AuthScreen(viewModel = viewModel, mode = "login")
                            }
                            is Screen.SignUp -> {
                                AuthScreen(viewModel = viewModel, mode = "signup")
                            }
                            is Screen.ForgotPassword -> {
                                AuthScreen(viewModel = viewModel, mode = "forgot")
                            }
                            is Screen.Dashboard,
                            is Screen.ScamMessage,
                            is Screen.JobOffer,
                            is Screen.InvestmentScam,
                            is Screen.WebsiteRisk,
                            is Screen.DocumentFraud -> {
                                DashboardScreen(viewModel = viewModel, activeTab = screen)
                            }
                            is Screen.AdminPanel -> {
                                AdminScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
