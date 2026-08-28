package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Algerian Flag Inspired High-Vibrancy Palette
val DzGreen = Color(0xFF00C853)
val DzGreenDark = Color(0xFF007E33)
val DzGreenLight = Color(0xFF69F0AE)
val DzEmeraldGlow = Color(0xFF00E676)

val DzRed = Color(0xFFFF3D71)
val DzRedDark = Color(0xFFC2185B)
val DzRedCrimson = Color(0xFFD50000)

val DzGold = Color(0xFFFFB703)
val DzGoldDark = Color(0xFFFB8500)
val DzGoldLight = Color(0xFFFFE082)
val DzGoldShimmer = Color(0xFFFFF3B0)

// Deep Futuristic Dark Surfaces
val DarkBackground = Color(0xFF0A0F0D)
val DarkSurface = Color(0xFF121B16)
val DarkSurfaceVariant = Color(0xFF1B2822)
val DarkCard = Color(0xFF16231C)
val DarkCardElevated = Color(0xFF1E3027)
val GlassSurface = Color(0x30FFFFFF)
val GlassBorder = Color(0x40FFFFFF)

val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB5C9BE)
val TextMuted = Color(0xFF768E80)

val AccentBlue = Color(0xFF38B6FF)
val AccentPurple = Color(0xFF9D4EDD)
val AccentOrange = Color(0xFFFF7B00)
val AccentPink = Color(0xFFFF007F)

// Gradient Brushes
val GreenGlowGradient = Brush.horizontalGradient(
    listOf(DzGreenLight, DzEmeraldGlow, DzGreen)
)

val GoldGlowGradient = Brush.horizontalGradient(
    listOf(DzGoldLight, DzGold, DzGoldDark)
)

val RedGlowGradient = Brush.horizontalGradient(
    listOf(Color(0xFFFF5252), DzRed, DzRedCrimson)
)

val DarkGlassGradient = Brush.verticalGradient(
    listOf(Color(0x3025382E), Color(0x1817241E))
)

val BorderGlowGradient = Brush.linearGradient(
    listOf(Color(0x5500E676), Color(0x22FFFFFF), Color(0x55FFB703))
)
