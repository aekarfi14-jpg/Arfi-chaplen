package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
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
import com.example.engine.GameViewModel
import com.example.i18n.AppStrings
import com.example.ui.components.CreatorCreditFooter
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun TurnIntroScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeTeam = uiState.activeTeam
    val activeRep = uiState.activeRepresentative
    val activeJudge = uiState.activeJudge
    val teamColor = Color(activeTeam?.colorHex ?: 0xFFE63946)
    val lang = uiState.settings.appLanguage
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .atmosphericBackground()
            .verticalScroll(scrollState)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Turn Header Badge
            Surface(
                color = teamColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(teamColor, teamColor.copy(alpha = 0.5f)))
                ),
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(activeTeam?.emoji ?: "🔴", fontSize = 18.sp, modifier = Modifier.padding(end = 6.dp))
                    Text(
                        text = AppStrings.turnOf(lang, activeTeam?.name ?: ""),
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Massive Team & Role Showcase Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                borderColor = teamColor.copy(alpha = 0.8f),
                borderWidth = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(teamColor.copy(alpha = 0.25f))
                            .border(2.dp, teamColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(activeTeam?.emoji ?: "🔴", fontSize = 36.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = activeTeam?.name ?: "",
                        fontWeight = FontWeight.Black,
                        fontSize = 26.sp,
                        color = Color.White
                    )

                    Text(
                        text = AppStrings.currentTeamScore(lang, activeTeam?.score ?: 0),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DzGoldLight
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0x20FFFFFF))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Actor spotlight
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurfaceVariant)
                            .border(1.dp, DzEmeraldGlow.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = AppStrings.actorRoleLabel(lang),
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = activeRep?.name ?: "اللاعب",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = DzEmeraldGlow
                            )
                        }
                        Text("🎭", fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Judge / Phone holder spotlight
                    // REQUIREMENT 1: TEXT ONLY - الحاكم وماسك الهاتف (اختياري): / Judge & Phone Holder (Optional):
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurfaceVariant)
                            .border(1.dp, DzGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = AppStrings.judgeRoleLabel(lang),
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = activeJudge?.name ?: activeRep?.name ?: "اللاعب",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = DzGoldLight
                            )
                        }
                        Text("📱", fontSize = 24.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Speed Frenzy Rule Notice
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = AppStrings.speedRulesHeader(lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DzGold
                    )
                    Text(
                        text = AppStrings.speedRulesBody(lang),
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Action Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Main direct start: Go immediately
            Button(
                onClick = { viewModel.startActiveTurn() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .testTag("btn_start_active_turn"),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DzGreen),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.startFrenzyBtn(lang),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            // Secret private reveal option
            OutlinedButton(
                onClick = { viewModel.prepareTurnReveal() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_private_reveal"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(DzGold, DzGoldDark))
                )
            ) {
                Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp), tint = DzGoldLight)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.secretRevealBtn(lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DzGoldLight
                )
            }

            CreatorCreditFooter()
        }
    }
}
