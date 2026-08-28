package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DzGreen,
    onPrimary = Color.White,
    primaryContainer = DzGreenDark,
    onPrimaryContainer = Color.White,
    secondary = DzGold,
    onSecondary = Color.Black,
    secondaryContainer = DzGoldDark,
    onSecondaryContainer = Color.White,
    tertiary = DzRed,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    error = DzRed,
    onError = Color.White
)

private val LightColorScheme = darkColorScheme( // Keep vivid Algerian theme as primary look
    primary = DzGreen,
    onPrimary = Color.White,
    primaryContainer = DzGreenDark,
    onPrimaryContainer = Color.White,
    secondary = DzGold,
    onSecondary = Color.Black,
    secondaryContainer = DzGoldDark,
    onSecondaryContainer = Color.White,
    tertiary = DzRed,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    error = DzRed,
    onError = Color.White
)

@Composable
fun ArfiChaplenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
