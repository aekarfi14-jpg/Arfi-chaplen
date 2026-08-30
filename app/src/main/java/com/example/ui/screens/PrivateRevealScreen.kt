package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
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
import com.example.data.model.GameScreen
import com.example.engine.GameViewModel
import com.example.i18n.AppStrings
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.CreatorCreditFooter
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun PrivateRevealScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeRep = uiState.activeRepresentative
    val activeJudge = uiState.activeJudge
    val activeWord = uiState.currentWord
    val lang = uiState.settings.appLanguage
    var isRevealed by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ArfiTopBar(
                title = AppStrings.secretRevealTitle(lang),
                onBack = { viewModel.navigateTo(GameScreen.TURN_INTRO) }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .atmosphericBackground()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Instructions
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${AppStrings.actorRoleLabel(lang)} ${activeRep?.name ?: ""}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = DzEmeraldGlow
                )
                Text(
                    text = "${AppStrings.judgeRoleLabel(lang)} ${activeJudge?.name ?: activeRep?.name ?: ""}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DzGoldLight
                )
                Text(
                    text = AppStrings.secretRevealInstructions(lang),
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            // Word Reveal Card / Mask
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { isRevealed = !isRevealed }
                    .testTag("secret_reveal_card"),
                shape = RoundedCornerShape(24.dp),
                borderColor = if (isRevealed) DzEmeraldGlow else DzGold,
                borderWidth = 2.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isRevealed) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "Tap to Reveal",
                                modifier = Modifier.size(54.dp),
                                tint = DzGold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = AppStrings.tapToReveal(lang),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Badge(containerColor = DarkSurfaceVariant) {
                                    Text(
                                        text = "${activeWord?.category?.icon ?: ""} ${AppStrings.categoryName(activeWord?.category ?: com.example.data.model.Category.DZ, lang)}",
                                        color = DzGoldLight,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                                val isHard = activeWord?.difficulty == Difficulty.HARD
                                Badge(containerColor = if (isHard) DzRedDark else DzGreenDark) {
                                    Text(
                                        text = if (isHard) (if (lang == AppLanguage.ENGLISH) "Hard (+100)" else "صعيب (+100)") else (if (lang == AppLanguage.ENGLISH) "Easy (+50)" else "سهل (+50)"),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = activeWord?.text ?: "",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                lineHeight = 42.sp
                            )
                        }
                    }
                }
            }

            // Start Direct Frenzy Button
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.startActiveTurn() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .testTag("btn_start_active_turn"),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DzGreen),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.startActiveTurn(lang),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                CreatorCreditFooter()
            }
        }
    }
}
