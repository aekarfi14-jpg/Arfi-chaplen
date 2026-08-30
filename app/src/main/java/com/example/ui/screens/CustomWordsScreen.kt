package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Category
import com.example.data.model.CharadeWord
import com.example.data.model.Difficulty
import com.example.data.model.GameScreen
import com.example.engine.GameViewModel
import com.example.i18n.AppStrings
import com.example.ui.components.ArfiTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.atmosphericBackground
import com.example.ui.theme.*

@Composable
fun CustomWordsScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.settings.appLanguage
    var showAddDialog by remember { mutableStateOf(false) }
    var editingWord by remember { mutableStateOf<CharadeWord?>(null) }
    var inputText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category.DZ) }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.EASY) }

    Scaffold(
        topBar = {
            ArfiTopBar(
                title = AppStrings.customWordsTitle(lang),
                onBack = { viewModel.navigateTo(GameScreen.HOME) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    inputText = ""
                    selectedCategory = Category.DZ
                    selectedDifficulty = Difficulty.EASY
                    editingWord = null
                    showAddDialog = true
                },
                containerColor = DzGreen,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.testTag("add_custom_word_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(AppStrings.addCustomWordBtn(lang), fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 80.dp)
        ) {
            if (uiState.customWords.isEmpty()) {
                item {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("✨", fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = AppStrings.noCustomWordsYet(lang),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                            Text(
                                text = AppStrings.noCustomWordsTip(lang),
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            } else {
                items(uiState.customWords, key = { it.id }) { word ->
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        borderColor = Color(0x25FFFFFF)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = word.text,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "${word.category.icon} ${AppStrings.categoryName(word.category, lang)} • ${if (word.difficulty == Difficulty.HARD) (if (lang == AppLanguage.ENGLISH) "Hard (+100)" else "صعيب (+100)") else (if (lang == AppLanguage.ENGLISH) "Easy (+50)" else "سهل (+50)")}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        editingWord = word
                                        inputText = word.text
                                        selectedCategory = word.category
                                        selectedDifficulty = word.difficulty
                                    }
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DzGold)
                                }
                                IconButton(
                                    onClick = { viewModel.deleteCustomWord(word.id) }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DzRed)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Custom Word Dialog
    if (showAddDialog || editingWord != null) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
                editingWord = null
            },
            title = {
                Text(
                    text = if (editingWord != null) (if (lang == AppLanguage.ENGLISH) "Edit Word" else "تعديل الكلمة") else (if (lang == AppLanguage.ENGLISH) "Add New Custom Word" else "إضافة كلمة مخصصة جديدة"),
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = { Text(AppStrings.wordTextInputLabel(lang)) },
                        placeholder = { Text(if (lang == AppLanguage.ENGLISH) "e.g. Drinking hot tea" else "مثال: يونس يطلب قهوة كحلة") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = DzEmeraldGlow
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(AppStrings.categoryLabel(lang), fontSize = 13.sp, color = TextSecondary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Category.values().take(4).forEach { cat ->
                            val isSel = selectedCategory == cat
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedCategory = cat },
                                label = { Text(AppStrings.categoryName(cat, lang).take(8), fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DzGreen)
                            )
                        }
                    }

                    Text(AppStrings.difficultyLabel(lang), fontSize = 13.sp, color = TextSecondary)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(
                            selected = selectedDifficulty == Difficulty.EASY,
                            onClick = { selectedDifficulty = Difficulty.EASY },
                            label = { Text(AppStrings.easyDiffLabel(lang)) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DzGreen)
                        )
                        FilterChip(
                            selected = selectedDifficulty == Difficulty.HARD,
                            onClick = { selectedDifficulty = Difficulty.HARD },
                            label = { Text(AppStrings.hardDiffLabel(lang)) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DzRedDark)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            if (editingWord != null) {
                                viewModel.updateCustomWord(
                                    id = editingWord!!.id,
                                    text = inputText,
                                    category = selectedCategory,
                                    difficulty = selectedDifficulty,
                                    enabled = true
                                )
                            } else {
                                viewModel.addCustomWord(
                                    text = inputText,
                                    category = selectedCategory,
                                    difficulty = selectedDifficulty
                                )
                            }
                            showAddDialog = false
                            editingWord = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DzGreen)
                ) {
                    Text(AppStrings.saveWordBtn(lang), color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false
                    editingWord = null
                }) {
                    Text(AppStrings.cancelBtn(lang), color = TextSecondary)
                }
            },
            containerColor = DarkCardElevated
        )
    }
}
