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
import com.example.data.model.AppLanguage
import com.example.data.model.GameScreen
import com.example.engine.GameViewModel
import com.example.i18n.AppStrings
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
    val lang = settings.appLanguage
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
        // Top Brand Bar with Audio Controls & Language switcher
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
                    Text(lang.flag, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = AppStrings.appTitle(lang),
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = AppStrings.appSubtitle(lang),
                        fontSize = 11.sp,
                        color = DzGoldLight,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Quick audio toggles & Language quick toggle
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Language switch button in top bar
                IconButton(
                    onClick = { viewModel.toggleLanguage() },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DzGold.copy(alpha = 0.4f), CircleShape)
                        .testTag("home_lang_toggle")
                ) {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "EN" else "عربي",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = DzGoldLight
                    )
                }

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
                        contentDescription = "Sound FX",
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
                        contentDescription = "Music",
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
                    contentDescription = "Hero Banner",
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

                // Floating Hero App Icon
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon),
                        contentDescription = "App Icon",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.5.dp, DzGold, CircleShape)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Big Catchy Title & Slogan
        Text(
            text = AppStrings.appTitle(lang),
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Text(
            text = AppStrings.heroSlogan(lang),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = DzGoldLight,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Modern 3-Metric Capsules Row with Clickable Language Switcher Button
        Row(
            modifier = Modifier
                .fillMaxWidth(0.92f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCapsule(
                icon = "🇩🇿",
                title = AppStrings.wordsCountTitle(lang),
                subtitle = AppStrings.wordsCountSubtitle(lang),
                modifier = Modifier.weight(1f)
            )

            // Requirement 7: Language Button on Home (replacing old decorative offline icon)
            MetricCapsule(
                icon = "🌐",
                title = AppStrings.languageToggleTitle(lang),
                subtitle = AppStrings.languageToggleSubtitle(lang),
                onClick = { viewModel.toggleLanguage() },
                isHighlight = true,
                modifier = Modifier.weight(1.15f)
            )

            MetricCapsule(
                icon = "🎭",
                title = AppStrings.categoriesCapsuleTitle(lang),
                subtitle = AppStrings.categoriesCapsuleSubtitle(lang),
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
                .testTag("btn_start_game"),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DzGreen
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = AppStrings.startNewMatch(lang),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Secondary Navigation Grid Cards
        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Words Manager / Custom Words Bank
            HomeMenuCard(
                icon = "📚",
                title = AppStrings.wordBankTitle(lang),
                subtitle = AppStrings.wordBankSubtitle(lang),
                badge = "${uiState.totalWordsInLibrary}",
                onClick = { viewModel.navigateTo(GameScreen.CUSTOM_WORDS_MANAGER) }
            )

            // Statistics & Hall of Fame
            HomeMenuCard(
                icon = "🏆",
                title = AppStrings.statsTitle(lang),
                subtitle = AppStrings.statsSubtitle(lang),
                badge = if (lang == AppLanguage.ENGLISH) "Hall of Fame" else "جوائز وتاريخ",
                onClick = { viewModel.navigateTo(GameScreen.STATS_DASHBOARD) }
            )

            // Rules & App Origin Story
            HomeMenuCard(
                icon = "ℹ️",
                title = AppStrings.rulesTitle(lang),
                subtitle = AppStrings.rulesSubtitle(lang),
                badge = if (lang == AppLanguage.ENGLISH) "Rules" else "القوانين",
                onClick = { viewModel.navigateTo(GameScreen.ABOUT) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        CreatorCreditFooter(creatorText = AppStrings.creatorStoryHeader(lang))
        Spacer(modifier = Modifier.height(16.dp))
    }

    // Algerian Music Selector Dialog
    if (showMusicDialog) {
        AlgerianMusicSelectorDialog(
            selectedTrack = settings.selectedMusicTrack,
            isMusicEnabled = settings.musicEnabled,
            lang = lang,
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
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isHighlight: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isHighlight) DarkSurfaceVariant else DarkCard)
            .border(
                1.dp,
                if (isHighlight) DzGold.copy(alpha = 0.7f) else Color(0x20FFFFFF),
                RoundedCornerShape(16.dp)
            )
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            )
            .padding(vertical = 10.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = if (isHighlight) DzGoldLight else Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun HomeMenuCard(
    icon: String,
    title: String,
    subtitle: String,
    badge: String,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        borderColor = Color(0x25FFFFFF)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DzGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = icon, fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }
            }

            Badge(
                containerColor = DarkSurfaceVariant,
                contentColor = DzGoldLight
            ) {
                Text(
                    text = badge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun AlgerianMusicSelectorDialog(
    selectedTrack: com.example.data.model.AlgerianMusicTrack,
    isMusicEnabled: Boolean,
    lang: AppLanguage = AppLanguage.ARABIC,
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
                    text = AppStrings.musicDialogTitle(lang),
                    fontWeight = FontWeight.Black,
                    color = Color.White,
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(AppStrings.musicDialogToggle(lang), color = Color.White, fontSize = 14.sp)
                    Switch(
                        checked = isMusicEnabled,
                        onCheckedChange = onToggleMusic,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = DzGreen,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = DarkSurfaceVariant
                        )
                    )
                }

                Divider(color = Color(0x20FFFFFF))

                com.example.data.model.AlgerianMusicTrack.getAllTracks().forEach { track ->
                    val isSelected = selectedTrack == track && isMusicEnabled
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) DzGreenDark else DarkSurfaceVariant)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) DzGold else Color(0x15FFFFFF),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { onSelectTrack(track) }
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(track.emoji, fontSize = 20.sp, modifier = Modifier.padding(end = 10.dp))
                                Column {
                                    Text(
                                        text = track.title,
                                        fontWeight = FontWeight.Bold,
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
                                Text(
                                    if (lang == AppLanguage.ENGLISH) "Playing" else "شغال",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = DzEmeraldGlow
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = DzGreen)
            ) {
                Text(AppStrings.closeBtn(lang), color = Color.White)
            }
        },
        containerColor = DarkCardElevated,
        shape = RoundedCornerShape(20.dp)
    )
}
