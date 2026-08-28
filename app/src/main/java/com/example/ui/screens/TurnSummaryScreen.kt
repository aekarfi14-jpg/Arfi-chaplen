package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.ResolutionType
import com.example.engine.GameViewModel
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun TurnSummaryScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val nextTeam = uiState.activeTeam
    val turnResults = uiState.turnWordHistory
    val turnPoints = turnResults.sumOf { it.pointsDelta }

    Scaffold(
        topBar = {
            ArfiTopBar(
                title = "⏳ نهاية الدور",
                onBack = { viewModel.requestExitConfirmation(true) }
            )
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
                        onClick = { viewModel.proceedToNextTeamTurnIntro() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .testTag("next_team_turn_btn"),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DzEmeraldGlow)
                    ) {
                        Text(
                            text = "الدور التالي (${nextTeam?.name ?: "الفريق"}) ➡️",
                            fontSize = 18.sp,
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
            contentPadding = PaddingValues(top = 16.dp, bottom = 20.dp)
        ) {
            // Turn Banner
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    borderColor = if (turnPoints >= 0) Color(0x4000E676) else Color(0x40FF3D71),
                    containerColor = DarkCard
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🔔 خلاص وقت الدور!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (turnPoints >= 0) "+$turnPoints دج فهاد الدور 🔥" else "$turnPoints دج 💀",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = if (turnPoints >= 0) DzEmeraldGlow else DzRed
                        )
                    }
                }
            }

            // Standings Overview
            item {
                Text(
                    text = "📊 ترتيب الفرق الحالي:",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = DzGoldLight
                )
            }

            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = Color(0x30FFFFFF)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        uiState.teams.sortedByDescending { it.score }.forEachIndexed { idx, team ->
                            val teamColor = Color(team.colorHex)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(DarkSurfaceVariant.copy(alpha = 0.6f))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${idx + 1}.",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp,
                                        color = DzGold,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(teamColor.copy(alpha = 0.3f))
                                            .border(1.5.dp, teamColor, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(team.emoji, fontSize = 13.sp)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = team.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                }

                                Text(
                                    text = "${team.score} دج",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = DzGoldLight
                                )
                            }
                        }
                    }
                }
            }

            // Words played in this turn
            if (turnResults.isNotEmpty()) {
                item {
                    Text(
                        text = "📝 كلمات هذا الدور:",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                items(turnResults) { res ->
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        borderColor = Color(0x20FFFFFF)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = res.word.text,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "${res.word.category.displayName} • ${if (res.word.difficulty.name == "HARD") "صعيب" else "سهل"}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }

                            val badgeColor = when (res.type) {
                                ResolutionType.CORRECT -> DzGreen
                                ResolutionType.SKIP -> DzGoldDark
                                ResolutionType.BAD_PERFORMANCE -> DzRed
                            }

                            val badgeText = when (res.type) {
                                ResolutionType.CORRECT -> "+${res.pointsDelta} دج ✅"
                                ResolutionType.SKIP -> "${res.pointsDelta} دج ⏭️"
                                ResolutionType.BAD_PERFORMANCE -> "${res.pointsDelta} دج ❌"
                            }

                            Badge(containerColor = badgeColor) {
                                Text(
                                    text = badgeText,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
