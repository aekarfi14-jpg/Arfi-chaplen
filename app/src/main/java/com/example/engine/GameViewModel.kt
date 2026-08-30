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
    val isProcessingAction: Boolean = false,
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
        val initialLanguage = repository.getSavedLanguage()
        val initialSettings = GameSettings(appLanguage = initialLanguage)
        _uiState.update { it.copy(settings = initialSettings) }

        initializeDefaultGameSetup()
        soundManager.sfxEnabled = initialSettings.sfxEnabled
        soundManager.musicEnabled = initialSettings.musicEnabled
        soundManager.currentTrack = initialSettings.selectedMusicTrack
        soundManager.startMenuMusic(initialSettings.selectedMusicTrack)
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

    fun setLanguage(language: AppLanguage) {
        repository.saveLanguage(language)
        _uiState.update {
            it.copy(settings = it.settings.copy(appLanguage = language))
        }
        soundManager.playButtonClick()
    }

    fun toggleLanguage() {
        val current = _uiState.value.settings.appLanguage
        val next = if (current == AppLanguage.ARABIC) AppLanguage.ENGLISH else AppLanguage.ARABIC
        setLanguage(next)
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

    fun updateSettings(newSettings: GameSettings) {
        soundManager.sfxEnabled = newSettings.sfxEnabled
        soundManager.musicEnabled = newSettings.musicEnabled
        if (newSettings.selectedMusicTrack != _uiState.value.settings.selectedMusicTrack) {
            soundManager.currentTrack = newSettings.selectedMusicTrack
            soundManager.startMenuMusic(newSettings.selectedMusicTrack)
        }
        _uiState.update { it.copy(settings = newSettings) }
    }

    fun selectMusicTrack(track: AlgerianMusicTrack) {
        soundManager.currentTrack = track
        soundManager.startMenuMusic(track)
        _uiState.update {
            it.copy(settings = it.settings.copy(selectedMusicTrack = track, musicEnabled = true))
        }
    }

    fun toggleCategory(category: Category) {
        val currentSet = _uiState.value.settings.enabledCategories.toMutableSet()
        if (currentSet.contains(category)) {
            if (currentSet.size > 1) {
                currentSet.remove(category)
            }
        } else {
            currentSet.add(category)
        }
        soundManager.playButtonClick()
        _uiState.update {
            it.copy(settings = it.settings.copy(enabledCategories = currentSet))
        }
    }

    fun setTeamCount(count: Int) {
        val currentTeams = _uiState.value.teams
        val newTeams = when (count) {
            2 -> currentTeams.take(2)
            3 -> {
                if (currentTeams.size >= 3) currentTeams.take(3)
                else currentTeams + Team("team_green", "الفريق الأخضر", 0xFF2A9D8F, "🟢", 0, listOf(Player(UUID.randomUUID().toString(), "سهيلة", "team_green"), Player(UUID.randomUUID().toString(), "أسماء", "team_green")), 0, 1)
            }
            4 -> {
                val fullList = mutableListOf<Team>()
                fullList.addAll(currentTeams)
                if (fullList.none { it.id == "team_green" }) {
                    fullList.add(Team("team_green", "الفريق الأخضر", 0xFF2A9D8F, "🟢", 0, listOf(Player(UUID.randomUUID().toString(), "سهيلة", "team_green"), Player(UUID.randomUUID().toString(), "أسماء", "team_green")), 0, 1))
                }
                if (fullList.none { it.id == "team_yellow" }) {
                    fullList.add(Team("team_yellow", "الفريق الأصفر", 0xFFE76F51, "🟡", 0, listOf(Player(UUID.randomUUID().toString(), "ياسر", "team_yellow"), Player(UUID.randomUUID().toString(), "صالح", "team_yellow")), 0, 1))
                }
                fullList.take(4)
            }
            else -> currentTeams
        }
        soundManager.playButtonClick()
        _uiState.update { it.copy(teams = newTeams) }
    }

    fun renameTeam(teamId: String, newName: String) {
        if (newName.isBlank()) return
        _uiState.update { state ->
            state.copy(
                teams = state.teams.map {
                    if (it.id == teamId) it.copy(name = newName.trim()) else it
                }
            )
        }
    }

    fun renamePlayer(teamId: String, playerId: String, newName: String) {
        if (newName.isBlank()) return
        _uiState.update { state ->
            state.copy(
                teams = state.teams.map { team ->
                    if (team.id == teamId) {
                        team.copy(
                            players = team.players.map { player ->
                                if (player.id == playerId) player.copy(name = newName.trim()) else player
                            }
                        )
                    } else team
                }
            )
        }
    }

    fun addPlayerToTeam(teamId: String, playerName: String) {
        if (playerName.isBlank()) return
        _uiState.update { state ->
            state.copy(
                teams = state.teams.map { team ->
                    if (team.id == teamId) {
                        val newPlayer = Player(
                            id = UUID.randomUUID().toString(),
                            name = playerName.trim(),
                            teamId = teamId
                        )
                        team.copy(players = team.players + newPlayer)
                    } else team
                }
            )
        }
        soundManager.playButtonClick()
    }

    fun removePlayer(teamId: String, playerId: String) {
        _uiState.update { state ->
            state.copy(
                teams = state.teams.map { team ->
                    if (team.id == teamId && team.players.size > 1) {
                        team.copy(players = team.players.filter { it.id != playerId })
                    } else team
                }
            )
        }
    }

    fun startNewMatch() {
        soundManager.playStartGame()
        repository.resetMatchHistory()
        val resetTeams = _uiState.value.teams.map { team ->
            team.copy(
                score = 0,
                currentRepIndex = 0,
                currentJudgeIndex = if (team.players.size > 1) 1 else 0,
                players = team.players.map { it.copy(score = 0, correctGuesses = 0, skips = 0, badPerformances = 0, wordsCompleted = 0) }
            )
        }
        _uiState.update {
            it.copy(
                teams = resetTeams,
                currentTeamIndex = 0,
                currentWord = null,
                isWordRevealed = false,
                isTimerRunning = false,
                remainingSeconds = it.settings.turnDurationSeconds,
                turnWordHistory = emptyList(),
                matchWordHistory = emptyList(),
                matchStartTimeMs = System.currentTimeMillis(),
                matchDurationSeconds = 0L,
                winningTeam = null,
                funnyBannerMessage = DefaultWordsData.funnyGuidancePhrases.random(),
                currentScreen = GameScreen.TURN_INTRO,
                showExitConfirmDialog = false
            )
        }
    }

    fun prepareTurnReveal() {
        val word = repository.pickNextWord(_uiState.value.settings.enabledCategories)
        _uiState.update {
            it.copy(
                currentWord = word,
                isWordRevealed = false,
                currentScreen = GameScreen.PRIVATE_REVEAL
            )
        }
    }

    fun startActiveTurn() {
        soundManager.stopMenuMusic()
        soundManager.playTurnStart()
        val word = _uiState.value.currentWord ?: repository.pickNextWord(_uiState.value.settings.enabledCategories)
        randomEventCheckedForCurrentTurn = false
        _uiState.update {
            it.copy(
                currentWord = word,
                isWordRevealed = true,
                isTimerRunning = true,
                remainingSeconds = it.settings.turnDurationSeconds,
                turnWordHistory = emptyList(),
                activeEvent = null,
                isEventDialogVisible = false,
                currentScreen = GameScreen.ACTIVE_PLAY
            )
        }
        startTimerLoop()
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.isTimerRunning && _uiState.value.remainingSeconds > 0) {
                delay(1000)
                val currentSec = _uiState.value.remainingSeconds - 1
                if (currentSec == 10 || currentSec == 5) {
                    soundManager.playTickWarning()
                } else if (currentSec <= 3 && currentSec > 0) {
                    soundManager.playTickWarning()
                }

                // Check for random meme event trigger
                checkRandomMemeEventTrigger(currentSec)

                _uiState.update { it.copy(remainingSeconds = currentSec) }

                if (currentSec <= 0) {
                    endCurrentTurn()
                    break
                }
            }
        }
    }

    private fun checkRandomMemeEventTrigger(currentSec: Int) {
        val state = _uiState.value
        val freq = state.settings.randomEventFrequency
        if (freq == EventFrequency.OFF || randomEventCheckedForCurrentTurn) return
        val totalTurnTime = state.settings.turnDurationSeconds
        val elapsed = totalTurnTime - currentSec

        if (elapsed in state.settings.gracePeriodSeconds..(totalTurnTime - 10)) {
            val roll = (1..100).random()
            if (roll <= freq.chancePercentage) {
                randomEventCheckedForCurrentTurn = true
                val event = repository.pickRandomMemeEvent()
                pauseTimer()
                soundManager.playMemeEventAlert()
                _uiState.update {
                    it.copy(
                        activeEvent = event,
                        isEventDialogVisible = true
                    )
                }
            }
        }
    }

    fun dismissRandomEventAndResume() {
        _uiState.update {
            it.copy(
                activeEvent = null,
                isEventDialogVisible = false
            )
        }
        resumeTimer()
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isTimerRunning = false) }
    }

    fun resumeTimer() {
        if (_uiState.value.remainingSeconds > 0) {
            _uiState.update { it.copy(isTimerRunning = true) }
            startTimerLoop()
        }
    }

    fun resolveWord(type: ResolutionType, targetTeamId: String? = null) {
        val state = _uiState.value
        if (state.isProcessingAction) return // Guard against rapid multi-clicks
        val currentWord = state.currentWord ?: return
        val activeTeam = state.activeTeam ?: return
        val activeRep = state.activeRepresentative
        val activeJudge = state.activeJudge

        _uiState.update { it.copy(isProcessingAction = true) }

        val pointsDelta = when (type) {
            ResolutionType.CORRECT -> currentWord.points
            ResolutionType.SKIP -> ResolutionType.SKIP.points
            ResolutionType.BAD_PERFORMANCE -> ResolutionType.BAD_PERFORMANCE.points
        }

        when (type) {
            ResolutionType.CORRECT -> soundManager.playCorrect()
            ResolutionType.SKIP -> soundManager.playSkip()
            ResolutionType.BAD_PERFORMANCE -> soundManager.playFail()
        }

        val targetTeamIdResolved = targetTeamId ?: activeTeam.id
        val turnResult = TurnResult(
            teamId = targetTeamIdResolved,
            repPlayerId = activeRep?.id ?: "",
            judgePlayerId = activeJudge?.id,
            word = currentWord,
            type = type,
            pointsDelta = pointsDelta
        )

        val updatedTeams = state.teams.map { team ->
            when {
                team.id == activeTeam.id && (type == ResolutionType.SKIP || type == ResolutionType.BAD_PERFORMANCE) -> {
                    val newScore = (team.score + pointsDelta).coerceAtLeast(0)
                    team.copy(
                        score = newScore,
                        players = team.players.map { player ->
                            if (player.id == activeRep?.id) {
                                when (type) {
                                    ResolutionType.SKIP -> player.copy(skips = player.skips + 1, wordsCompleted = player.wordsCompleted + 1)
                                    ResolutionType.BAD_PERFORMANCE -> player.copy(badPerformances = player.badPerformances + 1, wordsCompleted = player.wordsCompleted + 1)
                                    else -> player
                                }
                            } else player
                        }
                    )
                }
                team.id == activeTeam.id && targetTeamIdResolved == activeTeam.id && type == ResolutionType.CORRECT -> {
                    val newScore = (team.score + pointsDelta).coerceAtLeast(0)
                    team.copy(
                        score = newScore,
                        players = team.players.map { player ->
                            if (player.id == activeRep?.id) {
                                player.copy(
                                    score = player.score + pointsDelta,
                                    correctGuesses = player.correctGuesses + 1,
                                    wordsCompleted = player.wordsCompleted + 1
                                )
                            } else player
                        }
                    )
                }
                team.id == targetTeamIdResolved -> {
                    val newScore = (team.score + pointsDelta).coerceAtLeast(0)
                    team.copy(score = newScore)
                }
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
