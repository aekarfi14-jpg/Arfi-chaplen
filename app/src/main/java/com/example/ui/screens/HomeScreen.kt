package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.GameScreen
import com.example.engine.GameViewModel
import com.example.ui.components.CreatorCreditFooter
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings = uiState.settings
    val scrollState = rememberScrollState()
    var showMusicDialog by remember { mutableStateOf(false) }

    // Smooth subtle pulsing animation for the Start Game button
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_hero")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btnPulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .atmosphericBackground()
            .verticalScroll(scrollState)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Brand Bar with Audio Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(DzGreenDark)
                        .border(1.5.dp, DzGoldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🇩🇿", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "ARFI CHAPLEN",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "عرفي شابلن الجزائري",
                        fontSize = 11.sp,
                        color = DzGoldLight,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Quick audio toggles
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(
                    onClick = { viewModel.updateSettings(settings.copy(sfxEnabled = !settings.sfxEnabled)) },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .testTag("home_sfx_toggle")
                ) {
                    Icon(
                        imageVector = if (settings.sfxEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                        contentDescription = "صوت التأثيرات",
                        tint = if (settings.sfxEnabled) DzEmeraldGlow else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = { showMusicDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .testTag("home_music_toggle")
                ) {
                    Icon(
                        imageVector = if (settings.musicEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                        contentDescription = "الموسيقى",
                        tint = if (settings.musicEnabled) DzGold else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Hero Visual Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .height(210.dp),
            shape = RoundedCornerShape(26.dp),
            borderColor = Color(0x4000E676),
            containerColor = Color.Transparent
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "بانر اللعبة الجزائري",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // High-fidelity Gradient Vignette
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    DarkBackground.copy(alpha = 0.5f),
                                    DarkBackground.copy(alpha = 0.95f)
                                )
                            )
                        )
                )

                // Floating Hero App Icon & Slogan
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon),
                        contentDescription = "أيقونة اللعبة",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.5.dp, DzGold, CircleShape)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Big Catchy Title & Description
        Text(
            text = "🎭 عرفي شابلن",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Text(
            text = "لعبة التمثيل والضحك الجزائري في القعدات 🇩🇿🔥",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = DzGoldLight,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Modern 3-Metric Capsules Row
        Row(
            modifier = Modifier
                .fillMaxWidth(0.92f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCapsule(
                icon = "🇩🇿",
                title = "1600+ كلمة",
                subtitle = "قاموس جزائري",
                modifier = Modifier.weight(1f)
            )
            MetricCapsule(
                icon = "📴",
                title = "أوفلاين 100%",
                subtitle = "بدون إنترنت",
                modifier = Modifier.weight(1f)
            )
            MetricCapsule(
                icon = "🎭",
                title = "8 فئات",
                subtitle = "متنوعة ومضحكة",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Primary Action: Grand Glowing Play Button
        Button(
            onClick = { viewModel.navigateTo(GameScreen.SETUP_TEAMS) },
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .height(68.dp)
                .scale(pulseScale)
                .testTag("start_new_game_button"),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DzEmeraldGlow)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "🔥 ابدأ ماتش جديد",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid of Quick Feature Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth(0.92f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Words Bank Card
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .height(125.dp)
                    .clickable { viewModel.navigateTo(GameScreen.CUSTOM_WORDS_MANAGER) }
                    .testTag("custom_words_btn"),
                borderColor = Color(0x35FFB703),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceVariant)
                            .border(1.dp, DzGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = DzGold,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "📚 بنك الكلمات",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Text(
                        text = "أضف كلمات قعدتكم",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            // Stats Dashboard Card
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .height(125.dp)
                    .clickable { viewModel.navigateTo(GameScreen.STATS_DASHBOARD) }
                    .testTag("stats_dashboard_btn"),
                borderColor = Color(0x3500E676),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceVariant)
                            .border(1.dp, DzEmeraldGlow.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Leaderboard,
                            contentDescription = null,
                            tint = DzEmeraldGlow,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "🏆 إحصائيات وجوائز",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Text(
                        text = "أفضل ممثل وكوارث",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // About & Rules Button
        GlassCard(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clickable { viewModel.navigateTo(GameScreen.ABOUT) }
                .testTag("about_game_btn"),
            borderColor = Color(0x25FFFFFF),
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = DzGoldLight, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "ℹ️ قوانين اللعبة وحكاية صنعها",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = "كيفاش تلعبوا بالتناوب العادل والميمز",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
                Text("⬅️", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Creator Credit
        CreatorCreditFooter(modifier = Modifier.fillMaxWidth(0.92f))

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (showMusicDialog) {
        com.example.ui.components.AlgerianMusicSelectorDialog(
            selectedTrack = settings.selectedMusicTrack,
            isMusicEnabled = settings.musicEnabled,
            onSelectTrack = { track ->
                viewModel.selectMusicTrack(track)
            },
            onToggleMusic = { enabled ->
                viewModel.updateSettings(settings.copy(musicEnabled = enabled))
            },
            onDismiss = { showMusicDialog = false }
        )
    }
}

@Composable
private fun MetricCapsule(
    icon: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(16.dp),
        borderColor = Color(0x25FFFFFF),
        containerColor = DarkCard.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 16.sp)
            Text(
                text = title,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp,
                color = Color.White,
                maxLines = 1
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextSecondary,
                maxLines = 1
            )
        }
    }
}
