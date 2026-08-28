package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Category
import com.example.data.model.EventFrequency
import com.example.data.model.GameScreen
import com.example.engine.GameViewModel
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.SetupStepIndicator
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun CategorySettingsScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings = uiState.settings

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(DarkSurface.copy(alpha = 0.95f))) {
                ArfiTopBar(
                    title = "⚙️ الفئات وإعدادات الماتش",
                    onBack = { viewModel.navigateTo(GameScreen.SETUP_TEAMS) },
                    sfxEnabled = settings.sfxEnabled,
                    musicEnabled = settings.musicEnabled,
                    onToggleSfx = { viewModel.updateSettings(settings.copy(sfxEnabled = !settings.sfxEnabled)) },
                    onToggleMusic = { viewModel.updateSettings(settings.copy(musicEnabled = !settings.musicEnabled)) }
                )
                SetupStepIndicator(currentStep = 2)
            }
        },
        bottomBar = {
            Surface(
                color = DarkSurface.copy(alpha = 0.95f),
                tonalElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { viewModel.navigateTo(GameScreen.SETUP_TEAMS) },
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                    ) {
                        Text("⬅️ رجوع", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { viewModel.navigateTo(GameScreen.SETUP_REVIEW) },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(54.dp)
                            .testTag("next_to_review_btn"),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DzEmeraldGlow)
                    ) {
                        Text(
                            text = "التالي: مراجعة القعدة 📋",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
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
            contentPadding = PaddingValues(top = 12.dp, bottom = 20.dp)
        ) {
            // Match Core Duration Presets
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = Color(0x3500E676)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⏱️ وقت الدور الواحد:",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = "${settings.turnDurationSeconds} ثانية",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = DzEmeraldGlow
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick duration preset pills
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(15, 30, 45, 60, 90).forEach { sec ->
                                val isSelected = settings.turnDurationSeconds == sec
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) DzGreenDark else DarkSurfaceVariant)
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) DzEmeraldGlow else Color(0x15FFFFFF),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.updateSettings(settings.copy(turnDurationSeconds = sec)) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${sec}ث",
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                        fontSize = 13.sp,
                                        color = if (isSelected) Color.White else TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Target Winning Score Presets
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = Color(0x35FFB703)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🏆 نقاط الفوز بالماتش:",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = "${settings.winningScore} دج",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = DzGoldLight
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Winning score preset pills
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(500, 1000, 1500, 2000, 3000).forEach { pts ->
                                val isSelected = settings.winningScore == pts
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) DzGoldDark.copy(alpha = 0.3f) else DarkSurfaceVariant)
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) DzGold else Color(0x15FFFFFF),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.updateSettings(settings.copy(winningScore = pts)) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$pts",
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                        fontSize = 12.sp,
                                        color = if (isSelected) DzGoldLight else TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Random Events Frequency Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = Color(0x25FFFFFF)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "🎲 وتيرة الأحداث والمفاجآت الفكاهية:",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Text(
                            text = "تحديات مفاجئة للممثل فوسط الدور (تكلم بشلحة، اضحك بلا صوت...)",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            EventFrequency.values().forEach { freq ->
                                val isSelected = settings.randomEventFrequency == freq
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) DzRedDark.copy(alpha = 0.4f) else DarkSurfaceVariant)
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) DzRed else Color(0x15FFFFFF),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.updateSettings(settings.copy(randomEventFrequency = freq)) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = freq.label,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                                        fontSize = 11.sp,
                                        color = if (isSelected) Color.White else TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Algerian Procedural Music Library Selector
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = Color(0x35FFB703)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "🎵 نوع الموسيقى الجزائرية:",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "موسيقى تراثية وحماسية في الخلفية",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
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
                                                Text("🔥 شغال", fontSize = 11.sp, fontWeight = FontWeight.Black, color = DzEmeraldGlow)
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
                        text = "🎭 فئات الكلمات المتاحة:",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                    Text(
                        text = "${settings.enabledCategories.size} مفعلة من ${Category.values().size}",
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
                                    text = cat.displayName,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = if (isEnabled) Color.White else TextMuted
                                )
                                Text(
                                    text = cat.description,
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
