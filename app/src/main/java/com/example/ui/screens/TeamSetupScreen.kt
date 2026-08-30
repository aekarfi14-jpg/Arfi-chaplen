package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonAdd
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
import com.example.data.model.AppLanguage
import com.example.data.model.GameScreen
import com.example.data.model.Player
import com.example.data.model.Team
import com.example.engine.GameViewModel
import com.example.i18n.AppStrings
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun TeamSetupScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.settings.appLanguage
    var editingTeam by remember { mutableStateOf<Team?>(null) }
    var editingPlayer by remember { mutableStateOf<Pair<String, Player>?>(null) }
    var newPlayerTeamId by remember { mutableStateOf<String?>(null) }
    var inputDialogText by remember { mutableStateOf("") }

    val allTeamsHaveMinPlayers = uiState.teams.all { it.players.isNotEmpty() }

    Scaffold(
        topBar = {
            ArfiTopBar(
                title = AppStrings.teamSetupTitle(lang),
                onBack = { viewModel.navigateTo(GameScreen.HOME) }
            )
        },
        bottomBar = {
            Surface(
                color = DarkBackground.copy(alpha = 0.95f),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!allTeamsHaveMinPlayers) {
                        Text(
                            text = AppStrings.minPlayerWarning(lang),
                            color = DzRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = { viewModel.navigateTo(GameScreen.SETUP_CATEGORIES) },
                        enabled = allTeamsHaveMinPlayers,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("btn_proceed_categories"),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DzGreen,
                            disabledContainerColor = DarkSurfaceVariant
                        )
                    ) {
                        Text(
                            text = AppStrings.continueToCategories(lang),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = if (allTeamsHaveMinPlayers) Color.White else TextMuted
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
            contentPadding = PaddingValues(top = 10.dp, bottom = 16.dp)
        ) {
            // Team count selector segment
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = AppStrings.teamCountLabel(lang),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DzGoldLight
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(2, 3, 4).forEach { count ->
                                val isSelected = uiState.teams.size == count
                                val label = when (count) {
                                    2 -> AppStrings.twoTeams(lang)
                                    3 -> AppStrings.threeTeams(lang)
                                    else -> AppStrings.fourTeams(lang)
                                }
                                Button(
                                    onClick = { viewModel.setTeamCount(count) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSelected) DzGreen else DarkSurfaceVariant,
                                        contentColor = if (isSelected) Color.White else TextMuted
                                    )
                                ) {
                                    Text(
                                        text = label,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Teams list
            items(uiState.teams.size) { index ->
                val team = uiState.teams[index]
                val teamColor = Color(team.colorHex)

                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = teamColor.copy(alpha = 0.6f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Team Header with Name and Edit icon
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(teamColor.copy(alpha = 0.3f))
                                        .border(1.5.dp, teamColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(team.emoji, fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = team.name,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 17.sp,
                                    color = Color.White
                                )
                            }
                            IconButton(
                                onClick = {
                                    editingTeam = team
                                    inputDialogText = team.name
                                }
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DzGoldLight)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = Color(0x15FFFFFF))
                        Spacer(modifier = Modifier.height(10.dp))

                        // Players inside this team
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            team.players.forEachIndexed { pIdx, player ->
                                val isRep = pIdx == team.currentRepIndex % (team.players.size.coerceAtLeast(1))
                                val isJudge = team.players.size > 1 && pIdx == team.currentJudgeIndex % (team.players.size.coerceAtLeast(1))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(DarkSurfaceVariant.copy(alpha = 0.6f))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${pIdx + 1}. ${player.name}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                        if (isRep) {
                                            Text(
                                                text = AppStrings.roleActorTag(lang),
                                                fontSize = 11.sp,
                                                color = DzEmeraldGlow,
                                                fontWeight = FontWeight.Bold
                                            )
                                        } else if (isJudge) {
                                            Text(
                                                text = AppStrings.roleJudgeTag(lang),
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
                                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DzGoldLight, modifier = Modifier.size(16.dp))
                                        }
                                        if (team.players.size > 1) {
                                            IconButton(
                                                onClick = { viewModel.removePlayer(team.id, player.id) },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = "Delete", tint = DzRed, modifier = Modifier.size(16.dp))
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
                                Text(AppStrings.addPlayerToTeam(lang), fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
            title = { Text(AppStrings.editTeamNameDialogTitle(lang), color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = inputDialogText,
                    onValueChange = { inputDialogText = it },
                    label = { Text(AppStrings.teamNameField(lang)) },
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
                    Text(AppStrings.saveBtn(lang), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingTeam = null }) {
                    Text(AppStrings.cancelBtn(lang), color = TextSecondary)
                }
            },
            containerColor = DarkCard
        )
    }

    // Rename Player Dialog
    editingPlayer?.let { (teamId, player) ->
        AlertDialog(
            onDismissRequest = { editingPlayer = null },
            title = { Text(AppStrings.editPlayerNameDialogTitle(lang), color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = inputDialogText,
                    onValueChange = { inputDialogText = it },
                    label = { Text(AppStrings.playerNameField(lang)) },
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
                    Text(AppStrings.saveBtn(lang), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingPlayer = null }) {
                    Text(AppStrings.cancelBtn(lang), color = TextSecondary)
                }
            },
            containerColor = DarkCard
        )
    }

    // Add Player Dialog
    newPlayerTeamId?.let { teamId ->
        AlertDialog(
            onDismissRequest = { newPlayerTeamId = null },
            title = { Text(AppStrings.addNewPlayerDialogTitle(lang), color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = inputDialogText,
                    onValueChange = { inputDialogText = it },
                    label = { Text(AppStrings.playerNameField(lang)) },
                    placeholder = { Text(if (lang == AppLanguage.ENGLISH) "e.g. Sam" else "مثال: أمين") },
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
                    Text(AppStrings.addBtn(lang), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { newPlayerTeamId = null }) {
                    Text(AppStrings.cancelBtn(lang), color = TextSecondary)
                }
            },
            containerColor = DarkCard
        )
    }
}
