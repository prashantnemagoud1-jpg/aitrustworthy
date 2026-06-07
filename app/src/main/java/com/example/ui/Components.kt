package com.example.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberBorder,
    glowColor: Color = Color.Transparent,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CyberSurface)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .drawBehind {
                if (glowColor != Color.Transparent) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(glowColor.copy(alpha = 0.08f), Color.Transparent),
                            center = Offset(size.width / 2, size.height),
                            radius = size.width * 0.7f
                        ),
                        center = Offset(size.width / 2, size.height),
                        radius = size.width * 0.7f
                    )
                }
            }
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun CyberButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = CyberPrimary,
    textColor: Color = Color.Black,
    icon: @Composable (() -> Unit)? = null,
    testTag: String = ""
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .testTag(testTag),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun TrustBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = CyberTertiary
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
            Text(
                text = text.uppercase(),
                color = color,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun ThreatIndicatorGauge(
    score: Int,
    size: Dp = 100.dp
) {
    val animatedPercent by animateFloatAsState(
        targetValue = score / 100f,
        animationSpec = tween(1200),
        label = "gauge"
    )

    val color = when {
        score < 30 -> TrustGreen
        score < 60 -> TrustMedium
        else -> TrustHigh
    }

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 8.dp.toPx()
            
            drawArc(
                color = CyberBorder.copy(alpha = 0.3f),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = color,
                startAngle = 135f,
                sweepAngle = animatedPercent * 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${(animatedPercent * 100).toInt()}%",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = when {
                    score < 30 -> "SAFE"
                    score < 60 -> "WARN"
                    else -> "RISK"
                },
                color = color,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
        }
    }
}
