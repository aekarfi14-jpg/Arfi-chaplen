package com.example.engine

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundManager
import com.example.data.model.*
import com.example.data.repository.GameContentRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class GameUiState(
    val currentScreen: GameScreen = GameScreen.HOME,
    val teams: List<Team> = emptyList(),
    val settings: GameSettings = GameSettings(),
    val currentTeamIndex: Int = 0,
    val currentWord: CharadeWord? = null,
    val isWordRevealed: Boolean = false,
    val isTimerRunning: Boolean = false,
    val remainingSeconds: Int = 60,
    val activeEvent: RandomMemeEvent? = null,
    val isEventDialogVisible: Boolean = false,
    val turnWordHistory: List<TurnResult> = emptyList(),
    val matchWordHistory: List<TurnResult> = emptyList(),
    val matchStartTimeMs: Long = 0L,
    val matchDurationSeconds: Long = 0L,
    val winningTeam: Team? = null,
    val isProcessingAction: Boolean = false, // Double-tap guard
    val funnyBannerMessage: String = "",
    val customWords: List<CharadeWord> = emptyList(),
    val totalWordsInLibrary: Int = 0,
    val showExitConfirmDialog: Boolean = false
) {
    val activeTeam: Team?
        get() = if (teams.isNotEmpty() && currentTeamIndex in teams.indices) teams[currentTeamIndex] else null

    val activeRepresentative: Player?
        get() = activeTeam?.currentRepresentative

    val activeJudge: Player?
        get() = activeTeam?.currentJudge
}

class GameViewModel(application: Application) : AndroidViewModel(application) {

    val repository = GameContentRepository(application)
    val soundManager = SoundManager(application)

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var randomEventCheckedForCurrentTurn = false

    init {
        initializeDefaultGameSetup()
        soundManager.sfxEnabled = _uiState.value.settings.sfxEnabled
        soundManager.musicEnabled = _uiState.value.settings.musicEnabled
        soundManager.currentTrack = _uiState.value.settings.selectedMusicTrack
        soundManager.startMenuMusic(_uiState.value.settings.selectedMusicTrack)
        refreshCustomWordsList()
    }

    private fun initializeDefaultGameSetup() {
        val defaultPlayers = listOf(
            Player(UUID.randomUUID().toString(), "خالد", "team_red"),
            Player(UUID.randomUUID().toString(), "محمد", "team_red"),
            Player(UUID.randomUUID().toString(), "يونس", "team_blue"),
            Player(UUID.randomUUID().toString(), "يوسف", "team_blue"),
            Player(UUID.randomUUID().toString(), "سهيلة", "team_green"),
            Player(UUID.randomUUID().toString(), "أسماء", "team_green"),
            Player(UUID.randomUUID().toString(), "ياسر", "team_yellow"),
            Player(UUID.randomUUID().toString(), "صالح", "team_yellow")
        )

        val redPlayers = defaultPlayers.filter { it.teamId == "team_red" }
        val bluePlayers = defaultPlayers.filter { it.teamId == "team_blue" }
        val greenPlayers = defaultPlayers.filter { it.teamId == "team_green" }
        val yellowPlayers = defaultPlayers.filter { it.teamId == "team_yellow" }

        val initialTeams = listOf(
            Team("team_red", "الفريق الأحمر", 0xFFE63946, "🔴", 0, redPlayers, 0, 1),
            Team("team_blue", "الفريق الأزرق", 0xFF1D3557, "🔵", 0, bluePlayers, 0, 1),
            Team("team_green", "الفريق الأخضر", 0xFF2A9D8F, "🟢", 0, greenPlayers, 0, 1),
            Team("team_yellow", "الفريق الأصفر", 0xFFE76F51, "🟡", 0, yellowPlayers, 0, 1)
        )

        _uiState.update {
            it.copy(
                teams = initialTeams,
                totalWordsInLibrary = repository.getTotalWordsCount()
            )
        }
    }

    fun refreshCustomWordsList() {
        _uiState.update {
            it.copy(
                customWords = repository.getCustomWords(),
                totalWordsInLibrary = repository.getTotalWordsCount()
            )
        }
    }

    fun navigateTo(screen: GameScreen) {
        soundManager.playButtonClick()
        if (screen == GameScreen.ACTIVE_PLAY || screen == GameScreen.PRIVATE_REVEAL) {
            soundManager.stopMenuMusic()
        } else {
            soundManager.startMenuMusic()
        }
        _uiState.update { it.copy(currentScreen = screen) }
    }

    // --- Team & Player Configuration ---
    fun addTeam() {
        if (_uiState.value.teams.size >= 4) return
        val count = _uiState.value.teams.size + 1
        val colorPool = listOf(
            0xFF8338EC to "🟣",
            0xFFFB5607 to "🟠",
            0xFF3A86FF to "🩵",
            0xFFFF006E to "🩷"
        )
        val (color, emoji) = colorPool[(count - 1) % colorPool.size]
        val newTeamId = "team_${UUID.randomUUID().toString().take(6)}"
        val defaultNewPlayer = Player(UUID.randomUUID().toString(), "لاعب 1", newTeamId)
        val newTeam = Team(
            id = newTeamId,
            name = "الفريق $count",
            colorHex = color,
            emoji = emoji,
            score = 0,
            players = listOf(defaultNewPlayer),
            currentRepIndex = 0,
            currentJudgeIndex = 0
        )
        _uiState.update { it.copy(teams = it.teams + newTeam) }
        soundManager.playButtonClick()
    }

    fun removeTeam(teamId: String) {
        if (_uiState.value.teams.size <= 1) return
        _uiState.update { it.copy(teams = it.teams.filter { team -> team.id != teamId }) }
        soundManager.playButtonClick()
    }

    fun renameTeam(teamId: String, newName: String) {
        _uiState.update { state ->
            state.copy(
                teams = state.teams.map { team ->
                    if (team.id == teamId) team.copy(name = newName.ifBlank { team.name }) else team
                }
            )
        }
    }

    fun addPlayerToTeam(teamId: String, playerName: String = "") {
        val state = _uiState.value
        val team = state.teams.find { it.id == teamId } ?: return
        val count = team.players.size + 1
        val name = if (playerName.isNotBlank()) playerName else "لاعب $count"
        val newPlayer = Player(UUID.randomUUID().toString(), name, teamId)

        _uiState.update { s ->
            s.copy(
                teams = s.teams.map { t ->
                    if (t.id == teamId) {
                        val updatedPlayers = t.players + newPlayer
                        t.copy(
                            players = updatedPlayers,
                            currentJudgeIndex = if (updatedPlayers.size > 1 && t.currentJudgeIndex == 0) 1 else t.currentJudgeIndex
                        )
                    } else t
                }
            )
        }
        soundManager.playButtonClick()
    }

    fun removePlayer(teamId: String, playerId: String) {
        _uiState.update { state ->
            state.copy(
                teams = state.teams.map { team ->
                    if (team.id == teamId) {
                        val updated = team.players.filter { it.id != playerId }
                        team.copy(
                            players = updated,
                            currentRepIndex = if (updated.isNotEmpty()) team.currentRepIndex % updated.size else 0,
                            currentJudgeIndex = if (updated.size > 1) (team.currentRepIndex + 1) % updated.size else 0
                        )
                    } else team
                }
            )
        }
        soundManager.playButtonClick()
    }

    fun renamePlayer(teamId: String, playerId: String, newName: String) {
        _uiState.update { state ->
            state.copy(
                teams = state.teams.map { team ->
                    if (team.id == teamId) {
                        team.copy(
                            players = team.players.map { player ->
                                if (player.id == playerId) player.copy(name = newName.ifBlank { player.name }) else player
                            }
                        )
                    } else team
                }
            )
        }
    }

    // --- Settings Updates ---
    fun updateSettings(newSettings: GameSettings) {
        soundManager.sfxEnabled = newSettings.sfxEnabled
        soundManager.musicEnabled = newSettings.musicEnabled
        soundManager.currentTrack = newSettings.selectedMusicTrack
        if (newSettings.musicEnabled && _uiState.value.currentScreen != GameScreen.ACTIVE_PLAY) {
            soundManager.startMenuMusic(newSettings.selectedMusicTrack)
        } else {
            soundManager.stopMenuMusic()
        }
        _uiState.update { it.copy(settings = newSettings) }
    }

    fun selectMusicTrack(track: AlgerianMusicTrack) {
        val updated = _uiState.value.settings.copy(selectedMusicTrack = track)
        soundManager.currentTrack = track
        updateSettings(updated)
        soundManager.playButtonClick()
    }

    fun toggleCategory(category: Category) {
        val currentCats = _uiState.value.settings.enabledCategories.toMutableSet()
        if (category in currentCats) {
            if (currentCats.size > 1) {
                currentCats.remove(category)
            }
        } else {
            currentCats.add(category)
        }
        updateSettings(_uiState.value.settings.copy(enabledCategories = currentCats))
        soundManager.playButtonClick()
    }

    fun selectAllCategories() {
        updateSettings(_uiState.value.settings.copy(enabledCategories = Category.values().toSet()))
        soundManager.playButtonClick()
    }

    // --- Match Flow ---
    fun startNewMatch() {
        repository.resetMatchHistory()
        soundManager.stopMenuMusic()

        // Reset scores
        val resetTeams = _uiState.value.teams.map { team ->
            team.copy(
                score = 0,
                currentRepIndex = 0,
                currentJudgeIndex = if (team.players.size > 1) 1 else 0,
                players = team.players.map { p ->
                    p.copy(score = 0, correctGuesses = 0, skips = 0, badPerformances = 0, representativeTurns = 0, judgeTurns = 0, wordsCompleted = 0)
                }
            )
        }

        _uiState.update {
            it.copy(
                teams = resetTeams,
                currentTeamIndex = 0,
                matchStartTimeMs = System.currentTimeMillis(),
                matchWordHistory = emptyList(),
                turnWordHistory = emptyList(),
                winningTeam = null,
                currentScreen = GameScreen.TURN_INTRO,
                funnyBannerMessage = DefaultWordsData.funnyGuidancePhrases.random()
            )
        }
    }

    /**
     * Rapid Frenzy Mode: Launches active turn immediately with continuous words rolling
     * as soon as timer starts!
     */
    fun startFrenzyRoundDirectly() {
        val state = _uiState.value
        val word = repository.pickNextWord(state.settings.enabledCategories)

        val activeRep = state.activeRepresentative
        val activeJudge = state.activeJudge
        val updatedTeams = state.teams.map { team ->
            if (team.id == state.activeTeam?.id) {
                team.copy(
                    players = team.players.map { p ->
                        when (p.id) {
                            activeRep?.id -> p.copy(representativeTurns = p.representativeTurns + 1)
                            activeJudge?.id -> p.copy(judgeTurns = p.judgeTurns + 1)
                            else -> p
                        }
                    }
                )
            } else team
        }

        soundManager.playTurnStart()
        soundManager.stopMenuMusic()
        randomEventCheckedForCurrentTurn = false

        _uiState.update {
            it.copy(
                teams = updatedTeams,
                currentWord = word,
                remainingSeconds = it.settings.turnDurationSeconds,
                turnWordHistory = emptyList(),
                isTimerRunning = true,
                isProcessingAction = false,
                currentScreen = GameScreen.ACTIVE_PLAY
            )
        }
        startAuthoritativeTimer()
    }

    fun prepareTurnReveal() {
        val state = _uiState.value
        val word = repository.pickNextWord(state.settings.enabledCategories)

        // Increment representative and judge turn count for active players
        val activeRep = state.activeRepresentative
        val activeJudge = state.activeJudge
        val updatedTeams = state.teams.map { team ->
            if (team.id == state.activeTeam?.id) {
                team.copy(
                    players = team.players.map { p ->
                        when (p.id) {
                            activeRep?.id -> p.copy(representativeTurns = p.representativeTurns + 1)
                            activeJudge?.id -> p.copy(judgeTurns = p.judgeTurns + 1)
                            else -> p
                        }
                    }
                )
            } else team
        }

        _uiState.update {
            it.copy(
                teams = updatedTeams,
                currentWord = word,
                isWordRevealed = false,
                remainingSeconds = it.settings.turnDurationSeconds,
                turnWordHistory = emptyList(),
                isTimerRunning = false,
                currentScreen = GameScreen.PRIVATE_REVEAL,
                funnyBannerMessage = "🤫 غير الممثل يشوف الشاشة!"
            )
        }
        soundManager.playButtonClick()
    }

    fun revealSecretWord() {
        _uiState.update { it.copy(isWordRevealed = true) }
        soundManager.playButtonClick()
    }

    fun startActiveTurn() {
        soundManager.playTurnStart()
        soundManager.stopMenuMusic()
        randomEventCheckedForCurrentTurn = false

        _uiState.update {
            it.copy(
                currentScreen = GameScreen.ACTIVE_PLAY,
                isTimerRunning = true,
                isWordRevealed = false, // Hides secret word from judge screen
                isProcessingAction = false
            )
        }
        startAuthoritativeTimer()
    }

    private fun startAuthoritativeTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0 && _uiState.value.isTimerRunning) {
                delay(1000)
                if (!_uiState.value.isTimerRunning) break

                val newSec = _uiState.value.remainingSeconds - 1
                _uiState.update { it.copy(remainingSeconds = newSec) }

                // Audio tick on last 5 seconds
                if (newSec in 1..5) {
                    soundManager.playCountdownTick(newSec)
                }

                // Random event check during active gameplay
                checkAndTriggerRandomEvent(newSec)

                if (newSec <= 0) {
                    endCurrentTurn()
                    break
                }
            }
        }
    }

    private fun checkAndTriggerRandomEvent(remainingSec: Int) {
        val state = _uiState.value
        val freq = state.settings.randomEventFrequency
        if (freq == EventFrequency.OFF || randomEventCheckedForCurrentTurn) return

        val totalDuration = state.settings.turnDurationSeconds
        val elapsed = totalDuration - remainingSec
        // Do not trigger in grace period (first 5 seconds or last 5 seconds)
        if (elapsed >= state.settings.gracePeriodSeconds && remainingSec > 8) {
            val roll = (1..100).random()
            if (roll <= freq.chancePercentage) {
                randomEventCheckedForCurrentTurn = true
                triggerRandomMemeEvent()
            }
        }
    }

    fun triggerRandomMemeEvent() {
        pauseTimer()
        val event = repository.pickRandomMemeEvent()
        soundManager.playRandomEventTrigger()
        _uiState.update {
            it.copy(
                activeEvent = event,
                isEventDialogVisible = true
            )
        }
    }

    fun dismissRandomEventAndResume() {
        _uiState.update {
            it.copy(
                isEventDialogVisible = false,
                activeEvent = null,
                isTimerRunning = true
            )
        }
        soundManager.playButtonClick()
        startAuthoritativeTimer()
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isTimerRunning = false) }
    }

    fun resumeTimer() {
        if (_uiState.value.remainingSeconds > 0) {
            _uiState.update { it.copy(isTimerRunning = true) }
            startAuthoritativeTimer()
        }
    }

    // --- Word Resolution with Double-Tap Protection ---
    fun resolveWord(type: ResolutionType, targetTeamId: String? = null) {
        val state = _uiState.value
        if (state.isProcessingAction || !state.isTimerRunning || state.currentWord == null) return

        _uiState.update { it.copy(isProcessingAction = true) }

        val word = state.currentWord
        val activeTeam = state.activeTeam ?: return
        val activeRep = state.activeRepresentative
        val activeJudge = state.activeJudge

        val pointsDelta = when (type) {
            ResolutionType.CORRECT -> word.points
            ResolutionType.SKIP -> ResolutionType.SKIP.points
            ResolutionType.BAD_PERFORMANCE -> ResolutionType.BAD_PERFORMANCE.points
        }

        when (type) {
            ResolutionType.CORRECT -> soundManager.playCorrect()
            ResolutionType.SKIP -> soundManager.playSkip()
            ResolutionType.BAD_PERFORMANCE -> soundManager.playBadPerformance()
        }

        // For correct answers, points go to the guessing team (targetTeamId or activeTeam)
        // For penalties (SKIP / BAD_PERFORMANCE), penalties ALWAYS apply to activeTeam
        val targetTeamIdResolved = if (type == ResolutionType.CORRECT) {
            targetTeamId ?: activeTeam.id
        } else {
            activeTeam.id
        }

        val turnResult = TurnResult(
            teamId = targetTeamIdResolved,
            repPlayerId = activeRep?.id ?: "",
            judgePlayerId = activeJudge?.id,
            word = word,
            type = type,
            pointsDelta = pointsDelta
        )

        val updatedTeams = state.teams.map { team ->
            when {
                // Case 1: Team that scored points or received penalty
                team.id == targetTeamIdResolved && targetTeamIdResolved == activeTeam.id -> {
                    val newScore = (team.score + pointsDelta).coerceAtLeast(0)
                    team.copy(
                        score = newScore,
                        players = team.players.map { player ->
                            if (player.id == activeRep?.id) {
                                val newPlayerScore = (player.score + pointsDelta).coerceAtLeast(0)
                                when (type) {
                                    ResolutionType.CORRECT -> player.copy(
                                        score = newPlayerScore,
                                        correctGuesses = player.correctGuesses + 1,
                                        wordsCompleted = player.wordsCompleted + 1
                                    )
                                    ResolutionType.SKIP -> player.copy(
                                        score = newPlayerScore,
                                        skips = player.skips + 1,
                                        wordsCompleted = player.wordsCompleted + 1
                                    )
                                    ResolutionType.BAD_PERFORMANCE -> player.copy(
                                        score = newPlayerScore,
                                        badPerformances = player.badPerformances + 1,
                                        wordsCompleted = player.wordsCompleted + 1
                                    )
                                }
                            } else player
                        }
                    )
                }
                // Case 2: Another team guessed correctly (they get the points)
                team.id == targetTeamIdResolved -> {
                    val newScore = (team.score + pointsDelta).coerceAtLeast(0)
                    team.copy(score = newScore)
                }
                // Case 3: Active team when another team guessed correctly (rep still credited for successful act)
                team.id == activeTeam.id && type == ResolutionType.CORRECT -> {
                    team.copy(
                        players = team.players.map { player ->
                            if (player.id == activeRep?.id) {
                                player.copy(
                                    correctGuesses = player.correctGuesses + 1,
                                    wordsCompleted = player.wordsCompleted + 1
                                )
                            } else player
                        }
                    )
                }
                else -> team
            }
        }

        val updatedTurnHistory = state.turnWordHistory + turnResult
        val updatedMatchHistory = state.matchWordHistory + turnResult

        // Check if winning score achieved immediately by any team
        val winningCandidate = updatedTeams.find { it.score >= state.settings.winningScore }
        if (winningCandidate != null) {
            timerJob?.cancel()
            val matchDuration = (System.currentTimeMillis() - state.matchStartTimeMs) / 1000
            soundManager.playVictory()
            recordMatchFinish(winningCandidate, updatedTeams, updatedMatchHistory, state.matchStartTimeMs)

            _uiState.update {
                it.copy(
                    teams = updatedTeams,
                    winningTeam = winningCandidate,
                    isTimerRunning = false,
                    matchDurationSeconds = matchDuration,
                    turnWordHistory = updatedTurnHistory,
                    matchWordHistory = updatedMatchHistory,
                    currentScreen = GameScreen.VICTORY,
                    isProcessingAction = false
                )
            }
            return
        }

        // Generate next word immediately for the same representative
        val nextWord = repository.pickNextWord(state.settings.enabledCategories)

        _uiState.update {
            it.copy(
                teams = updatedTeams,
                currentWord = nextWord,
                turnWordHistory = updatedTurnHistory,
                matchWordHistory = updatedMatchHistory,
                isProcessingAction = false
            )
        }
    }

    private fun recordMatchFinish(
        winner: Team,
        teams: List<Team>,
        wordHistory: List<TurnResult>,
        startTimeMs: Long
    ) {
        val duration = (System.currentTimeMillis() - startTimeMs) / 1000
        val totalWords = wordHistory.size
        val correctWords = wordHistory.count { it.type == ResolutionType.CORRECT }
        val record = com.example.data.model.SavedMatchRecord(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            winnerTeamName = winner.name,
            winnerTeamEmoji = winner.emoji,
            winnerScore = winner.score,
            teamScores = teams.map { com.example.data.model.TeamScoreRecord(teamName = it.name, emoji = it.emoji, score = it.score) },
            durationSeconds = duration,
            totalWordsPlayed = totalWords,
            correctWordsCount = correctWords
        )
        repository.saveMatchRecord(record)
    }

    fun getSavedMatchHistory(): List<com.example.data.model.SavedMatchRecord> = repository.getMatchHistory()

    fun clearSavedMatchHistory() {
        repository.clearMatchHistory()
    }

    private fun endCurrentTurn() {
        timerJob?.cancel()
        soundManager.playTurnEnd()

        val state = _uiState.value
        val currentTeam = state.activeTeam

        // Fair player rotation inside the active team
        val updatedTeams = state.teams.map { team ->
            if (team.id == currentTeam?.id && team.players.isNotEmpty()) {
                val nextRep = (team.currentRepIndex + 1) % team.players.size
                val nextJudge = if (team.players.size > 1) {
                    (nextRep + 1) % team.players.size
                } else 0
                team.copy(
                    currentRepIndex = nextRep,
                    currentJudgeIndex = nextJudge
                )
            } else team
        }

        // Check if any team has won
        val winner = updatedTeams.find { it.score >= state.settings.winningScore }
        if (winner != null) {
            val matchDuration = (System.currentTimeMillis() - state.matchStartTimeMs) / 1000
            soundManager.playVictory()
            recordMatchFinish(winner, updatedTeams, state.matchWordHistory, state.matchStartTimeMs)

            _uiState.update {
                it.copy(
                    teams = updatedTeams,
                    winningTeam = winner,
                    isTimerRunning = false,
                    matchDurationSeconds = matchDuration,
                    currentScreen = GameScreen.VICTORY
                )
            }
            return
        }

        // Move to next team
        val nextTeamIndex = (state.currentTeamIndex + 1) % updatedTeams.size

        _uiState.update {
            it.copy(
                teams = updatedTeams,
                currentTeamIndex = nextTeamIndex,
                isTimerRunning = false,
                currentScreen = GameScreen.TURN_END_SUMMARY
            )
        }
    }

    fun proceedToNextTeamTurnIntro() {
        soundManager.playButtonClick()
        _uiState.update {
            it.copy(
                currentScreen = GameScreen.TURN_INTRO,
                funnyBannerMessage = DefaultWordsData.funnyGuidancePhrases.random()
            )
        }
    }

    fun calculateStatistics(): MatchStatistics {
        val state = _uiState.value
        val allPlayers = state.teams.flatMap { it.players }

        val topScorer = allPlayers.maxByOrNull { it.score }
        val bestActor = allPlayers.maxByOrNull { it.correctGuesses }
        val mostSkips = allPlayers.maxByOrNull { it.skips }
        val mostBadActor = allPlayers.maxByOrNull { it.badPerformances }
        val totalCorrect = state.matchWordHistory.count { it.type == ResolutionType.CORRECT }
        val totalSkips = state.matchWordHistory.count { it.type == ResolutionType.SKIP }
        val totalBad = state.matchWordHistory.count { it.type == ResolutionType.BAD_PERFORMANCE }

        return MatchStatistics(
            totalWordsPlayed = state.matchWordHistory.size,
            totalCorrectWords = totalCorrect,
            totalSkips = totalSkips,
            totalBadPerformances = totalBad,
            totalMatchDurationSeconds = state.matchDurationSeconds,
            topScoringPlayer = topScorer,
            bestActorPlayer = bestActor,
            mostSkipsPlayer = mostSkips,
            mostBadActorPlayer = mostBadActor,
            winningTeam = state.winningTeam ?: state.teams.maxByOrNull { it.score },
            teamRankings = state.teams.sortedByDescending { it.score }
        )
    }

    // --- Custom Word Management ---
    fun addCustomWord(text: String, category: Category, difficulty: Difficulty) {
        if (text.isBlank()) return
        repository.addCustomWord(text, category, difficulty)
        refreshCustomWordsList()
        soundManager.playButtonClick()
    }

    fun updateCustomWord(id: String, text: String, category: Category, difficulty: Difficulty, enabled: Boolean) {
        repository.updateCustomWord(id, text, category, difficulty, enabled)
        refreshCustomWordsList()
    }

    fun deleteCustomWord(id: String) {
        repository.deleteCustomWord(id)
        refreshCustomWordsList()
        soundManager.playButtonClick()
    }

    fun requestExitConfirmation(show: Boolean) {
        _uiState.update { it.copy(showExitConfirmDialog = show) }
    }

    fun exitMatchToHome() {
        timerJob?.cancel()
        soundManager.startMenuMusic()
        _uiState.update {
            it.copy(
                isTimerRunning = false,
                currentScreen = GameScreen.HOME,
                showExitConfirmDialog = false
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundManager.release()
    }
}
