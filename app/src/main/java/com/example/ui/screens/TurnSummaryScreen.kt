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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.ResolutionType
import com.example.engine.GameViewModel
import com.example.i18n.AppStrings
import com.example.ui.components.CreatorCreditFooter
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun TurnSummaryScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val turnHistory = uiState.turnWordHistory
    val nextTeam = uiState.activeTeam
    val lang = uiState.settings.appLanguage

    val turnPoints = turnHistory.sumOf { it.pointsDelta }
    val correctCount = turnHistory.count { it.type == ResolutionType.CORRECT }
    val skipCount = turnHistory.count { it.type == ResolutionType.SKIP }
    val badCount = turnHistory.count { it.type == ResolutionType.BAD_PERFORMANCE }

    Scaffold(
        bottomBar = {
            Surface(
                color = DarkBackground.copy(alpha = 0.95f),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Button(
                        onClick = { viewModel.proceedToNextTeamTurnIntro() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .testTag("btn_next_turn"),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DzGreen),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(26.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.nextTeamTurn(lang, nextTeam?.name ?: ""),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
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
            contentPadding = PaddingValues(top = 16.dp, bottom = 20.dp)
        ) {
            // Turn Finish Header Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    borderColor = if (turnPoints >= 0) DzEmeraldGlow.copy(alpha = 0.6f) else DzRed.copy(alpha = 0.6f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = AppStrings.turnSummaryTitle(lang),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (turnPoints >= 0) AppStrings.turnPointsGained(lang, turnPoints) else AppStrings.turnPointsLost(lang, turnPoints),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = if (turnPoints >= 0) DzEmeraldGlow else DzRed
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats capsules
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ScoreMiniBadge("✅ ${if (lang == AppLanguage.ENGLISH) "Correct" else "صح"}: $correctCount", DzGreenDark, Modifier.weight(1f))
                            ScoreMiniBadge("⏭️ ${if (lang == AppLanguage.ENGLISH) "Skip" else "تخطي"}: $skipCount", DarkSurfaceVariant, Modifier.weight(1f))
                            ScoreMiniBadge("❌ ${if (lang == AppLanguage.ENGLISH) "Penalty" else "عقوبة"}: $badCount", DzRedDark, Modifier.weight(1f))
                        }
                    }
                }
            }

            // Current Leaderboard Standings
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = AppStrings.currentStandings(lang),
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = DzGoldLight
                        )
                        uiState.teams.sortedByDescending { it.score }.forEachIndexed { idx, team ->
                            val teamColor = Color(team.colorHex)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkSurfaceVariant)
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${idx + 1}.", fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.padding(end = 8.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(teamColor.copy(alpha = 0.3f))
                                            .border(1.dp, teamColor, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(team.emoji, fontSize = 14.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(team.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                                }
                                Text(
                                    text = "${team.score} ${AppStrings.currencyUnit(lang)}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = DzGoldLight
                                )
                            }
                        }
                    }
                }
            }

            // Words played during this turn
            if (turnHistory.isNotEmpty()) {
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = AppStrings.turnWordsList(lang),
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                color = DzGoldLight
                            )
                            turnHistory.forEach { item ->
                                val outcomeColor = when (item.type) {
                                    ResolutionType.CORRECT -> DzEmeraldGlow
                                    ResolutionType.SKIP -> DzGold
                                    ResolutionType.BAD_PERFORMANCE -> DzRed
                                }
                                val outcomeIcon = when (item.type) {
                                    ResolutionType.CORRECT -> "✅"
                                    ResolutionType.SKIP -> "⏭️"
                                    ResolutionType.BAD_PERFORMANCE -> "❌"
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(DarkSurfaceVariant)
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(outcomeIcon, fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
                                        Text(item.word.text, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                                    }
                                    Text(
                                        text = if (item.pointsDelta > 0) "+${item.pointsDelta}" else "${item.pointsDelta}",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp,
                                        color = outcomeColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                CreatorCreditFooter()
            }
        }
    }
}

@Composable
private fun ScoreMiniBadge(text: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}
