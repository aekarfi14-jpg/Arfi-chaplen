package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.GameScreen
import com.example.engine.GameViewModel
import com.example.i18n.AppStrings
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
    val stats = remember { viewModel.calculateStatistics() }
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.settings.appLanguage

    Scaffold(
        topBar = {
            ArfiTopBar(
                title = AppStrings.statsScreenTitle(lang),
                onBack = { viewModel.navigateTo(GameScreen.HOME) }
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
            contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp)
        ) {
            // Best Actor Spotlight
            stats.bestActorPlayer?.let { player ->
                item {
                    AwardCard(
                        emoji = "🎭",
                        title = AppStrings.topActorAward(lang),
                        playerName = player.name,
                        detail = "${player.correctGuesses} ${if (lang == AppLanguage.ENGLISH) "correct words" else "كلمة صحيحة"}",
                        accentColor = DzEmeraldGlow
                    )
                }
            }

            // Skip Champion Spotlight
            stats.mostSkipsPlayer?.let { player ->
                if (player.skips > 0) {
                    item {
                        AwardCard(
                            emoji = "⏭️",
                            title = AppStrings.skipChampAward(lang),
                            playerName = player.name,
                            detail = "${player.skips} ${if (lang == AppLanguage.ENGLISH) "skips" else "تخطي"}",
                            accentColor = DzGold
                        )
                    }
                }
            }

            // Bad Acting Penalty Spotlight
            stats.mostBadActorPlayer?.let { player ->
                if (player.badPerformances > 0) {
                    item {
                        AwardCard(
                            emoji = "❌",
                            title = AppStrings.badActorAward(lang),
                            playerName = player.name,
                            detail = "${player.badPerformances} ${if (lang == AppLanguage.ENGLISH) "penalties" else "أخطاء"}",
                            accentColor = DzRed
                        )
                    }
                }
            }

            // High-Level Numbers Overview
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = AppStrings.matchSummaryHeader(lang),
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = DzGoldLight
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(AppStrings.totalPlayedWords(lang), color = TextSecondary, fontSize = 13.sp)
                            Text("${stats.totalWordsPlayed}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(AppStrings.correctWords(lang), color = TextSecondary, fontSize = 13.sp)
                            Text("${stats.totalCorrectWords}", color = DzEmeraldGlow, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(AppStrings.totalSkips(lang), color = TextSecondary, fontSize = 13.sp)
                            Text("${stats.totalSkips}", color = DzGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(AppStrings.totalPenalties(lang), color = TextSecondary, fontSize = 13.sp)
                            Text("${stats.totalBadPerformances}", color = DzRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(AppStrings.totalDuration(lang), color = TextSecondary, fontSize = 13.sp)
                            Text("${stats.totalMatchDurationSeconds} ${AppStrings.secondsUnit(lang)}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Player breakdown per team
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = AppStrings.playersRankings(lang),
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
                                    Text("${team.score} ${AppStrings.currencyUnit(lang)}", fontWeight = FontWeight.Black, color = DzGoldLight)
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
                                            "${p.score} ${AppStrings.currencyUnit(lang)} (${p.correctGuesses} ${if (lang == AppLanguage.ENGLISH) "correct" else "صح"}، ${p.skips} ${if (lang == AppLanguage.ENGLISH) "skips" else "تخطي"})",
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
                                    text = AppStrings.savedMatchesHistory(lang, matchHistory.size),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = DzGoldLight
                                )
                                TextButton(
                                    onClick = { viewModel.clearSavedMatchHistory() }
                                ) {
                                    Text(AppStrings.clearHistoryBtn(lang), color = DzRed, fontSize = 12.sp)
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
                                            text = "🏆 ${if (lang == AppLanguage.ENGLISH) "Winner:" else "الفائز:"} ${match.winnerTeamEmoji} ${match.winnerTeamName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "${match.winnerScore} ${AppStrings.currencyUnit(lang)}",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp,
                                            color = DzEmeraldGlow
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    val teamsSummary = match.teamScores.joinToString(" | ") { "${it.emoji} ${it.teamName}: ${it.score}${AppStrings.currencyUnit(lang)}" }
                                    Text(
                                        text = teamsSummary,
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "⏱️ ${if (lang == AppLanguage.ENGLISH) "Duration:" else "المدة:"} ${match.durationSeconds}s | 📝 ${if (lang == AppLanguage.ENGLISH) "Correct:" else "كلمات صحيحة:"} ${match.correctWordsCount}/${match.totalWordsPlayed}",
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
