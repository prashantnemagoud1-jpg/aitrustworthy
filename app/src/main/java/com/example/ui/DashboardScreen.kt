package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.Screen
import com.example.viewmodel.TrustViewModel
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: TrustViewModel,
    activeTab: Screen,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val currentUser by viewModel.currentUser.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val analysisResult by viewModel.analysisResult.collectAsState()
    val historyList by viewModel.allHistory.collectAsState()

    val securityIndex by viewModel.securityScore.collectAsState(initial = 100)
    val totalScansCount by viewModel.totalScans.collectAsState(initial = 128)
    val totalThreatsCount by viewModel.threatsDetected.collectAsState(initial = 34)
    val threatDistributionPercent by viewModel.threatDistribution.collectAsState(initial = emptyMap())

    val activeInput by viewModel.activeInputText.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CyberSurface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { viewModel.navigateTo(Screen.Landing) }
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = CyberPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI TRUSTSHIELD",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = currentUser?.name?.uppercase() ?: "OPERATOR",
                    color = CyberPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(end = 12.dp)
                )
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.AdminPanel) },
                    modifier = Modifier.testTag("admin_nav_button")
                ) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Admin telemetry link", tint = TextSecondary, modifier = Modifier.size(16.dp))
                }
                IconButton(onClick = { viewModel.logout() }) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Sign out link", tint = TextSecondary, modifier = Modifier.size(16.dp))
                }
            }
        }
        HorizontalDivider(color = CyberBorder, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TabSelectorPill(
                title = "DASHBOARD",
                selected = activeTab == Screen.Dashboard,
                onClick = { viewModel.navigateTo(Screen.Dashboard) }
            )
            TabSelectorPill(
                title = "💬 ANALYZE MESSAGE",
                selected = activeTab == Screen.ScamMessage,
                onClick = { viewModel.navigateTo(Screen.ScamMessage) }
            )
            TabSelectorPill(
                title = "💼 VET JOBS",
                selected = activeTab == Screen.JobOffer,
                onClick = { viewModel.navigateTo(Screen.JobOffer) }
            )
            TabSelectorPill(
                title = "📈 INVESTMENTS",
                selected = activeTab == Screen.InvestmentScam,
                onClick = { viewModel.navigateTo(Screen.InvestmentScam) }
            )
            TabSelectorPill(
                title = "🌐 DOMAIN RISK",
                selected = activeTab == Screen.WebsiteRisk,
                onClick = { viewModel.navigateTo(Screen.WebsiteRisk) }
            )
            TabSelectorPill(
                title = "📄 LEGAL SIGNATURES",
                selected = activeTab == Screen.DocumentFraud,
                onClick = { viewModel.navigateTo(Screen.DocumentFraud) }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            when (activeTab) {
                is Screen.Dashboard -> {
                    Text(
                        text = "CONTINUOUS THREAT OVERWATCH SYSTEM",
                        color = CyberPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(modifier = Modifier.weight(1.2f)) {
                            GlassCard(
                                modifier = Modifier.fillMaxWidth().height(160.dp),
                                borderColor = CyberPrimary,
                                glowColor = CyberPrimary
                            ) {
                                Text("OVERALL SECURITY LEVEL", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(14.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ThreatIndicatorGauge(score = securityIndex, size = 80.dp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("STATUS", fontSize = 8.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = if (securityIndex >= 80) "OPTIMAL" else if (securityIndex >= 50) "DEGRADED" else "BREACH RISK",
                                            color = if (securityIndex >= 80) TrustGreen else if (securityIndex >= 50) TrustMedium else TrustHigh,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                        Text("All sectors monitored.", fontSize = 9.sp, color = TextMuted)
                                    }
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(0.8f)) {
                            GlassCard(modifier = Modifier.fillMaxWidth().height(74.dp)) {
                                Text("TOTAL COUNTERS", fontSize = 8.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                Text("$totalScansCount SCANS", fontSize = 16.sp, color = TextPrimary, fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            GlassCard(modifier = Modifier.fillMaxWidth().height(74.dp), borderColor = TrustHigh) {
                                Text("THREAT CORES BLOCKED", fontSize = 8.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                Text("$totalThreatsCount ALERTS", fontSize = 16.sp, color = TrustHigh, fontWeight = FontWeight.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Text("RISK DISTRIBUTION ASSESSMENT", fontSize = 10.sp, color = CyberPrimary, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        val lowP = threatDistributionPercent["Low"] ?: 0.6f
                        val medP = threatDistributionPercent["Medium"] ?: 0.25f
                        val highP = threatDistributionPercent["High"] ?: 0.15f

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            DistributionLineTracker("Low Threat Levels (Secure/Verified)", lowP, TrustGreen)
                            DistributionLineTracker("Medium Alert Indicators (Suspicious)", medP, TrustMedium)
                            DistributionLineTracker("Critical Risk Profiles (Immediate Deterrent)", highP, TrustHigh)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "HISTO-TELEMETRY LOGS (${historyList.size})",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        if (historyList.isNotEmpty()) {
                            TextButton(onClick = { viewModel.clearHistory() }) {
                                Text("PURGE DATABASE", color = TrustHigh, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    if (historyList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberSurface)
                                .border(1.dp, CyberBorder, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "NO RECORDED COGNITIVE LOGS AVAILABLE. START TARGET ASSESSMENTS.",
                                color = TextMuted,
                                fontSize = 10.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            historyList.forEach { history ->
                                HistoryRowLog(
                                    history = history,
                                    onDelete = { viewModel.deleteHistory(history.id) }
                                )
                            }
                        }
                    }
                }

                else -> {
                    val scannerTitle = when (activeTab) {
                        is Screen.ScamMessage -> "SMS & SOCIAL DECEPTIONS TELEMETRY"
                        is Screen.JobOffer -> "FAKE RECRUITMENT AND JOB LOOP VALIDATOR"
                        is Screen.InvestmentScam -> "PONZI SCHEME INTEGRATED INVESTIGATOR"
                        is Screen.WebsiteRisk -> "MALICIOUS DOMAIN URL ASSESSOR"
                        is Screen.DocumentFraud -> "CONTRACT SIGNATURE TRAP SCANNER"
                        else -> "DECEPTIVE TARGET TELEMETRY ASSESSOR"
                    }

                    val scannerPlaceholder = when (activeTab) {
                        is Screen.ScamMessage -> "Paste WhatsApp text, message content, urgent alerts, etc..."
                        is Screen.JobOffer -> "Input job description details, onboarding fees, coordinate managers info..."
                        is Screen.InvestmentScam -> "Paste guaranteed money multipliers, yield claims, referral plans..."
                        is Screen.WebsiteRisk -> "Enter target domain URLs (e.g. www.secure-paypal-recovery.icu)..."
                        is Screen.DocumentFraud -> "Paste escrow terms, liability statements, wire transfer specifications..."
                        else -> "Enter target contents here..."
                    }

                    val categoryName = when (activeTab) {
                        is Screen.ScamMessage -> "Scam Message"
                        is Screen.JobOffer -> "Job Offer"
                        is Screen.InvestmentScam -> "Investment Scheme"
                        is Screen.WebsiteRisk -> "Website Risk"
                        is Screen.DocumentFraud -> "Document Analysis"
                        else -> "Deceptive Target"
                    }

                    Text(
                        text = scannerTitle,
                        color = CyberPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = activeInput,
                        onValueChange = { viewModel.activeInputText.value = it },
                        label = { Text("EVALUATION TARGET MATRIX", color = TextSecondary) },
                        placeholder = { Text(scannerPlaceholder, color = TextMuted, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberPrimary,
                            unfocusedBorderColor = CyberBorder,
                            focusedContainerColor = CyberSurface,
                            unfocusedContainerColor = CyberSurface,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .testTag("scanner_input_field")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CyberButton(
                            onClick = {
                                viewModel.executeAnalysis(categoryName, activeInput)
                            },
                            text = if (isAnalyzing) "DISSECTING ENGINES OUTLET..." else "EXECUTE CYBER AUDIT SCAN",
                            containerColor = if (isAnalyzing) CyberSurfaceVariant else CyberTertiary,
                            modifier = Modifier.weight(1f),
                            icon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp)) },
                            testTag = "execute_scan_button"
                        )

                        CyberButton(
                            onClick = { viewModel.activeInputText.value = "" },
                            text = "FLUSH INPUT",
                            containerColor = CyberBorder,
                            textColor = TextPrimary,
                            modifier = Modifier.width(100.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(
                        visible = isAnalyzing,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberSurface)
                                .border(1.dp, CyberPrimary, RoundedCornerShape(8.dp))
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = CyberPrimary, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "TRACE ROUTING DECEPTION CHECKS... PROBABILITY SCANS IN ACTION",
                                fontSize = 11.sp,
                                color = CyberPrimary,
                                fontWeight = FontWeight.Bold,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = analysisResult != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        analysisResult?.let { result ->
                            Column {
                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "EVALUATION TELEMETRY REPORT",
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                GlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    borderColor = when {
                                        result.riskScore < 30 -> TrustGreen
                                        result.riskScore < 60 -> TrustMedium
                                        else -> TrustHigh
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = result.finalVerdict.uppercase(),
                                                color = when {
                                                    result.riskScore < 30 -> TrustGreen
                                                    result.riskScore < 60 -> TrustMedium
                                                    else -> TrustHigh
                                                },
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                            Text("CYBER AUDIT VERDICT", fontSize = 8.sp, color = TextSecondary)
                                        }
                                        ThreatIndicatorGauge(score = result.riskScore, size = 64.dp)
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))
                                    HorizontalDivider(color = CyberBorder, thickness = 1.dp)
                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text("ASSESSMENT NARRATIVE", fontSize = 9.sp, color = CyberPrimary, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = result.explanation,
                                        color = TextPrimary,
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))
                                    HorizontalDivider(color = CyberBorder, thickness = 1.dp)
                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text("DETECTED EXPOSED RED FLAGS", fontSize = 9.sp, color = TrustHigh, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
                                    Spacer(modifier = Modifier.height(6.dp))

                                    result.redFlags.forEach { alarmLine ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Text("•", color = TrustHigh, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 6.dp))
                                            Text(text = alarmLine, color = TextPrimary, fontSize = 11.sp, lineHeight = 15.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))
                                    HorizontalDivider(color = CyberBorder, thickness = 1.dp)
                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text("RECOMMENDED DIRECT DEFENSIVE CONTROL ACTIONS", fontSize = 9.sp, color = TrustGreen, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
                                    Spacer(modifier = Modifier.height(6.dp))

                                    result.recommendedActions.forEach { actionLine ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = TrustGreen,
                                                modifier = Modifier.size(12.dp).padding(top = 2.dp, end = 4.dp)
                                            )
                                            Text(text = actionLine, color = TextSecondary, fontSize = 11.sp, lineHeight = 15.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun TabSelectorPill(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) CyberPrimary.copy(alpha = 0.15f) else CyberSurface)
            .border(
                width = 1.dp,
                color = if (selected) CyberPrimary else CyberBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (selected) CyberPrimary else TextSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun DistributionLineTracker(
    label: String,
    percentage: Float,
    barColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = TextSecondary, fontSize = 10.sp)
            Text("${(percentage * 100).toInt()}%", color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(CyberBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = percentage)
                    .clip(RoundedCornerShape(3.dp))
                    .background(barColor)
            )
        }
    }
}

@Composable
fun HistoryRowLog(
    history: com.example.data.TrustHistory,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val indicatorColor = when {
        history.riskScore < 30 -> TrustGreen
        history.riskScore < 60 -> TrustMedium
        else -> TrustHigh
    }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(indicatorColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = history.analysisType.uppercase(), color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Black)
                    Text(
                        text = if (history.inputText.length > 32) "${history.inputText.take(32)}..." else history.inputText,
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(indicatorColor.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("${history.riskScore}%", color = indicatorColor, fontSize = 9.sp, fontWeight = FontWeight.Black)
                }
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete item", tint = TrustHigh, modifier = Modifier.size(14.dp))
                }
            }
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = CyberBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Text("VERDICT DETECTOR", fontSize = 8.sp, color = CyberPrimary, fontWeight = FontWeight.Bold)
            Text(history.finalVerdict.uppercase(), fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Black)

            Spacer(modifier = Modifier.height(8.dp))

            Text("ANALYSIS DETAIL SUMMARY", fontSize = 8.sp, color = CyberPrimary, fontWeight = FontWeight.Bold)
            Text(history.explanation, fontSize = 10.sp, color = TextSecondary, lineHeight = 14.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text("EXPOSED SCAM INDICATORS", fontSize = 8.sp, color = TrustHigh, fontWeight = FontWeight.Bold)
            history.getRedFlagsList().forEach { flag ->
                Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(bottom = 2.dp)) {
                    Text("•", color = TrustHigh, fontSize = 10.sp, modifier = Modifier.padding(end = 4.dp))
                    Text(flag, color = TextPrimary, fontSize = 10.sp, lineHeight = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("MITIGATION DEFENSIVE STEPS", fontSize = 8.sp, color = TrustGreen, fontWeight = FontWeight.Bold)
            history.getRecommendedActionsList().forEach { action ->
                Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(bottom = 2.dp)) {
                    Text("✓", color = TrustGreen, fontSize = 10.sp, modifier = Modifier.padding(end = 4.dp))
                    Text(action, color = TextSecondary, fontSize = 10.sp, lineHeight = 13.sp)
                }
            }
        }
    }
}
