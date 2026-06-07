package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.Screen
import com.example.viewmodel.TrustViewModel
import com.example.ui.theme.*

@Composable
fun AdminScreen(
    viewModel: TrustViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val totalUsers by viewModel.adminTotalUsers.collectAsState()
    val totalAnalyses by viewModel.adminMockAnalyses.collectAsState()
    val alertCount by viewModel.adminAlertCount.collectAsState()

    var systemOnline by remember { mutableStateOf(true) }
    var firewallStrengthened by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Return dashboard",
                        tint = CyberPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "SYSADMIN TELEMETRY",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = CyberPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = CyberPrimary,
            glowColor = CyberPrimary
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("COGNITIVE PLATFORM INFRASTRUCTURE", fontSize = 10.sp, color = CyberPrimary, fontWeight = FontWeight.Black)
                    Text("SECURE OPERATION SHELL", fontSize = 16.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                }
                TrustBadge(text = if (systemOnline) "ONLINE" else "SHUTDOWN", color = if (systemOnline) TrustGreen else TrustHigh)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Operational terminal tracks systemic user signups, API requests throughput, local SQLite sync pools, and background heuristics telemetry.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                GlassCard {
                    Text("MEMBER NODES", fontSize = 9.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    Text("$totalUsers", fontSize = 24.sp, color = TextPrimary, fontWeight = FontWeight.Black)
                    Text("SYS REGISTERED", fontSize = 8.sp, color = CyberPrimary, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier.weight(1f)) {
                GlassCard {
                    Text("SCANS EXECUTED", fontSize = 9.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    Text("$totalAnalyses", fontSize = 24.sp, color = TextPrimary, fontWeight = FontWeight.Black)
                    Text("SEC CHECKS", fontSize = 8.sp, color = CyberTertiary, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        GlassCard(modifier = Modifier.fillMaxWidth(), borderColor = TrustHigh) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(TrustHigh.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .border(1.dp, TrustHigh, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = TrustHigh, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("TOTAL ANOMALIES INTERCEPTED", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    Text("$alertCount ALARMS PREVENTED", fontSize = 14.sp, color = TrustHigh, fontWeight = FontWeight.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("SYSTEM TOGGLE COMMANDS", fontSize = 11.sp, color = CyberPrimary, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(10.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Platform Engine State", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Activate or pause master heuristics routing blocks", color = TextSecondary, fontSize = 10.sp)
                }
                Switch(
                    checked = systemOnline,
                    onCheckedChange = { systemOnline = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CyberPrimary,
                        checkedTrackColor = CyberPrimary.copy(alpha = 0.3f),
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = CyberBorder
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CyberBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Strict Firewall Core", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Deter unconfirmed domain extension queries", color = TextSecondary, fontSize = 10.sp)
                }
                Switch(
                    checked = firewallStrengthened,
                    onCheckedChange = { firewallStrengthened = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CyberTertiary,
                        checkedTrackColor = CyberTertiary.copy(alpha = 0.3f),
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = CyberBorder
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        CyberButton(
            onClick = {
                viewModel.clearHistory()
            },
            text = "FLUSH TELESCOPIC INCIDENT LOGS",
            containerColor = TrustHigh,
            textColor = Color.White,
            testTag = "purge_all_btn"
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}
