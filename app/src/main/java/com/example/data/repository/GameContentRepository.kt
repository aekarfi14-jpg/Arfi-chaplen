package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AppLanguage
import com.example.data.model.Category
import com.example.data.model.CharadeWord
import com.example.data.model.DefaultWordsData
import com.example.data.model.Difficulty
import com.example.data.model.RandomMemeEvent
import com.example.data.model.SavedMatchRecord
import com.example.data.model.TeamScoreRecord
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class GameContentRepository(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("arfi_chaplen_data", Context.MODE_PRIVATE)

    private val defaultWords: List<CharadeWord> by lazy {
        DefaultWordsData.getAllInitialWords()
    }

    private val customWordsList = mutableListOf<CharadeWord>()

    // Used words tracker for current match to prevent duplicates
    private val usedWordIdsInCurrentMatch = mutableSetOf<String>()
    private var lastSelectedCategory: Category? = null

    // Track recently triggered random events in match
    private val recentEventIds = mutableListOf<String>()

    init {
        loadCustomWords()
    }

    fun getSavedLanguage(): AppLanguage {
        val code = prefs.getString("app_language", "ar") ?: "ar"
        return AppLanguage.fromCode(code)
    }

    fun saveLanguage(language: AppLanguage) {
        prefs.edit().putString("app_language", language.code).apply()
    }

    @Synchronized
    fun getAvailableWords(enabledCategories: Set<Category>): List<CharadeWord> {
        val allEnabledCustom = customWordsList.filter { it.enabled && it.category in enabledCategories }
        val allDefault = defaultWords.filter { it.category in enabledCategories }
        return allDefault + allEnabledCustom
    }

    @Synchronized
    fun getTotalWordsCount(): Int {
        return defaultWords.size + customWordsList.size
    }

    @Synchronized
    fun getCustomWords(): List<CharadeWord> {
        return customWordsList.toList()
    }

    @Synchronized
    fun addCustomWord(text: String, category: Category, difficulty: Difficulty): CharadeWord {
        val word = CharadeWord(
            id = "custom_${UUID.randomUUID().toString().take(8)}",
            text = text.trim(),
            category = category,
            difficulty = difficulty,
            isCustom = true,
            enabled = true
        )
        customWordsList.add(0, word)
        saveCustomWords()
        return word
    }

    @Synchronized
    fun updateCustomWord(id: String, text: String, category: Category, difficulty: Difficulty, enabled: Boolean) {
        val index = customWordsList.indexOfFirst { it.id == id }
        if (index != -1) {
            val updated = customWordsList[index].copy(
                text = text.trim(),
                category = category,
                difficulty = difficulty,
                enabled = enabled
            )
            customWordsList[index] = updated
            saveCustomWords()
        }
    }

    @Synchronized
    fun deleteCustomWord(id: String) {
        customWordsList.removeAll { it.id == id }
        saveCustomWords()
    }

    @Synchronized
    fun resetMatchHistory() {
        usedWordIdsInCurrentMatch.clear()
        lastSelectedCategory = null
        recentEventIds.clear()
    }

    @Synchronized
    fun pickNextWord(enabledCategories: Set<Category>): CharadeWord {
        val candidateCategories = if (enabledCategories.isEmpty()) {
            Category.values().toSet()
        } else {
            enabledCategories
        }
        val allPool = getAvailableWords(candidateCategories)
        if (allPool.isEmpty()) {
            return defaultWords.first()
        }

        // Filter out used words
        var unusedPool = allPool.filter { it.id !in usedWordIdsInCurrentMatch }
        if (unusedPool.isEmpty()) {
            usedWordIdsInCurrentMatch.clear()
            unusedPool = allPool
        }

        // Try to pick from a different category than last picked to maximize variety
        val variedPool = if (candidateCategories.size > 1 && lastSelectedCategory != null) {
            val nonRecent = unusedPool.filter { it.category != lastSelectedCategory }
            if (nonRecent.isNotEmpty()) nonRecent else unusedPool
        } else {
            unusedPool
        }

        val selected = variedPool.random()
        usedWordIdsInCurrentMatch.add(selected.id)
        lastSelectedCategory = selected.category
        return selected
    }

    @Synchronized
    fun pickRandomMemeEvent(): RandomMemeEvent {
        val allEvents = DefaultWordsData.defaultRandomEvents
        val freshEvents = allEvents.filter { it.id !in recentEventIds }
        val pool = if (freshEvents.isNotEmpty()) freshEvents else allEvents
        val event = pool.random()
        recentEventIds.add(event.id)
        if (recentEventIds.size > 5) {
            recentEventIds.removeAt(0)
        }
        return event
    }

    private fun loadCustomWords() {
        customWordsList.clear()
        val jsonString = prefs.getString("custom_words_json", null) ?: return
        try {
            val array = JSONArray(jsonString)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val cat = Category.fromId(obj.optString("category", "dz")) ?: Category.DZ
                val diff = if (obj.optString("difficulty", "EASY") == "HARD") Difficulty.HARD else Difficulty.EASY
                customWordsList.add(
                    CharadeWord(
                        id = obj.optString("id", "custom_$i"),
                        text = obj.optString("text", ""),
                        category = cat,
                        difficulty = diff,
                        isCustom = true,
                        enabled = obj.optBoolean("enabled", true)
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveCustomWords() {
        try {
            val array = JSONArray()
            for (word in customWordsList) {
                val obj = JSONObject()
                obj.put("id", word.id)
                obj.put("text", word.text)
                obj.put("category", word.category.id)
                obj.put("difficulty", word.difficulty.name)
                obj.put("enabled", word.enabled)
                array.put(obj)
            }
            prefs.edit().putString("custom_words_json", array.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun saveMatchRecord(record: SavedMatchRecord) {
        try {
            val currentHistory = getMatchHistory().toMutableList()
            currentHistory.add(0, record)
            val trimmed = currentHistory.take(30)
            val array = JSONArray()
            for (match in trimmed) {
                val obj = JSONObject()
                obj.put("id", match.id)
                obj.put("timestamp", match.timestamp)
                obj.put("winnerTeamName", match.winnerTeamName)
                obj.put("winnerTeamEmoji", match.winnerTeamEmoji)
                obj.put("winnerScore", match.winnerScore)
                obj.put("durationSeconds", match.durationSeconds)
                obj.put("totalWordsPlayed", match.totalWordsPlayed)
                obj.put("correctWordsCount", match.correctWordsCount)

                val teamsArray = JSONArray()
                for (team in match.teamScores) {
                    val tObj = JSONObject()
                    tObj.put("teamName", team.teamName)
                    tObj.put("emoji", team.emoji)
                    tObj.put("score", team.score)
                    teamsArray.put(tObj)
                }
                obj.put("teamScores", teamsArray)
                array.put(obj)
            }
            prefs.edit().putString("match_history_json", array.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun getMatchHistory(): List<SavedMatchRecord> {
        val historyList = mutableListOf<SavedMatchRecord>()
        val jsonString = prefs.getString("match_history_json", null) ?: return emptyList()
        try {
            val array = JSONArray(jsonString)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val teamsArray = obj.optJSONArray("teamScores") ?: JSONArray()
                val teamScores = mutableListOf<TeamScoreRecord>()
                for (j in 0 until teamsArray.length()) {
                    val tObj = teamsArray.getJSONObject(j)
                    teamScores.add(
                        TeamScoreRecord(
                            teamName = tObj.optString("teamName", ""),
                            emoji = tObj.optString("emoji", "🔴"),
                            score = tObj.optInt("score", 0)
                        )
                    )
                }
                historyList.add(
                    SavedMatchRecord(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                        winnerTeamName = obj.optString("winnerTeamName", "الفائز"),
                        winnerTeamEmoji = obj.optString("winnerTeamEmoji", "🏆"),
                        winnerScore = obj.optInt("winnerScore", 0),
                        teamScores = teamScores,
                        durationSeconds = obj.optLong("durationSeconds", 0L),
                        totalWordsPlayed = obj.optInt("totalWordsPlayed", 0),
                        correctWordsCount = obj.optInt("correctWordsCount", 0)
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return historyList
    }

    @Synchronized
    fun clearMatchHistory() {
        prefs.edit().remove("match_history_json").apply()
    }
}
