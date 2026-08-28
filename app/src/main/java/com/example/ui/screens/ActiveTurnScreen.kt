package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Difficulty
import com.example.data.model.ResolutionType
import com.example.engine.GameViewModel
import com.example.ui.components.AlgerianMusicSelectorDialog
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.DinarBadge
import com.example.ui.components.GlassCard
import com.example.ui.components.LargeTimerView
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun ActiveTurnScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val team = uiState.activeTeam
    val actor = uiState.activeRepresentative
    val word = uiState.currentWord
    val remainingSeconds = uiState.remainingSeconds
    val totalSeconds = uiState.settings.turnDurationSeconds
    val isTimerRunning = uiState.isTimerRunning

    var showMusicDialog by remember { mutableStateOf(false) }

    val turnResults = uiState.turnWordHistory
    val correctCount = turnResults.count { it.type == ResolutionType.CORRECT }
    val currentTurnNetPoints = turnResults.sumOf { it.pointsDelta }

    val pointsGain = if (word?.difficulty == Difficulty.HARD) 100 else 50

    Scaffold(
        topBar = {
            ArfiTopBar(
                title = "⏱️ مثل وفسر! (${team?.name ?: ""})",
                onBack = { viewModel.requestExitConfirmation(true) },
                sfxEnabled = uiState.settings.sfxEnabled,
                musicEnabled = uiState.settings.musicEnabled,
                onToggleSfx = { viewModel.updateSettings(uiState.settings.copy(sfxEnabled = !uiState.settings.sfxEnabled)) },
                onToggleMusic = { showMusicDialog = true }
            ) {
                IconButton(
                    onClick = {
                        if (isTimerRunning) {
                            viewModel.pauseTimer()
                        } else {
                            viewModel.resumeTimer()
                        }
                    },
                    modifier = Modifier.testTag("pause_timer_btn")
                ) {
                    Icon(
                        imageVector = if (!isTimerRunning) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = "إيقاف مؤقت",
                        tint = DzGold
                    )
                }
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .atmosphericBackground()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Live Turn Stats & Team Score Capsule
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                borderColor = Color(0x30FFFFFF)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎭 ", fontSize = 16.sp)
                        Text(
                            text = "الممثل: ${actor?.name ?: "—"}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Live speed round counter
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkSurfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("🎯 $correctCount صحيحة", fontSize = 12.sp, fontWeight = FontWeight.Black, color = DzEmeraldGlow)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentTurnNetPoints >= 0) "+$currentTurnNetPoints دج" else "$currentTurnNetPoints دج",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (currentTurnNetPoints >= 0) DzGoldLight else DzRed
                        )
                    }
                }
            }

            // Central Glowing Circular Timer
            LargeTimerView(
                secondsRemaining = remainingSeconds,
                totalSeconds = totalSeconds,
                isPaused = !isTimerRunning
            )

            // Current Active Word Presentation Card (Smooth Transition on Next Word)
            AnimatedContent(
                targetState = word,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(180)) + scaleIn(initialScale = 0.92f))
                        .togetherWith(fadeOut(animationSpec = tween(120)) + scaleOut(targetScale = 1.05f))
                },
                label = "wordCardAnim",
                modifier = Modifier.fillMaxWidth()
            ) { currentWord ->
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp),
                    shape = RoundedCornerShape(22.dp),
                    borderColor = if (currentWord?.difficulty == Difficulty.HARD) DzRed.copy(alpha = 0.6f) else DzEmeraldGlow.copy(alpha = 0.6f),
                    borderWidth = 2.dp,
                    containerColor = DarkCard
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (currentWord != null) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkSurfaceVariant)
                                    .padding(horizontal = 12.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(currentWord.category.icon, fontSize = 14.sp, modifier = Modifier.padding(end = 6.dp))
                                Text(
                                    text = currentWord.category.displayName,
                                    fontSize = 13.sp,
                                    color = DzGoldLight,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = currentWord.text,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                lineHeight = 36.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            DinarBadge(points = if (currentWord.difficulty == Difficulty.HARD) 100 else 50)
                        } else {
                            Text("جاري جلب الكلمة التالية...", color = TextSecondary, fontSize = 16.sp)
                        }
                    }
                }
            }

            // High-Impact Action Decision Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Correct Guess Button (Primary Large Green - adds 50 or 100 DA and rolls to next word immediately)
                Button(
                    onClick = { viewModel.resolveWord(ResolutionType.CORRECT) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .testTag("btn_correct_guess"),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DzEmeraldGlow)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "✅ جابوها صح! (+$pointsGain دج)",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }

                // Row for Skip (-20 DA) and Bad Performance (-5 DA)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Skip Button (-20 دج - as requested)
                    Button(
                        onClick = { viewModel.resolveWord(ResolutionType.SKIP) },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("btn_skip_word"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DzGoldDark)
                    ) {
                        Icon(Icons.Default.SkipNext, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "⏭️ تخطي (-20 دج)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }

                    // Bad Performance / Rule Break (-5 دج)
                    Button(
                        onClick = { viewModel.resolveWord(ResolutionType.BAD_PERFORMANCE) },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("btn_bad_performance"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DzRed)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "لم يمثل (-5 دج)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    // Algerian Music Selector Dialog
    if (showMusicDialog) {
        AlgerianMusicSelectorDialog(
            selectedTrack = uiState.settings.selectedMusicTrack,
            isMusicEnabled = uiState.settings.musicEnabled,
            onSelectTrack = { track ->
                viewModel.selectMusicTrack(track)
            },
            onToggleMusic = { enabled ->
                viewModel.updateSettings(uiState.settings.copy(musicEnabled = enabled))
            },
            onDismiss = { showMusicDialog = false }
        )
    }

    // Chaotic Random Meme Event Dialog
    uiState.activeEvent?.let { event ->
        AlertDialog(
            onDismissRequest = { viewModel.dismissRandomEventAndResume() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(event.emoji, fontSize = 28.sp, modifier = Modifier.padding(end = 8.dp))
                    Text(
                        text = "حدث مفاجئ: ${event.title}",
                        fontWeight = FontWeight.Black,
                        color = DzGoldLight,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_meme_event),
                        contentDescription = "حدث مضحك",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = event.memeInstruction,
                        fontSize = 15.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "الوقت راه متوقف، دير التحدي واضغط مواصلة!",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.dismissRandomEventAndResume() },
                    colors = ButtonDefaults.buttonColors(containerColor = DzGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("درتها ومواصلين! 🔥", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = DarkCardElevated,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
