package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Difficulty
import com.example.data.model.ResolutionType
import com.example.engine.GameViewModel
import com.example.i18n.AppStrings
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun ActiveTurnScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeTeam = uiState.activeTeam
    val activeRep = uiState.activeRepresentative
    val activeJudge = uiState.activeJudge
    val currentWord = uiState.currentWord
    val remainingSec = uiState.remainingSeconds
    val totalTurnSec = uiState.settings.turnDurationSeconds
    val isTimerRunning = uiState.isTimerRunning
    val isHard = currentWord?.difficulty == Difficulty.HARD
    val pointsGain = currentWord?.points ?: 50
    val lang = uiState.settings.appLanguage

    val timerProgress = (remainingSec.toFloat() / totalTurnSec.toFloat()).coerceIn(0f, 1f)
    val timerColor = when {
        remainingSec <= 10 -> DzRed
        remainingSec <= 20 -> DzGold
        else -> DzEmeraldGlow
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .atmosphericBackground()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar with Acting Details, Judge & Timer
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Representative and Judge Indicator
                    // REQUIREMENT 1: TEXT ONLY - الحاكم وماسك الهاتف (اختياري) / Judge & Phone Holder (Optional)
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🎭", fontSize = 14.sp, modifier = Modifier.padding(end = 4.dp))
                            Text(
                                text = "${AppStrings.actorRoleLabel(lang)} ${activeRep?.name ?: ""}",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = DzEmeraldGlow
                            )
                        }
                        Text(
                            text = "${AppStrings.judgeRoleLabel(lang)} ${activeJudge?.name ?: activeRep?.name ?: ""}",
                            fontSize = 11.sp,
                            color = DzGoldLight,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Top Action Controls: Pause & Exit Match
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (isTimerRunning) viewModel.pauseTimer() else viewModel.resumeTimer()
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant)
                                .testTag("pause_timer_btn")
                        ) {
                            Icon(
                                imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isTimerRunning) AppStrings.pauseTimer(lang) else AppStrings.resumeTimer(lang),
                                tint = if (isTimerRunning) DzGold else DzEmeraldGlow,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.requestExitConfirmation(true) },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant)
                                .testTag("exit_turn_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Exit",
                                tint = DzRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Timer Visual Bar & Remaining seconds display
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHard) AppStrings.doublePointsBanner(lang, pointsGain) else "",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = DzGoldLight
                        )
                        Text(
                            text = "$remainingSec ${AppStrings.secondsUnit(lang)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = timerColor
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    LinearProgressIndicator(
                        progress = { timerProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = timerColor,
                        trackColor = DarkSurfaceVariant
                    )
                }
            }

            // Word Stage Card (Center Stage)
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(26.dp),
                borderColor = if (isHard) DzGold else DzEmeraldGlow.copy(alpha = 0.6f),
                borderWidth = if (isHard) 2.dp else 1.5.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Category & Difficulty Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Badge(containerColor = DarkSurfaceVariant) {
                            Text(
                                text = "${currentWord?.category?.icon ?: ""} ${AppStrings.categoryName(currentWord?.category ?: com.example.data.model.Category.DZ, lang)}",
                                color = DzGoldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Badge(
                            containerColor = if (isHard) DzRedDark else DzGreenDark
                        ) {
                            Text(
                                text = if (isHard) (if (lang == AppLanguage.ENGLISH) "🔴 Hard (+100 pts)" else "🔴 صعيب (+100 دج)") else (if (lang == AppLanguage.ENGLISH) "🟢 Easy (+50 pts)" else "🟢 سهل (+50 دج)"),
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // The Big Word Display
                    AnimatedContent(
                        targetState = currentWord?.text ?: "",
                        transitionSpec = {
                            fadeIn() togetherWith fadeOut()
                        },
                        label = "word_animation"
                    ) { wordText ->
                        Text(
                            text = wordText,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 44.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )
                    }

                    // Turn Words Counter Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val correctThisTurn = uiState.turnWordHistory.count { it.type == ResolutionType.CORRECT }
                        Text(
                            text = if (lang == AppLanguage.ENGLISH) "Solved this turn: $correctThisTurn 🔥" else "كلمات صحيحة فهاد الدور: $correctThisTurn 🔥",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                    }
                }
            }

            // BOTTOM SCORING & RESOLUTION CONTROLS
            // REQUIREMENT 2: Primarily visual team scoring buttons during Active Representation Turns
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Section Title for Which Team Guessed First
                Text(
                    text = AppStrings.whoGuessedFirst(lang, pointsGain),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DzGoldLight,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Team Correct Scoring Buttons: Visual Compact Row
                // Fits up to 4 teams horizontally without overflow or clipping
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    uiState.teams.forEach { team ->
                        val teamColor = Color(team.colorHex)
                        Button(
                            onClick = {
                                viewModel.resolveWord(
                                    type = ResolutionType.CORRECT,
                                    targetTeamId = team.id
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("score_btn_${team.id}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = teamColor.copy(alpha = 0.85f),
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(team.emoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "+$pointsGain",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Skip & Bad Performance Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Skip Button (-20 pts)
                    Button(
                        onClick = { viewModel.resolveWord(type = ResolutionType.SKIP) },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("skip_word_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkSurfaceVariant,
                            contentColor = TextSecondary
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.SkipNext,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = DzGold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = AppStrings.skipBtn(lang),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Rule Break / Bad Acting Penalty Button (-5 pts)
                    Button(
                        onClick = { viewModel.resolveWord(type = ResolutionType.BAD_PERFORMANCE) },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("bad_performance_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DzRedDark.copy(alpha = 0.6f),
                            contentColor = DzRed
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text(
                            text = AppStrings.badPerformanceBtn(lang),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Surprise Random Meme Event Dialog (Comedic twist)
        if (uiState.isEventDialogVisible && uiState.activeEvent != null) {
            val event = uiState.activeEvent!!
            AlertDialog(
                onDismissRequest = { viewModel.dismissRandomEventAndResume() },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(event.emoji, fontSize = 28.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(
                            text = event.title,
                            fontWeight = FontWeight.Black,
                            color = DzGoldLight,
                            fontSize = 18.sp
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = event.memeInstruction,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "\"${event.quote}\"",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.dismissRandomEventAndResume() },
                        colors = ButtonDefaults.buttonColors(containerColor = DzGreen)
                    ) {
                        Text(if (lang == AppLanguage.ENGLISH) "Done & Resume! 🔥" else "فهمت! كمل العداد 🔥", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = DarkCardElevated,
                shape = RoundedCornerShape(22.dp)
            )
        }

        // Exit Match Confirmation Dialog
        if (uiState.showExitConfirmDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.requestExitConfirmation(false) },
                title = {
                    Text(
                        text = AppStrings.exitMatchConfirmTitle(lang),
                        fontWeight = FontWeight.Black,
                        color = DzRed
                    )
                },
                text = {
                    Text(
                        text = AppStrings.exitMatchConfirmBody(lang),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.exitMatchToHome() },
                        colors = ButtonDefaults.buttonColors(containerColor = DzRed)
                    ) {
                        Text(AppStrings.exitMatchYes(lang), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.requestExitConfirmation(false) }) {
                        Text(AppStrings.exitMatchCancel(lang), color = TextSecondary)
                    }
                },
                containerColor = DarkCardElevated,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}
