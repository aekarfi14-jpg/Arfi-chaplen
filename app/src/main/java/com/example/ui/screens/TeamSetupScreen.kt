package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameScreen
import com.example.data.model.Player
import com.example.data.model.Team
import com.example.engine.GameViewModel
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.SetupStepIndicator
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun TeamSetupScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var expandedTeamId by remember { mutableStateOf<String?>(uiState.teams.firstOrNull()?.id) }

    // Dialog state for renaming or adding
    var editingTeam by remember { mutableStateOf<Team?>(null) }
    var editingPlayer by remember { mutableStateOf<Pair<String, Player>?>(null) }
    var newPlayerTeamId by remember { mutableStateOf<String?>(null) }
    var inputDialogText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(DarkSurface.copy(alpha = 0.95f))) {
                ArfiTopBar(
                    title = "👥 إعداد الفرق واللاعبين",
                    onBack = { viewModel.navigateTo(GameScreen.HOME) },
                    sfxEnabled = uiState.settings.sfxEnabled,
                    musicEnabled = uiState.settings.musicEnabled,
                    onToggleSfx = { viewModel.updateSettings(uiState.settings.copy(sfxEnabled = !uiState.settings.sfxEnabled)) },
                    onToggleMusic = { viewModel.updateSettings(uiState.settings.copy(musicEnabled = !uiState.settings.musicEnabled)) }
                )
                SetupStepIndicator(currentStep = 1)
            }
        },
        bottomBar = {
            Surface(
                color = DarkSurface.copy(alpha = 0.95f),
                tonalElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { viewModel.addTeam() },
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .testTag("add_team_btn"),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DzGold),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(listOf(DzGold, DzGoldDark))
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("إضافة فريق", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { viewModel.navigateTo(GameScreen.SETUP_CATEGORIES) },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(54.dp)
                            .testTag("next_to_categories_btn"),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DzEmeraldGlow)
                    ) {
                        Text(
                            text = "التالي: الفئات ➡️",
                            fontSize = 16.sp,
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 20.dp)
        ) {
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    borderColor = Color(0x3500E676)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("💡", fontSize = 22.sp, modifier = Modifier.padding(end = 10.dp))
                        Text(
                            text = "نظام التدوير العادل: اللاعبين يتناوبوا تلقائياً كل دور بين التمثيل ومسك الهاتف كحاكم بكل عدل ومرح!",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            itemsIndexed(uiState.teams, key = { _, team -> team.id }) { _, team ->
                val isExpanded = expandedTeamId == team.id
                val teamColor = Color(team.colorHex)

                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = if (isExpanded) teamColor else Color(0x25FFFFFF),
                    borderWidth = if (isExpanded) 1.5.dp else 1.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Team Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedTeamId = if (isExpanded) null else team.id }
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
                                        .background(teamColor.copy(alpha = 0.3f))
                                        .border(2.dp, teamColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(team.emoji, fontSize = 20.sp)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = team.name,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 17.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${team.players.size} لاعبين • الممثل الأول: ${team.currentRepresentative?.name ?: "—"}",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        editingTeam = team
                                        inputDialogText = team.name
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "تعديل اسم الفريق", tint = DzGold, modifier = Modifier.size(20.dp))
                                }

                                if (uiState.teams.size > 1) {
                                    IconButton(
                                        onClick = { viewModel.removeTeam(team.id) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "حذف الفريق", tint = DzRed, modifier = Modifier.size(20.dp))
                                    }
                                }

                                IconButton(
                                    onClick = { expandedTeamId = if (isExpanded) null else team.id },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = "عرض اللاعبين",
                                        tint = TextSecondary
                                    )
                                }
                            }
                        }

                        // Collapsible Players Drawer
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(DarkSurfaceVariant.copy(alpha = 0.5f))
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "أعضاء الفريق:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = DzGoldLight
                                    )
                                    Text(
                                        text = "اضغط على القلم للتعديل",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                team.players.forEachIndexed { pIdx, player ->
                                    val isRep = pIdx == team.currentRepIndex % (team.players.size.coerceAtLeast(1))
                                    val isJudge = pIdx == team.currentJudgeIndex % (team.players.size.coerceAtLeast(1)) && team.players.size > 1

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(DarkCard)
                                            .border(
                                                width = 1.dp,
                                                color = if (isRep) DzEmeraldGlow.copy(alpha = 0.5f) else Color(0x15FFFFFF),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = if (isRep) "🎭" else if (isJudge) "📱" else "👤",
                                                fontSize = 18.sp,
                                                modifier = Modifier.padding(end = 8.dp)
                                            )
                                            Text(
                                                text = player.name,
                                                fontSize = 15.sp,
                                                color = Color.White,
                                                fontWeight = if (isRep || isJudge) FontWeight.Bold else FontWeight.Normal
                                            )
                                            if (isRep) {
                                                Text(
                                                    text = " (الممثل)",
                                                    fontSize = 11.sp,
                                                    color = DzEmeraldGlow,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            } else if (isJudge) {
                                                Text(
                                                    text = " (الحاكم)",
                                                    fontSize = 11.sp,
                                                    color = DzGold,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        Row {
                                            IconButton(
                                                onClick = {
                                                    editingPlayer = team.id to player
                                                    inputDialogText = player.name
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = DzGoldLight, modifier = Modifier.size(16.dp))
                                            }

                                            if (team.players.size > 1) {
                                                IconButton(
                                                    onClick = { viewModel.removePlayer(team.id, player.id) },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(Icons.Default.Close, contentDescription = "حذف", tint = DzRed, modifier = Modifier.size(16.dp))
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedButton(
                                    onClick = {
                                        newPlayerTeamId = team.id
                                        inputDialogText = ""
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DzEmeraldGlow),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        brush = Brush.horizontalGradient(listOf(DzEmeraldGlow, DzGreenDark))
                                    )
                                ) {
                                    Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("إضافة لاعب لهذا الفريق", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Rename Team Dialog
    editingTeam?.let { team ->
        AlertDialog(
            onDismissRequest = { editingTeam = null },
            title = { Text("تعديل اسم الفريق", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = inputDialogText,
                    onValueChange = { inputDialogText = it },
                    label = { Text("اسم الفريق") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = DzEmeraldGlow
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.renameTeam(team.id, inputDialogText)
                        editingTeam = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DzGreen)
                ) {
                    Text("حفظ", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingTeam = null }) {
                    Text("إلغاء", color = TextSecondary)
                }
            },
            containerColor = DarkCard
        )
    }

    // Rename Player Dialog
    editingPlayer?.let { (teamId, player) ->
        AlertDialog(
            onDismissRequest = { editingPlayer = null },
            title = { Text("تعديل اسم اللاعب", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = inputDialogText,
                    onValueChange = { inputDialogText = it },
                    label = { Text("اسم اللاعب") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = DzEmeraldGlow
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.renamePlayer(teamId, player.id, inputDialogText)
                        editingPlayer = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DzGreen)
                ) {
                    Text("حفظ", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingPlayer = null }) {
                    Text("إلغاء", color = TextSecondary)
                }
            },
            containerColor = DarkCard
        )
    }

    // Add Player Dialog
    newPlayerTeamId?.let { teamId ->
        AlertDialog(
            onDismissRequest = { newPlayerTeamId = null },
            title = { Text("إضافة لاعب جديد", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = inputDialogText,
                    onValueChange = { inputDialogText = it },
                    label = { Text("اسم اللاعب") },
                    placeholder = { Text("مثال: أمين") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = DzEmeraldGlow
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addPlayerToTeam(teamId, inputDialogText)
                        newPlayerTeamId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DzGreen)
                ) {
                    Text("إضافة", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { newPlayerTeamId = null }) {
                    Text("إلغاء", color = TextSecondary)
                }
            },
            containerColor = DarkCard
        )
    }
}
