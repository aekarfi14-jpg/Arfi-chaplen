package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.AppLanguage
import com.example.data.model.Category
import com.example.data.model.EventFrequency
import com.example.data.model.GameScreen
import com.example.engine.GameViewModel
import com.example.i18n.AppStrings
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun CategorySettingsScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings = uiState.settings
    val lang = settings.appLanguage

    Scaffold(
        topBar = {
            ArfiTopBar(
                title = AppStrings.settingsTitle(lang),
                onBack = { viewModel.navigateTo(GameScreen.SETUP_TEAMS) }
            )
        },
        bottomBar = {
            Surface(
                color = DarkBackground.copy(alpha = 0.95f),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Button(
                        onClick = { viewModel.navigateTo(GameScreen.SETUP_REVIEW) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("btn_proceed_review"),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DzGreen)
                    ) {
                        Text(
                            text = AppStrings.continueToReview(lang),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .atmosphericBackground()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 20.dp)
        ) {
            // Turn Timer settings card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = AppStrings.turnDurationLabel(lang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = DzGoldLight
                            )
                            Text(
                                text = "${settings.turnDurationSeconds} ${AppStrings.secondsUnit(lang)}",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(45, 60, 90).forEach { sec ->
                                val isSel = settings.turnDurationSeconds == sec
                                Button(
                                    onClick = { viewModel.updateSettings(settings.copy(turnDurationSeconds = sec)) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSel) DzGreen else DarkSurfaceVariant,
                                        contentColor = if (isSel) Color.White else TextMuted
                                    )
                                ) {
                                    Text("$sec ${AppStrings.secondsUnit(lang)}", fontSize = 13.sp, fontWeight = if (isSel) FontWeight.Black else FontWeight.Normal)
                                }
                            }
                        }
                    }
                }
            }

            // Winning Score Goal card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = AppStrings.winningScoreLabel(lang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = DzGoldLight
                            )
                            Text(
                                text = "${settings.winningScore} ${AppStrings.currencyUnit(lang)}",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = DzGold
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(500, 1000, 1500).forEach { pts ->
                                val isSel = settings.winningScore == pts
                                Button(
                                    onClick = { viewModel.updateSettings(settings.copy(winningScore = pts)) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSel) DzGoldDark else DarkSurfaceVariant,
                                        contentColor = if (isSel) Color.Black else TextMuted
                                    )
                                ) {
                                    Text("$pts ${AppStrings.currencyUnit(lang)}", fontSize = 13.sp, fontWeight = if (isSel) FontWeight.Black else FontWeight.Normal)
                                }
                            }
                        }
                    }
                }
            }

            // Random Meme Events Frequency
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = AppStrings.randomEventsLabel(lang),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = DzGoldLight
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            EventFrequency.values().forEach { freq ->
                                val isSel = settings.randomEventFrequency == freq
                                val freqLabel = when (freq) {
                                    EventFrequency.OFF -> if (lang == AppLanguage.ENGLISH) "Disabled ❌" else freq.label
                                    EventFrequency.VERY_RARE -> if (lang == AppLanguage.ENGLISH) "Very Rare 🐢 (Recommended)" else freq.label
                                    EventFrequency.RARE -> if (lang == AppLanguage.ENGLISH) "Rare 🎲" else freq.label
                                    EventFrequency.NORMAL -> if (lang == AppLanguage.ENGLISH) "Frequent 🔥" else freq.label
                                }
                                val freqDesc = when (freq) {
                                    EventFrequency.OFF -> if (lang == AppLanguage.ENGLISH) "No surprise events will trigger" else freq.description
                                    EventFrequency.VERY_RARE -> if (lang == AppLanguage.ENGLISH) "Subtle occasional challenges to keep things lively" else freq.description
                                    EventFrequency.RARE -> if (lang == AppLanguage.ENGLISH) "Occasional comedic challenge" else freq.description
                                    EventFrequency.NORMAL -> if (lang == AppLanguage.ENGLISH) "Frequent comedic twists creating chaotic fun" else freq.description
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSel) DzGreenDark else DarkSurfaceVariant)
                                        .border(
                                            width = 1.dp,
                                            color = if (isSel) DzGold else Color.Transparent,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.updateSettings(settings.copy(randomEventFrequency = freq)) }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = freqLabel,
                                                fontWeight = if (isSel) FontWeight.Black else FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = if (isSel) DzGoldLight else Color.White
                                            )
                                            Text(
                                                text = freqDesc,
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                        }
                                        RadioButton(
                                            selected = isSel,
                                            onClick = { viewModel.updateSettings(settings.copy(randomEventFrequency = freq)) },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = DzGold,
                                                unselectedColor = TextMuted
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Audio & Music Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(AppStrings.sfxLabel(lang), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(AppStrings.sfxSubtitle(lang), color = TextSecondary, fontSize = 11.sp)
                            }
                            Switch(
                                checked = settings.sfxEnabled,
                                onCheckedChange = { isEnabled ->
                                    viewModel.updateSettings(settings.copy(sfxEnabled = isEnabled))
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = DzGreen,
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = DarkSurfaceVariant
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color(0x15FFFFFF))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(AppStrings.musicLabel(lang), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(AppStrings.musicSubtitle(lang), color = TextSecondary, fontSize = 11.sp)
                            }
                            Switch(
                                checked = settings.musicEnabled,
                                onCheckedChange = { isEnabled ->
                                    viewModel.updateSettings(settings.copy(musicEnabled = isEnabled))
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = DzGreen,
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = DarkSurfaceVariant
                                )
                            )
                        }

                        if (settings.musicEnabled) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                com.example.data.model.AlgerianMusicTrack.getAllTracks().forEach { track ->
                                    val isSelected = settings.selectedMusicTrack == track
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) DzGreenDark else DarkSurfaceVariant)
                                            .border(
                                                width = 1.dp,
                                                color = if (isSelected) DzGold else Color(0x15FFFFFF),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable { viewModel.selectMusicTrack(track) }
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(track.emoji, fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
                                                Column {
                                                    Text(
                                                        text = track.title,
                                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                                        fontSize = 13.sp,
                                                        color = if (isSelected) DzGoldLight else Color.White
                                                    )
                                                    Text(
                                                        text = track.subtitle,
                                                        fontSize = 10.sp,
                                                        color = TextSecondary,
                                                        maxLines = 1
                                                    )
                                                }
                                            }
                                            if (isSelected) {
                                                Text(if (lang == AppLanguage.ENGLISH) "Playing" else "🔥 شغال", fontSize = 11.sp, fontWeight = FontWeight.Black, color = DzEmeraldGlow)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Categories Section Title & Counter
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.availableCategoriesLabel(lang),
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                    Text(
                        text = AppStrings.enabledOf(lang, settings.enabledCategories.size, Category.values().size),
                        fontSize = 13.sp,
                        color = DzEmeraldGlow,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Category cards
            items(Category.values().size) { idx ->
                val cat = Category.values()[idx]
                val isEnabled = settings.enabledCategories.contains(cat)
                val catTitle = AppStrings.categoryName(cat, lang)
                val catDesc = AppStrings.categoryDescription(cat, lang)

                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleCategory(cat) },
                    shape = RoundedCornerShape(18.dp),
                    borderColor = if (isEnabled) DzEmeraldGlow.copy(alpha = 0.5f) else Color(0x15FFFFFF),
                    containerColor = if (isEnabled) DarkCard else DarkSurface.copy(alpha = 0.6f)
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
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isEnabled) DarkSurfaceVariant else DarkCard)
                                    .border(
                                        1.dp,
                                        if (isEnabled) DzGold.copy(alpha = 0.6f) else Color(0x15FFFFFF),
                                        RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(cat.icon, fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = catTitle,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = if (isEnabled) Color.White else TextMuted
                                )
                                Text(
                                    text = catDesc,
                                    fontSize = 12.sp,
                                    color = if (isEnabled) TextSecondary else TextMuted,
                                    maxLines = 1
                                )
                            }
                        }

                        Switch(
                            checked = isEnabled,
                            onCheckedChange = { viewModel.toggleCategory(cat) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = DzGreen,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = DarkSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    }
}
