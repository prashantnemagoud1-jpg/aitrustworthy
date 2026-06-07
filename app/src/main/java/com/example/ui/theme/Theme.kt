package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CyberPrimary,
    onPrimary = Color(0xFF030712),
    secondary = CyberSecondary,
    onSecondary = Color.White,
    tertiary = CyberTertiary,
    onTertiary = Color(0xFF030712),
    background = CyberBackground,
    onBackground = TextPrimary,
    surface = CyberSurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = CyberBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    // Force modern Dark Theme for cybersecurity dashboard environment
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
