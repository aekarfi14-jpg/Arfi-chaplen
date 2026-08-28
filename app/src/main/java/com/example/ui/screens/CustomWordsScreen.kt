package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.Category
import com.example.data.model.CharadeWord
import com.example.data.model.Difficulty
import com.example.data.model.GameScreen
import com.example.engine.GameViewModel
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
    var showAddDialog by remember { mutableStateOf(false) }
    var editingWord by remember { mutableStateOf<CharadeWord?>(null) }

    var inputText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category.DZ) }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.EASY) }

    Scaffold(
        topBar = {
            ArfiTopBar(
                title = "📚 بنك الكلمات والتخصيص",
                onBack = { viewModel.navigateTo(GameScreen.HOME) },
                sfxEnabled = uiState.settings.sfxEnabled,
                musicEnabled = uiState.settings.musicEnabled,
                onToggleSfx = { viewModel.updateSettings(uiState.settings.copy(sfxEnabled = !uiState.settings.sfxEnabled)) },
                onToggleMusic = { viewModel.updateSettings(uiState.settings.copy(musicEnabled = !uiState.settings.musicEnabled)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    inputText = ""
                    selectedCategory = Category.DZ
                    selectedDifficulty = Difficulty.EASY
                    showAddDialog = true
                },
                containerColor = DzEmeraldGlow,
                contentColor = Color.Black,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_add_custom_word")
            ) {
                Icon(Icons.Default.Add, contentDescription = "إضافة كلمة مخصصة")
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
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
        ) {
            // Library summary header
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = Color(0x35FFB703)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "🇩🇿 مكتبة الكلمات الجزائرية الجاهزة:",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = DzGoldLight
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "تحتوي اللعبة على أكثر من ${uiState.totalWordsInLibrary} كلمة وموقف جزائري بدون إنترنت! ويمكنك أيضاً إضافة كلماتك الخاصة مع أصحابك وعائلتك 💡",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✍️ كلماتك المخصصة (${uiState.customWords.size}):",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                }
            }

            if (uiState.customWords.isEmpty()) {
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        borderColor = Color(0x20FFFFFF),
                        containerColor = DarkSurface.copy(alpha = 0.5f)
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
                                text = "مازال ما أضفت حتى كلمة مخصصة",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "اضغط على الزر الأخضر بالأسفل لإضافة أسماء أصدقائك أو نكت خاصة بقعدتكم!",
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
                                    text = "${word.category.icon} ${word.category.displayName} • ${if (word.difficulty == Difficulty.HARD) "صعيب (+100)" else "سهل (+50)"}",
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
                                    Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = DzGold)
                                }

                                IconButton(
                                    onClick = { viewModel.deleteCustomWord(word.id) }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "حذف", tint = DzRed)
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
                    text = if (editingWord != null) "تعديل الكلمة" else "إضافة كلمة مخصصة جديدة",
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
                        label = { Text("نص الكلمة أو العبارة") },
                        placeholder = { Text("مثال: يونس يطلب قهوة كحلة") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = DzEmeraldGlow
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("الفئة:", fontSize = 13.sp, color = TextSecondary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Category.values().take(4).forEach { cat ->
                            val isSel = selectedCategory == cat
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat.displayName.take(7), fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DzGreen)
                            )
                        }
                    }

                    Text("الصعوبة:", fontSize = 13.sp, color = TextSecondary)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(
                            selected = selectedDifficulty == Difficulty.EASY,
                            onClick = { selectedDifficulty = Difficulty.EASY },
                            label = { Text("🟢 سهل (+50 دج)") },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DzGreen)
                        )
                        FilterChip(
                            selected = selectedDifficulty == Difficulty.HARD,
                            onClick = { selectedDifficulty = Difficulty.HARD },
                            label = { Text("🔴 صعيب (+100 دج)") },
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
                    Text("حفظ الكلمة", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false
                    editingWord = null
                }) {
                    Text("إلغاء", color = TextSecondary)
                }
            },
            containerColor = DarkCardElevated
        )
    }
}
