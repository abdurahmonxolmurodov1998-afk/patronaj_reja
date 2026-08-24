package com.patronaj.reja.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PrimaryBlue = Color(0xFF1565C0)
private val PrimaryBlueDark = Color(0xFF90CAF9)
private val Danger = Color(0xFFD32F2F)
private val Warning = Color(0xFFF57C00)
private val Success = Color(0xFF2E7D32)

val ColorHigh = Danger
val ColorMedium = Warning
val ColorLow = Success

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    secondary = Success
)

private val DarkColors = darkColorScheme(
    primary = PrimaryBlueDark,
    secondary = Success
)

@Composable
fun PatronajRejaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
