package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameScreen
import com.example.engine.GameViewModel
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.CreatorCreditFooter
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun StatisticsScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val stats = viewModel.calculateStatistics()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ArfiTopBar(
                title = "🏆 إحصائيات القعدة",
                onBack = {
                    if (uiState.winningTeam != null) {
                        viewModel.navigateTo(GameScreen.VICTORY)
                    } else {
                        viewModel.navigateTo(GameScreen.HOME)
                    }
                }
            )
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
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
        ) {
            // Header summary
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    borderColor = Color(0x40FFB703),
                    containerColor = DarkCard
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📊 ملخص أرقام المباراة",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${stats.totalWordsPlayed}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)
                                Text("ملعوبة", fontSize = 12.sp, color = TextSecondary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${stats.totalCorrectWords}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = DzEmeraldGlow)
                                Text("صحيحة", fontSize = 12.sp, color = TextSecondary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${stats.totalSkips}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = DzGold)
                                Text("تخطي", fontSize = 12.sp, color = TextSecondary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${stats.totalBadPerformances}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = DzRed)
                                Text("عقوبات", fontSize = 12.sp, color = TextSecondary)
                            }
                        }
                    }
                }
            }

            // Hall of Fame & Funny Awards
            item {
                Text(
                    text = "🌟 جوائز وشرف القعدة:",
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    color = DzGoldLight
                )
            }

            // Top Scorer
            item {
                AwardCard(
                    emoji = "👑",
                    title = "أكثر لاعب حصد نقاطاً (الهدّاف)",
                    playerName = stats.topScoringPlayer?.name ?: "—",
                    detail = "${stats.topScoringPlayer?.score ?: 0} دج",
                    accentColor = DzGold
                )
            }

            // Best Actor
            item {
                AwardCard(
                    emoji = "🎭",
                    title = "أفضل ممثل فالماتش (شابلن القعدة)",
                    playerName = stats.bestActorPlayer?.name ?: "—",
                    detail = "${stats.bestActorPlayer?.correctGuesses ?: 0} صحيحة",
                    accentColor = DzEmeraldGlow
                )
            }

            // Most Skips
            item {
                AwardCard(
                    emoji = "⏭️",
                    title = "ملك التخطي (ماتكسرش راسك)",
                    playerName = stats.mostSkipsPlayer?.name ?: "—",
                    detail = "${stats.mostSkipsPlayer?.skips ?: 0} تخطي",
                    accentColor = DzGoldDark
                )
            }

            // Disaster Actor
            item {
                AwardCard(
                    emoji = "🤡",
                    title = "كارثة التمثيل (لم يمثل جيداً)",
                    playerName = stats.mostBadActorPlayer?.name ?: "—",
                    detail = "${stats.mostBadActorPlayer?.badPerformances ?: 0} عقوبات",
                    accentColor = DzRed
                )
            }

            // Team rankings detail
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = Color(0x30FFFFFF)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "👥 تفاصيل الفرق واللاعبين:",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.White
                        )

                        stats.teamRankings.forEach { team ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(DarkSurfaceVariant)
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${team.emoji} ${team.name}", fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("${team.score} دج", fontWeight = FontWeight.Black, color = DzGoldLight)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                team.players.forEach { p ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("• ${p.name}", fontSize = 13.sp, color = TextSecondary)
                                        Text(
                                            "${p.score} دج (${p.correctGuesses} صح، ${p.skips} تخطي)",
                                            fontSize = 12.sp,
                                            color = TextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Saved Match History (Offline Storage)
            val matchHistory = viewModel.getSavedMatchHistory()
            if (matchHistory.isNotEmpty()) {
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        borderColor = Color(0x35FFB703)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📜 سجل المباريات السابقة (${matchHistory.size})",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = DzGoldLight
                                )
                                TextButton(
                                    onClick = { viewModel.clearSavedMatchHistory() }
                                ) {
                                    Text("مسح السجل", color = DzRed, fontSize = 12.sp)
                                }
                            }

                            matchHistory.take(10).forEach { match ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(DarkSurfaceVariant.copy(alpha = 0.7f))
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "🏆 الفائز: ${match.winnerTeamEmoji} ${match.winnerTeamName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "${match.winnerScore} دج",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp,
                                            color = DzEmeraldGlow
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    val teamsSummary = match.teamScores.joinToString(" | ") { "${it.emoji} ${it.teamName}: ${it.score}دج" }
                                    Text(
                                        text = teamsSummary,
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )

                                    Text(
                                        text = "⏱️ المدة: ${match.durationSeconds}ث | 📝 كلمات صحيحة: ${match.correctWordsCount}/${match.totalWordsPlayed}",
                                        fontSize = 11.sp,
                                        color = TextMuted
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
private fun AwardCard(
    emoji: String,
    title: String,
    playerName: String,
    detail: String,
    accentColor: Color
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        borderColor = accentColor.copy(alpha = 0.5f)
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
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.2f))
                        .border(1.5.dp, accentColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, fontSize = 12.sp, color = TextSecondary)
                    Text(
                        text = playerName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            Badge(containerColor = accentColor.copy(alpha = 0.25f)) {
                Text(
                    text = detail,
                    color = accentColor,
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
