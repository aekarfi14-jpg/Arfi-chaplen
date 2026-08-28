package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.GameViewModel
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun TurnIntroScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val team = uiState.activeTeam
    val actor = uiState.activeRepresentative
    val judge = uiState.activeJudge
    val teamColor = team?.let { Color(it.colorHex) } ?: DzGreen

    Scaffold(
        topBar = {
            ArfiTopBar(
                title = "🎭 بداية دور جديد",
                onBack = { viewModel.requestExitConfirmation(true) },
                sfxEnabled = uiState.settings.sfxEnabled,
                musicEnabled = uiState.settings.musicEnabled,
                onToggleSfx = { viewModel.updateSettings(uiState.settings.copy(sfxEnabled = !uiState.settings.sfxEnabled)) },
                onToggleMusic = { viewModel.updateSettings(uiState.settings.copy(musicEnabled = !uiState.settings.musicEnabled)) }
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Primary Grand Action: Launch continuous frenzy round!
                    Button(
                        onClick = { viewModel.startFrenzyRoundDirectly() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .testTag("start_frenzy_round_btn"),
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
                            text = "🔥 انطلق! (ابدأ العداد وتتابع الكلمات)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }

                    // Secondary option for private reveal if preferred
                    OutlinedButton(
                        onClick = { viewModel.prepareTurnReveal() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("reveal_secret_btn"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DzGoldLight)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "🔒 كشف سري مسبق للممثل فقط",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Team Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(teamColor.copy(alpha = 0.25f))
                        .border(3.dp, teamColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(team?.emoji ?: "🔴", fontSize = 36.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "دور ${team?.name ?: "الفريق"}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "رصيد الفريق الحالي: ${team?.score ?: 0} دج",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = DzGoldLight
                )
            }

            // Role Assignments Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                borderColor = Color(0x3500E676)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Actor Role Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurfaceVariant)
                            .border(1.5.dp, DzEmeraldGlow.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(DzGreenDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎭", fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("الممثل فهاد الدور:", fontSize = 12.sp, color = TextSecondary)
                            Text(
                                text = actor?.name ?: "—",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = DzEmeraldGlow
                            )
                        }
                    }

                    // Judge Role Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurfaceVariant)
                            .border(1.5.dp, DzGold.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(DzGoldDark.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📱", fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("الحاكم وماسك الهاتف:", fontSize = 12.sp, color = TextSecondary)
                            Text(
                                text = judge?.name ?: "الجميع",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = DzGoldLight
                            )
                        }
                    }
                }
            }

            // Speed Frenzy Rule Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                borderColor = Color(0x30FFB703),
                containerColor = DarkSurface.copy(alpha = 0.6f)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚡", fontSize = 18.sp, modifier = Modifier.padding(end = 6.dp))
                        Text(
                            text = "نظام التحدي السريع المتتابع:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = DzGoldLight
                        )
                    }
                    Text(
                        text = "• عند بدأ العداد تخرج الكلمات متتالية واحدة تلو الأخرى!\n• مثل أكبر عدد من الكلمات قبل انتهاء الوقت ⏱️\n• صح = +50/+100 دج | تخطي = -20 دج 💸",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
