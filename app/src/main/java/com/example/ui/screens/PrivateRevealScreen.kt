package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.data.model.Difficulty
import com.example.engine.GameViewModel
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.DinarBadge
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun PrivateRevealScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val word = uiState.currentWord
    val actor = uiState.activeRepresentative
    var isRevealed by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ArfiTopBar(
                title = "🤫 كشف الكلمة بالسر للممثل",
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { viewModel.startActiveTurn() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .testTag("start_active_turn_btn"),
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
                            text = "⏱️ ابدأ الدور وسلم الهاتف للحاكم!",
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .atmosphericBackground()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Actor Notice
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "يا ${actor?.name ?: "الممثل"}، شوف الكلمة وحدك 🤫",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = DzGoldLight,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "ممنوع تنطق أي كلمة أو صوت أثناء التمثيل!",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            // Word Secret Reveal Card
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .clickable { isRevealed = !isRevealed }
                    .testTag("secret_word_card"),
                shape = RoundedCornerShape(24.dp),
                borderColor = if (isRevealed) DzGold else Color(0x3500E676),
                borderWidth = 2.dp,
                containerColor = DarkCard
            ) {
                AnimatedContent(
                    targetState = isRevealed,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                    },
                    label = "secretRevealAnim",
                    modifier = Modifier.fillMaxSize()
                ) { revealed ->
                    if (!revealed) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(DarkSurfaceVariant)
                                    .border(1.5.dp, DzEmeraldGlow, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = DzEmeraldGlow,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "اضغط لكشف الكلمة 👁️",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "تأكد بلي صحابك ما راهمش يشوفوا فالشاشة",
                                fontSize = 13.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (word != null) {
                                // Category Pill
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(DarkSurfaceVariant)
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(word.category.icon, fontSize = 14.sp, modifier = Modifier.padding(end = 6.dp))
                                    Text(
                                        text = word.category.displayName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DzGoldLight
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Word text
                                Text(
                                    text = word.text,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 38.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Points & Difficulty
                                DinarBadge(
                                    points = if (word.difficulty == Difficulty.HARD) 100 else 50,
                                    isLarge = true
                                )
                            }
                        }
                    }
                }
            }

            // Advice note
            Text(
                text = "💡 بعد ما تحفظ الكلمة، اضغط على الزر بالأسفل وسلم الهاتف للحاكم باش يبدأ التوقيت فوراً.",
                fontSize = 12.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
