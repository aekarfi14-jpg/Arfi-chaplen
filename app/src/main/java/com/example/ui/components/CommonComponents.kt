package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Atmospheric background modifier that draws soft ambient emerald and gold glowing orbs
 * to eliminate flat dark screens and provide visual depth.
 */
fun Modifier.atmosphericBackground(): Modifier = this.drawBehind {
    val canvasWidth = size.width
    val canvasHeight = size.height

    // Base deep dark canvas
    drawRect(color = DarkBackground)

    // Top-start soft emerald glow orb
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0x3300E676), Color(0x10008744), Color.Transparent),
            center = Offset(canvasWidth * 0.15f, canvasHeight * 0.12f),
            radius = canvasWidth * 0.75f
        ),
        radius = canvasWidth * 0.75f,
        center = Offset(canvasWidth * 0.15f, canvasHeight * 0.12f)
    )

    // Bottom-end subtle gold glow orb
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0x28FFB703), Color(0x08F77F00), Color.Transparent),
            center = Offset(canvasWidth * 0.85f, canvasHeight * 0.88f),
            radius = canvasWidth * 0.7f
        ),
        radius = canvasWidth * 0.7f,
        center = Offset(canvasWidth * 0.85f, canvasHeight * 0.88f)
    )
}

/**
 * Modern Glassmorphic Card with subtle translucent gradient background and glowing border.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    borderColor: Color = Color(0x3500E676),
    borderWidth: Dp = 1.dp,
    containerColor: Color = DarkCard.copy(alpha = 0.85f),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .clip(shape)
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    listOf(borderColor, Color(0x20FFFFFF), borderColor.copy(alpha = 0.3f))
                ),
                shape = shape
            ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = shape
    ) {
        content()
    }
}

@Composable
fun DinarBadge(
    points: Int,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false
) {
    val bgBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF005A2E), Color(0xFF00381C))
    )
    val borderBrush = Brush.horizontalGradient(
        colors = listOf(DzGoldLight, DzGold, DzGoldDark)
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(bgBrush)
            .border(1.5.dp, borderBrush, RoundedCornerShape(24.dp))
            .padding(horizontal = if (isLarge) 18.dp else 12.dp, vertical = if (isLarge) 8.dp else 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🇩🇿",
            fontSize = if (isLarge) 22.sp else 15.sp,
            modifier = Modifier.padding(end = 6.dp)
        )
        Text(
            text = "+$points دج",
            color = DzGoldLight,
            fontWeight = FontWeight.Black,
            fontSize = if (isLarge) 22.sp else 15.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArfiTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    sfxEnabled: Boolean = true,
    musicEnabled: Boolean = true,
    onToggleSfx: (() -> Unit)? = null,
    onToggleMusic: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Black,
                fontSize = 19.sp,
                color = Color.White
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("top_bar_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = DzGoldLight
                    )
                }
            }
        },
        actions = {
            if (onToggleSfx != null) {
                IconButton(
                    onClick = onToggleSfx,
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .clip(CircleShape)
                        .background(if (sfxEnabled) DarkSurfaceVariant else Color.Transparent)
                        .testTag("sfx_toggle_btn")
                ) {
                    Icon(
                        imageVector = if (sfxEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                        contentDescription = "صوت التأثيرات",
                        tint = if (sfxEnabled) DzEmeraldGlow else TextMuted
                    )
                }
            }
            if (onToggleMusic != null) {
                IconButton(
                    onClick = onToggleMusic,
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .clip(CircleShape)
                        .background(if (musicEnabled) DarkSurfaceVariant else Color.Transparent)
                        .testTag("music_toggle_btn")
                ) {
                    Icon(
                        imageVector = if (musicEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                        contentDescription = "الموسيقى",
                        tint = if (musicEnabled) DzGold else TextMuted
                    )
                }
            }
            actions()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkSurface.copy(alpha = 0.95f),
            titleContentColor = Color.White
        )
    )
}

/**
 * Step Progress Indicator for the match creation wizard (Teams -> Categories -> Review)
 */
@Composable
fun SetupStepIndicator(
    currentStep: Int, // 1, 2, or 3
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val steps = listOf("١. الفرق 👥", "٢. الفئات ⚙️", "٣. المراجعة 📋")
        steps.forEachIndexed { index, title ->
            val stepNumber = index + 1
            val isActive = stepNumber == currentStep
            val isDone = stepNumber < currentStep

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when {
                            isActive -> DzGreenDark
                            isDone -> DarkSurfaceVariant
                            else -> DarkCard
                        }
                    )
                    .border(
                        width = 1.dp,
                        color = when {
                            isActive -> DzEmeraldGlow
                            isDone -> DzGreenLight.copy(alpha = 0.5f)
                            else -> Color(0x20FFFFFF)
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = if (isActive) FontWeight.Black else FontWeight.SemiBold,
                    color = when {
                        isActive -> Color.White
                        isDone -> DzGoldLight
                        else -> TextMuted
                    }
                )
            }
        }
    }
}

@Composable
fun LargeTimerView(
    secondsRemaining: Int,
    totalSeconds: Int,
    isPaused: Boolean = false,
    modifier: Modifier = Modifier
) {
    val progress = (secondsRemaining.toFloat() / totalSeconds.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    val isCritical = secondsRemaining <= 5 && secondsRemaining > 0
    val isWarning = secondsRemaining in 6..10

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isCritical) 1.12f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "timerScale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = if (isCritical) 0.65f else 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val timerColor = when {
        isCritical -> DzRed
        isWarning -> DzGoldDark
        else -> DzEmeraldGlow
    }

    Box(
        modifier = modifier
            .size(185.dp)
            .scale(if (isCritical) scale else 1f),
        contentAlignment = Alignment.Center
    ) {
        // Glowing background halo
        Box(
            modifier = Modifier
                .size(175.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(timerColor.copy(alpha = glowAlpha), Color.Transparent)
                    )
                )
        )

        // Progress ring
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(175.dp),
            color = timerColor,
            strokeWidth = 11.dp,
            trackColor = DarkSurfaceVariant
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$secondsRemaining",
                fontSize = 58.sp,
                fontWeight = FontWeight.Black,
                color = if (isCritical) DzRed else Color.White
            )
            Text(
                text = if (isPaused) "⏸️ موقف" else if (isCritical) "🔥 أسرع!" else "ثانية",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = if (isCritical) DzRed else DzGoldLight
            )
        }
    }
}

@Composable
fun CreatorCreditFooter(modifier: Modifier = Modifier) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        borderColor = Color(0x40FFB703),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFB703))
                    .border(1.dp, DzGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🗿",
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "هذه اللعبة سُكبتْ من دمي الطاهر وعرقي النفيس وفكري الذي لا يُضاهى... يونس الشيكور، الأعظم الذي وُلد ليصنع المستحيل.🗿",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = DzGoldLight,
                lineHeight = 18.sp
            )
        }
    }
}

/**
 * Algerian Music Track Selector Dialog for switching between authentic procedural styles.
 */
@Composable
fun AlgerianMusicSelectorDialog(
    selectedTrack: com.example.data.model.AlgerianMusicTrack,
    isMusicEnabled: Boolean,
    onSelectTrack: (com.example.data.model.AlgerianMusicTrack) -> Unit,
    onToggleMusic: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🎵", fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                Text(
                    text = "مكتبة الموسيقى الجزائرية",
                    fontWeight = FontWeight.Black,
                    color = DzGoldLight,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "تشغيل الموسيقى",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Switch(
                        checked = isMusicEnabled,
                        onCheckedChange = onToggleMusic,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = DzGreen,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = DarkCard
                        )
                    )
                }

                Text(
                    text = "اختر نوع الموسيقى في القعدة:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )

                com.example.data.model.AlgerianMusicTrack.getAllTracks().forEach { track ->
                    val isSelected = isMusicEnabled && selectedTrack == track
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onToggleMusic(true)
                                onSelectTrack(track)
                            },
                        shape = RoundedCornerShape(14.dp),
                        borderColor = if (isSelected) DzGold else Color(0x20FFFFFF),
                        borderWidth = if (isSelected) 1.5.dp else 1.dp,
                        containerColor = if (isSelected) DarkCardElevated else DarkCard
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) DzGreenDark else DarkSurfaceVariant)
                                        .border(1.dp, if (isSelected) DzGold else Color(0x20FFFFFF), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(track.emoji, fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = track.title,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp,
                                        color = if (isSelected) DzGoldLight else Color.White
                                    )
                                    Text(
                                        text = track.subtitle,
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        maxLines = 1
                                    )
                                }
                            }

                            if (isSelected) {
                                Text("🔥 شغال", fontSize = 11.sp, fontWeight = FontWeight.Black, color = DzEmeraldGlow)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = DzEmeraldGlow),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("تم ✅", color = Color.Black, fontWeight = FontWeight.Black)
            }
        },
        containerColor = DarkCard,
        shape = RoundedCornerShape(22.dp)
    )
}

