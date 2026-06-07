package com.example.ui

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.Screen
import com.example.viewmodel.TrustViewModel
import com.example.ui.theme.*

@Composable
fun LandingScreen(
    viewModel: TrustViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CyberSurface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Shield logo",
                    tint = CyberPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "AI TRUSTSHIELD",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "ZERO-TRUST CYBERSECURITY",
                        color = CyberPrimary,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Row {
                TextButton(onClick = { viewModel.navigateTo(Screen.Login) }) {
                    Text("SIGN IN", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Black)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Button(
                    onClick = { viewModel.navigateTo(Screen.SignUp) },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("ENROLL NOW", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                }
            }
        }
        HorizontalDivider(color = CyberBorder, thickness = 1.dp)

        Column(modifier = Modifier.padding(16.dp)) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = CyberPrimary,
                glowColor = CyberPrimary
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberTertiary.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "MILITARY-GRADE COGNITIVE SYSTEM",
                        color = CyberTertiary,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "DEFEND YOUR LIFE\nFROM SMART SCAMS.",
                    fontSize = 26.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Black,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Detect SMS phishes, fake recruiter job loops, malicious URLs, Ponzi traps, and deceptive contracts with advanced AI models.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                CyberButton(
                    onClick = { viewModel.navigateTo(Screen.SignUp) },
                    text = "INITIALIZE SHIELD SCANNER",
                    containerColor = CyberTertiary,
                    icon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp)) },
                    testTag = "start_defense_button"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberSurface)
                    .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(TrustHigh.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("LIVE FEED", color = TrustHigh, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Suspicious PayPal redirect detected on secure-portal-upgrade.icu! Rating: HIGH",
                    fontSize = 11.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "THE ESCALATING FRAUD THREAT",
                fontSize = 11.sp,
                color = CyberPrimary,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Financial scams have expanded into complex systems. Criminals combine deep local lookups, spoofed domains, and social pressure to trick people. Relying on basic intuition leaves profiles exposed. AI TrustShield is built to trace and neutralize scams immediately.",
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "SYSTEM CENTRAL PROTECTIVE COGNITIONS",
                fontSize = 11.sp,
                color = CyberPrimary,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            FeatureItem(
                title = "💬 SMS / WhatsApp Scam Analyzer",
                desc = "Evaluates incoming suspicious messages, money prize awards, or login warnings for credential phishes.",
                color = CyberPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            FeatureItem(
                title = "💼 Job Offer Validator",
                desc = "Identifies fake remote coordinate positions asking for upfront processing fees or unverified Gmail handlers.",
                color = CyberAccent
            )
            Spacer(modifier = Modifier.height(10.dp))
            FeatureItem(
                title = "📈 Investment Scheme Evaluator",
                desc = "Tracks unbacked high daily compound interest plans or multi-tiered referral network loops.",
                color = CyberTertiary
            )
            Spacer(modifier = Modifier.height(10.dp))
            FeatureItem(
                title = "🌐 Website Phish Assessor",
                desc = "Analyzes web domain URLs to expose hidden character swaps, fake sub-paths, and brand theft.",
                color = CyberSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))
            FeatureItem(
                title = "📄 Document Signature Scanner",
                desc = "Scans legal papers and transaction contract drafts to pinpoint hidden liability traps.",
                color = CyberAccent
            )

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "COMMUNITY REVIEWS",
                fontSize = 11.sp,
                color = CyberPrimary,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = CyberTertiary, modifier = Modifier.size(14.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"TrustShield discovered a fake email pretending to recruit writers for \$1,000/week that requested a \$120 onboarding kit. That saved my savings!\"",
                    fontSize = 12.sp,
                    color = TextPrimary,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "— Donald R., Security Analyst",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "PRICING CONSOLES",
                fontSize = 11.sp,
                color = CyberPrimary,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    GlassCard(modifier = Modifier.fillMaxHeight()) {
                        Text("BASIC SHIELD", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                        Text("$0 /mo", fontSize = 20.sp, color = TextPrimary, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Standard AI heuristics to scan 5 localized targets daily in SQLite registers.", fontSize = 10.sp, color = TextSecondary, lineHeight = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.weight(1f)) {
                    GlassCard(modifier = Modifier.fillMaxHeight()) {
                        Text("CORE SAAS", fontSize = 10.sp, color = CyberPrimary, fontWeight = FontWeight.Bold)
                        Text("$49 /mo", fontSize = 20.sp, color = TextPrimary, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Unlimited models, deep signature heuristics checks, and PDF report downloads.", fontSize = 10.sp, color = TextSecondary, lineHeight = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = CyberTertiary,
                glowColor = CyberTertiary
            ) {
                Text(
                    text = "LAUNCH TOTAL TELEMETRY PROTECTION",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Equip continuous shielding structures today. Analyze targets completely offline or connected to secure API gates.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                CyberButton(
                    onClick = { viewModel.navigateTo(Screen.SignUp) },
                    text = "SECURE PROFILE SETUP NOW",
                    containerColor = CyberTertiary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun FeatureItem(
    title: String,
    desc: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CyberSurface)
            .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text(text = desc, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)
        }
    }
}
