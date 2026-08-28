package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameScreen
import com.example.engine.GameViewModel
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.SetupStepIndicator
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun ReviewGameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings = uiState.settings

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(DarkSurface.copy(alpha = 0.95f))) {
                ArfiTopBar(
                    title = "📋 مراجعة القعدة قبل البداية",
                    onBack = { viewModel.navigateTo(GameScreen.SETUP_CATEGORIES) },
                    sfxEnabled = settings.sfxEnabled,
                    musicEnabled = settings.musicEnabled,
                    onToggleSfx = { viewModel.updateSettings(settings.copy(sfxEnabled = !settings.sfxEnabled)) },
                    onToggleMusic = { viewModel.updateSettings(settings.copy(musicEnabled = !settings.musicEnabled)) }
                )
                SetupStepIndicator(currentStep = 3)
            }
        },
        bottomBar = {
            Surface(
                color = DarkSurface.copy(alpha = 0.95f),
                tonalElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { viewModel.startNewMatch() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .testTag("launch_match_btn"),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DzEmeraldGlow)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "🔥 يلا نبدأو الماتش والضحك!",
                            fontSize = 19.sp,
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
            item {
                Text(
                    text = "جاهزين للضحك؟ راجعوا التشكيلة وقوانين اللعبة:",
                    fontSize = 15.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Teams & Players Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = Color(0x3500E676)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "👥 الفرق المشاركة (${uiState.teams.size} فرق):",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = DzGoldLight
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        uiState.teams.forEach { team ->
                            val teamColor = Color(team.colorHex)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkSurfaceVariant.copy(alpha = 0.5f))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(teamColor.copy(alpha = 0.3f))
                                        .border(1.5.dp, teamColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(team.emoji, fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = team.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = team.players.joinToString("، ") { it.name },
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Match Settings Overview
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = Color(0x35FFB703)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "⚙️ إعدادات المباراة:",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = DzGoldLight
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("⏱️ وقت الدور:", color = TextSecondary, fontSize = 14.sp)
                            Text("${settings.turnDurationSeconds} ثانية", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("🏆 نقاط الفوز:", color = TextSecondary, fontSize = 14.sp)
                            Text("${settings.winningScore} دج", color = DzGold, fontWeight = FontWeight.Black)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("🎲 الأحداث المفاجئة:", color = TextSecondary, fontSize = 14.sp)
                            Text(settings.randomEventFrequency.label, color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("📚 الفئات المفعلة:", color = TextSecondary, fontSize = 14.sp)
                            Text("${settings.enabledCategories.size} من 8", color = DzEmeraldGlow, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Categories Badges
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = Color(0x25FFFFFF)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "🎭 الفئات المختارة:",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = DzGoldLight
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            settings.enabledCategories.take(4).forEach { cat ->
                                SuggestionChip(
                                    onClick = {},
                                    label = { Text("${cat.icon} ${cat.displayName}", fontSize = 11.sp) },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = DarkSurfaceVariant,
                                        labelColor = Color.White
                                    )
                                )
                            }
                        }
                        if (settings.enabledCategories.size > 4) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                settings.enabledCategories.drop(4).forEach { cat ->
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text("${cat.icon} ${cat.displayName}", fontSize = 11.sp) },
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = DarkSurfaceVariant,
                                            labelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
