package com.example.data.model

enum class Category(
    val id: String,
    val displayName: String,
    val icon: String,
    val description: String
) {
    FOOD("food", "أكل", "🍔", "محاجب، شخشوخة، تاكوس وأكلات جزائرية وعالمية"),
    PEOPLE("people", "مشاهير وشخصيات", "👤", "رياضيين، فنانين، شخصيات كرتونية ومشاهير"),
    ACTIONS("actions", "أفعال", "🕺", "حركات، أفعال يومية ومواقف حركية طريفة"),
    OBJECTS("objects", "أشياء", "📦", "أشياء، أدوات وأغراض نلقاوها فالدار والزنقة"),
    DZ("dz", "ثقافة DZ", "🇩🇿", "حوايج جزائرية 100% من الحومة والتقاليد"),
    FUNNY_SITUATIONS("funny", "مواقف مضحكة", "😂", "لقطات وكوارث تصرى غير فالقعدات المضحكة"),
    EMBARRASSING_SITUATIONS("embarrassing", "مواقف محرجة", "😳", "حشمات ومواقف تمنيت الأرض تبلعك فيها"),
    PROVERBS_EXPRESSIONS("proverbs", "عبارات وأمثال", "🗣️", "أمثال شعبية، عبارات جزائرية وكلام موزون");

    companion object {
        fun getAllCategories(): List<Category> = values().toList()
        fun fromId(id: String): Category? = values().find { it.id == id }
    }
}

enum class Difficulty(
    val points: Int,
    val label: String,
    val emoji: String
) {
    EASY(50, "سهل", "🟢"),
    HARD(100, "صعيب", "🔴")
}

data class CharadeWord(
    val id: String,
    val text: String,
    val category: Category,
    val difficulty: Difficulty,
    val tags: List<String> = emptyList(),
    val isCustom: Boolean = false,
    val enabled: Boolean = true
) {
    val points: Int get() = difficulty.points
}

data class Player(
    val id: String,
    val name: String,
    val teamId: String,
    val score: Int = 0,
    val correctGuesses: Int = 0,
    val skips: Int = 0,
    val badPerformances: Int = 0,
    val representativeTurns: Int = 0,
    val judgeTurns: Int = 0,
    val wordsCompleted: Int = 0
)

data class Team(
    val id: String,
    val name: String,
    val colorHex: Long,
    val emoji: String,
    val score: Int = 0,
    val players: List<Player> = emptyList(),
    val currentRepIndex: Int = 0,
    val currentJudgeIndex: Int = 0
) {
    val currentRepresentative: Player?
        get() = if (players.isNotEmpty()) players[currentRepIndex % players.size] else null

    val currentJudge: Player?
        get() = if (players.size > 1) {
            players[currentJudgeIndex % players.size]
        } else if (players.size == 1) {
            players[0]
        } else null
}

enum class EventFrequency(val label: String, val chancePercentage: Int, val description: String) {
    OFF("معطل ❌", 0, "ما يخرج حتى حدث مفاجئ"),
    VERY_RARE("نادر جداً 🐢", 8, "حدث خفيف نادر لتفادي الملل (الموصى به)"),
    RARE("نادر 🎲", 18, "يظهر خطرة على خطرة"),
    NORMAL("عادي 🔥", 30, "أحداث كوميدية متكررة تخلق فوضى حماسية")
}

enum class AlgerianMusicTrack(
    val id: String,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val bpm: Int
) {
    RAP_RAI(
        id = "rap_rai",
        title = "راب راي حماسي",
        subtitle = "808 Trap-Raï مع سينث أوتوتيون متوهج 🔥",
        emoji = "🔥",
        bpm = 128
    ),
    RAI_ARASSI(
        id = "rai_arassi",
        title = "راي عراسي وفيبريشن",
        subtitle = "إيقاع راي 6/8 مع صوت الكورغ العراسي 🎺",
        emoji = "🎺",
        bpm = 136
    ),
    CHAABI_ALGEROIS(
        id = "chaabi",
        title = "شعبي عاصمي حنين",
        subtitle = "مندول وقصبة مع دربوكة القعدة العاصمية 🪕",
        emoji = "🪕",
        bpm = 112
    ),
    GNAWA_DIWAN(
        id = "gnawa",
        title = "ديوان قناوي صحراوي",
        subtitle = "قمبري وقراقب مع ترانس صحراوي حماسي 🏜️",
        emoji = "🏜️",
        bpm = 120
    );

    companion object {
        fun getAllTracks(): List<AlgerianMusicTrack> = values().toList()
        fun fromId(id: String): AlgerianMusicTrack = values().find { it.id == id } ?: RAP_RAI
    }
}

data class RandomMemeEvent(
    val id: String,
    val title: String,
    val memeInstruction: String,
    val emoji: String,
    val quote: String
)

data class GameSettings(
    val turnDurationSeconds: Int = 60,
    val winningScore: Int = 1000,
    val randomEventFrequency: EventFrequency = EventFrequency.VERY_RARE,
    val stealMechanicEnabled: Boolean = false,
    val enabledCategories: Set<Category> = Category.values().toSet(),
    val sfxEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val selectedMusicTrack: AlgerianMusicTrack = AlgerianMusicTrack.RAP_RAI,
    val gracePeriodSeconds: Int = 5 // Do not trigger event in first X seconds
)

enum class GameScreen {
    HOME,
    SETUP_TEAMS,
    SETUP_CATEGORIES,
    SETUP_REVIEW,
    TURN_INTRO,
    PRIVATE_REVEAL,
    ACTIVE_PLAY,
    TURN_END_SUMMARY,
    VICTORY,
    CUSTOM_WORDS_MANAGER,
    STATS_DASHBOARD,
    ABOUT
}

data class TurnResult(
    val teamId: String,
    val repPlayerId: String,
    val judgePlayerId: String?,
    val word: CharadeWord,
    val type: ResolutionType,
    val pointsDelta: Int
)

enum class ResolutionType(val label: String, val points: Int) {
    CORRECT("صح", 0), // points derived from word difficulty (+50 / +100)
    SKIP("تخطي", -20),
    BAD_PERFORMANCE("لم يمثل جيداً", -5)
}

data class MatchStatistics(
    val totalWordsPlayed: Int = 0,
    val totalCorrectWords: Int = 0,
    val totalSkips: Int = 0,
    val totalBadPerformances: Int = 0,
    val totalMatchDurationSeconds: Long = 0,
    val topScoringPlayer: Player? = null,
    val bestActorPlayer: Player? = null,
    val mostSkipsPlayer: Player? = null,
    val mostBadActorPlayer: Player? = null,
    val winningTeam: Team? = null,
    val teamRankings: List<Team> = emptyList()
)
