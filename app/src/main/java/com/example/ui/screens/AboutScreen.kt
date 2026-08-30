package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.GameScreen
import com.example.engine.GameViewModel
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.CreatorCreditFooter
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun AboutScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            ArfiTopBar(
                title = "ℹ️ حول اللعبة والقواعد",
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
        ) {
            // App Banner Header
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(DzGold.copy(alpha = 0.35f), Color.Transparent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon),
                            contentDescription = "أيقونة اللعبة",
                            modifier = Modifier
                                .size(86.dp)
                                .clip(CircleShape)
                                .border(2.5.dp, DzGold, CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "🎭 ARFI CHAPLEN",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "عرفي شابلن — النسخة الجزائرية الأصلية 🇩🇿",
                        fontSize = 14.sp,
                        color = DzGoldLight,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Creator story card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    borderColor = Color(0x50FFB703),
                    borderWidth = 1.5.dp
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🗿", fontSize = 28.sp, modifier = Modifier.padding(end = 10.dp))
                            Text(
                                text = "حكاية صنع اللعبة:",
                                fontWeight = FontWeight.Black,
                                fontSize = 17.sp,
                                color = DzGoldLight
                            )
                        }
                        Text(
                            text = "صنعها يونس الشكور 🗿 باش ما تبقاش القعدة ميتة وكل واحد شاد تليفونو وحدو!",
                            fontSize = 14.sp,
                            color = Color.White,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = "الفكرة بدات بقعدة جزائرية عادية وضحك مع الصحاب، ومن بعد يونس قرر يدير منها تطبيق جزائري 100% يجمع العائلة والأصدقاء بالتمثيل والميمز والتحديات 🔥🇩🇿",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 19.sp
                        )
                    }
                }
            }

            // Rules Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    borderColor = Color(0x3500E676)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "📜 قوانين القعدة واللعب:",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = DzEmeraldGlow
                        )

                        val rules = listOf(
                            "1️⃣ التناوب العادل: كل فريق عندو ممثل وحاكم يتناوبوا تلقائياً كل دور.",
                            "2️⃣ الكتمان التام: الممثل يشوف الكلمة بالسر، وممنوع عليه ينطق حتى حرف أو صوت!",
                            "3️⃣ نظام النقاط: الفريق الذي يعرف الكلمة أولاً ينال النقاط (+50 أو +100 دج).",
                            "4️⃣ العقوبات: التخطي = -20 دج، والتمثيل السيء = -5 دج وتُخصم من الفريق الممثل.",
                            "5️⃣ الأحداث المفاجئة: إذا تفعلت، توقف الوقت وتطلب حركة مضحكة من الممثل.",
                            "6️⃣ الفوز: أول فريق يوصل للهدف المحدد يربح البطولة والكأس 🏆."
                        )

                        rules.forEach { rule ->
                            Text(
                                text = rule,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }

            // Offline Guarantee Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = Color(0x20FFFFFF),
                    containerColor = DarkSurfaceVariant.copy(alpha = 0.6f)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "📴 100% أوفلاين وبدون إنترنت",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Text(
                            text = "اللعبة مصممة لتشتغل في أي مكان (الصحراء، البحر، السطح، القهوة) بدون الحاجة لأي اتصال بالإنترنت أو حسابات.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            item {
                CreatorCreditFooter()
            }
        }
    }
}
