package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Refresh
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
fun VictoryScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val winner = uiState.winningTeam ?: uiState.teams.maxByOrNull { it.score }
    val winnerColor = winner?.let { Color(it.colorHex) } ?: DzGreen

    Scaffold(
        containerColor = Color.Transparent
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .atmosphericBackground()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 30.dp)
        ) {
            // Trophy Image Hero
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(170.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(DzGold.copy(alpha = 0.35f), Color.Transparent)
                                )
                            )
                    )

                    Image(
                        painter = painterResource(id = R.drawable.img_trophy),
                        contentDescription = "كأس الفوز",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(24.dp))
                    )
                }
            }

            // Victory Announcement
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🎉 مبروووك الفوز! 🏆",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = DzGoldLight,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(winnerColor.copy(alpha = 0.25f))
                            .border(2.dp, winnerColor, RoundedCornerShape(24.dp))
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(winner?.emoji ?: "🔴", fontSize = 28.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(
                            text = winner?.name ?: "الفريق الفائز",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "وصلوا لـ ${winner?.score ?: 0} دج وربحوا القعدة 🇩🇿🔥",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DzEmeraldGlow
                    )
                }
            }

            // Podium Standings
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    borderColor = DzGold.copy(alpha = 0.6f),
                    borderWidth = 1.5.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "🏅 الترتيب النهائي للماتش:",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = DzGoldLight
                        )

                        uiState.teams.sortedByDescending { it.score }.forEachIndexed { idx, team ->
                            val medal = when (idx) {
                                0 -> "🥇"
                                1 -> "🥈"
                                2 -> "🥉"
                                else -> "${idx + 1}."
                            }
                            val teamColor = Color(team.colorHex)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (idx == 0) DarkSurfaceVariant else DarkSurface.copy(alpha = 0.6f))
                                    .border(
                                        width = if (idx == 0) 1.dp else 0.dp,
                                        color = if (idx == 0) DzGold.copy(alpha = 0.5f) else Color.Transparent,
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(medal, fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(teamColor.copy(alpha = 0.3f))
                                            .border(1.dp, teamColor, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(team.emoji, fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = team.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = team.players.joinToString("، ") { it.name },
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Text(
                                    text = "${team.score} دج",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = if (idx == 0) DzGoldLight else Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Navigation CTA Buttons
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Show Full Stats
                    Button(
                        onClick = { viewModel.navigateTo(GameScreen.STATS_DASHBOARD) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .testTag("view_stats_btn"),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DzEmeraldGlow)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Leaderboard,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "📊 تفاصيل وإحصائيات القعدة",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }

                    // Play Again / Rematch
                    OutlinedButton(
                        onClick = { viewModel.startNewMatch() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("rematch_btn"),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DzGold),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(listOf(DzGold, DzGoldDark))
                        )
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("🔥 ثأر وماتش جديد!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    // Exit to Home
                    TextButton(
                        onClick = { viewModel.exitMatchToHome() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("العودة للقائمة الرئيسية 🏠", color = TextSecondary, fontSize = 15.sp)
                    }
                }
            }

            item {
                CreatorCreditFooter()
            }
        }
    }
}
