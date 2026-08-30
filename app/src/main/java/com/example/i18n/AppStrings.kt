package com.example.i18n

import com.example.data.model.AppLanguage
import com.example.data.model.Category
import com.example.data.model.Difficulty
import com.example.data.model.ResolutionType

object AppStrings {
    // --- Brand & App ---
    fun appTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "ARFI CHAPLEN" else "عرفي شابلن"
    fun appSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Algerian Charades Party Game 🇩🇿" else "عرفي شابلن الجزائري 🇩🇿"
    fun heroSlogan(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "The Ultimate Algerian Charades & Acting Party Game 🇩🇿🔥" else "لعبة التمثيل والضحك الجزائري في القعدات 🇩🇿🔥"
    fun creatorStoryHeader(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Created with love for gatherings 🇩🇿" else "صُنعت بكل حب للقعدات الجزائرية 🇩🇿"

    // --- Metric Capsules ---
    fun wordsCountTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "1600+ Words" else "1600+ كلمة"
    fun wordsCountSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Algerian Dictionary" else "قاموس جزائري"
    fun languageToggleTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "English ↔ العربية" else "العربية ↔ English"
    fun languageToggleSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Tap to switch" else "اضغط لتغيير اللغة"
    fun categoriesCapsuleTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "8 Categories" else "8 فئات"
    fun categoriesCapsuleSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Diverse & Fun" else "متنوعة ومضحكة"

    // --- Home Screen ---
    fun startNewMatch(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🔥 Start New Match" else "🔥 ابدأ ماتش جديد"
    fun wordBankTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "📚 Words Bank" else "📚 بنك الكلمات"
    fun wordBankSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Add custom words & local jokes" else "أضف كلمات قعدتكم ونكتكم الخاصة"
    fun statsTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🏆 Stats & Awards" else "🏆 إحصائيات وجوائز"
    fun statsSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Top actors & match highlights" else "أفضل ممثل وكوارث القعدة"
    fun rulesTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "ℹ️ Game Rules & Story" else "ℹ️ قوانين اللعبة وحكاية صنعها"
    fun rulesSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "How to play fair rotation & memes" else "كيفاش تلعبوا بالتناوب العادل والميمز"

    // --- Team Setup Screen ---
    fun teamSetupTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "👥 Teams & Players Setup" else "👥 تشكيلة الفرق واللاعبين"
    fun teamCountLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Number of Teams:" else "عدد الفرق:"
    fun twoTeams(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "2 Teams" else "فريقين"
    fun threeTeams(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "3 Teams" else "3 فرق"
    fun fourTeams(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "4 Teams" else "4 فرق"
    fun roleActorTag(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) " (Actor)" else " (الممثل)"
    fun roleJudgeTag(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) " (Judge)" else " (الحاكم)"
    fun addPlayerToTeam(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Add Player to this Team" else "إضافة لاعب لهذا الفريق"
    fun continueToCategories(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Continue to Categories ➡️" else "المواصلة لاختيار الفئات ➡️"
    fun minPlayerWarning(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "At least 1 player required per team" else "يلزم لاعب واحد على الأقل في كل فريق"
    fun editTeamNameDialogTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Edit Team Name" else "تعديل اسم الفريق"
    fun editPlayerNameDialogTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Edit Player Name" else "تعديل اسم اللاعب"
    fun addNewPlayerDialogTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Add New Player" else "إضافة لاعب جديد"
    fun teamNameField(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Team Name" else "اسم الفريق"
    fun playerNameField(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Player Name" else "اسم اللاعب"
    fun saveBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Save" else "حفظ"
    fun cancelBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Cancel" else "إلغاء"
    fun addBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Add" else "إضافة"

    // --- Category & Match Settings ---
    fun settingsTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "⚙️ Match Settings & Categories" else "⚙️ إعدادات الماتش والفئات"
    fun turnDurationLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "⏱️ Turn Duration:" else "⏱️ وقت الدور:"
    fun secondsUnit(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "sec" else "ثانية"
    fun winningScoreLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🏆 Winning Score Target:" else "🏆 نقاط الفوز للبطولة:"
    fun currencyUnit(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "pts" else "دج"
    fun randomEventsLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🎲 Sudden Meme Events:" else "🎲 الأحداث الكوميدية المفاجئة:"
    fun sfxLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🔊 Sound Effects & Hype" else "🔊 صوت التأثيرات والحماس"
    fun sfxSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Bells, timers, victory sounds" else "جرس، عداد، وصوت الربحة"
    fun musicLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🎵 Background Algerian Music" else "🎵 الموسيقى الجزائرية في الخلفية"
    fun musicSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Raï, Chaabi, Gnawa vibes" else "راي، شعبي، وقناوي حماسي"
    fun availableCategoriesLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🎭 Word Categories:" else "🎭 فئات الكلمات المتاحة:"
    fun enabledOf(lang: AppLanguage, count: Int, total: Int) = if (lang == AppLanguage.ENGLISH) "$count enabled of $total" else "$count مفعلة من $total"
    fun continueToReview(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Continue to Review ➡️" else "المواصلة لمراجعة التشكيلة ➡️"

    // --- Review Game Screen ---
    fun reviewTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "📋 Review Setup & Rules" else "📋 مراجعة التشكيلة والقوانين"
    fun reviewSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Ready for laughs? Review your teams and game settings:" else "جاهزين للضحك؟ راجعوا التشكيلة وقوانين اللعبة:"
    fun participatingTeams(lang: AppLanguage, count: Int) = if (lang == AppLanguage.ENGLISH) "👥 Participating Teams ($count teams):" else "👥 الفرق المشاركة ($count فرق):"
    fun matchSettingsSummary(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "⚙️ Match Settings:" else "⚙️ إعدادات المباراة:"
    fun selectedCategories(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🎭 Selected Categories:" else "🎭 الفئات المختارة:"
    fun startBattleBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🔥 Start the Game! 🇩🇿" else "🔥 ابدأ المعركة والتمثيل! 🇩🇿"

    // --- Turn Intro Screen ---
    fun turnOf(lang: AppLanguage, teamName: String) = if (lang == AppLanguage.ENGLISH) "Turn: $teamName" else "دور $teamName"
    fun currentTeamScore(lang: AppLanguage, score: Int) = if (lang == AppLanguage.ENGLISH) "Current Team Score: $score pts" else "رصيد الفريق الحالي: $score دج"
    fun actorRoleLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Actor this turn:" else "الممثل فهاد الدور:"
    // REQUIREMENT 1: TEXT ONLY - (اختياري) / (Optional)
    fun judgeRoleLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Judge & Phone Holder (Optional):" else "الحاكم وماسك الهاتف (اختياري):"
    fun startFrenzyBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🔥 Go! (Start timer & word frenzy)" else "🔥 انطلق! (ابدأ العداد وتتابع الكلمات)"
    fun secretRevealBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🔒 Secret reveal for actor only" else "🔒 كشف سري مسبق للممثل فقط"
    fun speedRulesHeader(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Speed Frenzy Mode:" else "نظام التحدي السريع المتتابع:"
    fun speedRulesBody(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH)
        "• Once the timer starts, words appear one after another!\n• Act out as many words as possible before time expires ⏱️\n• Correct = +50/+100 pts | Skip = -20 pts 💸"
    else
        "• عند بدأ العداد تخرج الكلمات متتالية واحدة تلو الأخرى!\n• مثل أكبر عدد من الكلمات قبل انتهاء الوقت ⏱️\n• صح = +50/+100 دج | تخطي = -20 دج 💸"

    // --- Private Reveal Screen ---
    fun privateRevealTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🔒 Secret First Word Reveal" else "🔒 كشف الكلمة الأولى للممثل"
    fun privateRevealWarning(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Hand the phone to the actor only! Don't let other players see!" else "أعط الهاتف للممثل فقط ولا تدع الآخرين يشاهدون الكلمة!"
    fun tapToReveal(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Tap to reveal first word 👁️" else "اضغط لكشف الكلمة الأولى 👁️"
    fun readyStartFrenzy(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Ready? Start timer and show your skills! 🔥" else "جاهز؟ ابدأ العداد وورينا شطارتك! 🔥"

    // --- Active Play Screen ---
    fun doublePointsBanner(lang: AppLanguage, pts: Int) = if (lang == AppLanguage.ENGLISH) "✨ Double Points (+$pts pts) ✨" else "✨ نقاط مضاعفة (+$pts دج) ✨"
    fun whoGuessedFirst(lang: AppLanguage, gain: Int) = if (lang == AppLanguage.ENGLISH) "Which team guessed correctly first? (+$gain pts)" else "من الفريق الذي عرف الكلمة أولاً؟ (+$gain دج)"
    fun skipBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "⏭️ Skip (-20 pts)" else "⏭️ تخطي (-20 دج)"
    fun badPerformanceBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Rule Break (-5 pts)" else "لم يمثل (-5 دج)"
    fun pauseTimer(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Pause Timer" else "إيقاف العداد"
    fun resumeTimer(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Resume Timer" else "استئناف العداد"
    fun exitMatchConfirmTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Exit Match?" else "هل تريد الخروج من الماتش؟"
    fun exitMatchConfirmBody(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Current match progress will be lost and you will return to Home." else "سيتم إلغاء الماتش الحالي والعودة للشاشة الرئيسية."
    fun exitMatchYes(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Exit to Home" else "خروج للرئيسية"
    fun exitMatchCancel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Keep Playing" else "مواصلة اللعب"

    // --- Turn End Summary Screen ---
    fun turnSummaryTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🔔 Turn Time Expired!" else "🔔 خلاص وقت الدور!"
    fun turnPointsGained(lang: AppLanguage, pts: Int) = if (lang == AppLanguage.ENGLISH) "+$pts pts in this turn 🔥" else "+$pts دج فهاد الدور 🔥"
    fun turnPointsLost(lang: AppLanguage, pts: Int) = if (lang == AppLanguage.ENGLISH) "$pts pts 💀" else "$pts دج 💀"
    fun currentStandings(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "📊 Current Team Standings:" else "📊 ترتيب الفرق الحالي:"
    fun turnWordsList(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "📝 Words in this Turn:" else "📝 كلمات هذا الدور:"
    fun nextTeamTurn(lang: AppLanguage, nextTeamName: String) = if (lang == AppLanguage.ENGLISH) "Next Turn: ($nextTeamName) ➡️" else "الدور التالي: ($nextTeamName) ➡️"

    // --- Victory Screen ---
    fun victoryTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🏆 Victory & Champions!" else "🏆 نهاية الماتش والفوز الكبير!"
    fun winningTeamBanner(lang: AppLanguage, winnerName: String) = if (lang == AppLanguage.ENGLISH) "Champions: $winnerName" else "الفريق الفائز: $winnerName"
    fun winningScoreBanner(lang: AppLanguage, score: Int) = if (lang == AppLanguage.ENGLISH) "Reached $score pts and won the match! 🇩🇿🔥" else "وصلوا لـ $score دج وربحوا القعدة 🇩🇿🔥"
    fun finalStandings(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🏅 Final Match Standings:" else "🏅 الترتيب النهائي للماتش:"
    fun viewStatsBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "📊 Match Stats & Awards" else "📊 تفاصيل وإحصائيات القعدة"
    fun rematchBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🔥 Rematch / New Game!" else "🔥 ثأر وماتش جديد!"
    fun returnHomeBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Return to Home 🏠" else "العودة للقائمة الرئيسية 🏠"

    // --- Statistics Screen ---
    fun statsScreenTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🏆 Gathering Statistics & Awards" else "🏆 إحصائيات القعدة والجوائز"
    fun topActorAward(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🎭 Best Actor Award (Most Correct Guesses)" else "🎭 أفضل ممثل (أكثر من جاب كلمات صح)"
    fun skipChampAward(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "⏭️ Skip Champion (Most Skips)" else "⏭️ بطل التخطي (أكثر من دار سكايب)"
    fun badActorAward(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "❌ Acting Penalty Award (Most Rule Breaks)" else "❌ أكثر ممثل تبهدل (أخطاء تمثيل)"
    fun matchSummaryHeader(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "📊 Overall Summary:" else "📊 ملخص أرقام الماتش:"
    fun totalPlayedWords(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Total Words:" else "مجموع الكلمات:"
    fun correctWords(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Correct Guesses:" else "تمثيل ناجح:"
    fun totalSkips(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Skips:" else "تخطي:"
    fun totalPenalties(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Rule Penalties:" else "أخطاء تمثيل:"
    fun totalDuration(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Match Duration:" else "مدة الماتش:"
    fun playersRankings(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "👥 Individual Player Scores:" else "👥 نقاط وتفاصيل كل لاعب:"
    fun savedMatchesHistory(lang: AppLanguage, count: Int) = if (lang == AppLanguage.ENGLISH) "📜 Previous Match History ($count)" else "📜 سجل المباريات السابقة ($count)"
    fun clearHistoryBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Clear History" else "مسح السجل"

    // --- Custom Words Screen ---
    fun customWordsTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "📚 Custom Words Bank" else "📚 بنك الكلمات المخصصة"
    fun noCustomWordsYet(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "No custom words added yet" else "مازال ما أضفت حتى كلمة مخصصة"
    fun noCustomWordsTip(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Tap the green button below to add your friends' names or inside jokes!" else "اضغط على الزر الأخضر بالأسفل لإضافة أسماء أصدقائك أو نكت خاصة بقعدتكم!"
    fun addCustomWordBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Add Custom Word ➕" else "إضافة كلمة مخصصة ➕"
    fun wordTextInputLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Word or Phrase text" else "نص الكلمة أو العبارة"
    fun categoryLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Category:" else "الفئة:"
    fun difficultyLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Difficulty:" else "الصعوبة:"
    fun easyDiffLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🟢 Easy (+50 pts)" else "🟢 سهل (+50 دج)"
    fun hardDiffLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🔴 Hard (+100 pts)" else "🔴 صعيب (+100 دج)"
    fun saveWordBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Save Word" else "حفظ الكلمة"

    // --- About Screen ---
    fun aboutTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "ℹ️ About the Game & Rules" else "ℹ️ حول اللعبة والقواعد"
    fun aboutStoryTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Origin Story:" else "حكاية صنع اللعبة:"
    fun aboutStoryP1(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH)
        "Created by Younes Chekour 🗿 so party gatherings never feel dull and isolated on phones!"
    else
        "صنعها يونس الشكور 🗿 باش ما تبقاش القعدة ميتة وكل واحد شاد تليفونو وحدو!"
    fun aboutStoryP2(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH)
        "Started from real Algerian friends gatherings, turned into a 100% Algerian party game packed with acting, memes, and challenges 🔥🇩🇿"
    else
        "الفكرة بدات بقعدة جزائرية عادية وضحك مع الصحاب، ومن بعد يونس قرر يدير منها تطبيق جزائري 100% يجمع العائلة والأصدقاء بالتمثيل والميمز والتحديات 🔥🇩🇿"
    fun aboutRulesHeader(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "📜 Gathering Rules & Gameplay:" else "📜 قوانين القعدة واللعب:"
    fun getRulesList(lang: AppLanguage): List<String> = if (lang == AppLanguage.ENGLISH) listOf(
        "1️⃣ Fair Rotation: Every team automatically rotates actor and judge roles every turn.",
        "2️⃣ Strict Silence: The actor sees the word secretly and MUST NOT make any sounds or spoken words!",
        "3️⃣ Scoring: The first team to guess the word wins the points (+50 or +100 pts).",
        "4️⃣ Penalties: Skipping = -20 pts, bad acting / rule break = -5 pts deducted from acting team.",
        "5️⃣ Surprise Events: Random comedic challenges freeze the timer for hilarious twists.",
        "6️⃣ Victory: The first team to reach the target score wins the tournament cup 🏆."
    ) else listOf(
        "1️⃣ التناوب العادل: كل فريق عندو ممثل وحاكم يتناوبوا تلقائياً كل دور.",
        "2️⃣ الكتمان التام: الممثل يشوف الكلمة بالسر، وممنوع عليه ينطق حتى حرف أو صوت!",
        "3️⃣ نظام النقاط: الفريق الذي يعرف الكلمة أولاً ينال النقاط (+50 أو +100 دج).",
        "4️⃣ العقوبات: التخطي = -20 دج، والتمثيل السيء = -5 دج وتُخصم من الفريق الممثل.",
        "5️⃣ الأحداث المفاجئة: إذا تفعلت، توقف الوقت وتطلب حركة مضحكة من الممثل.",
        "6️⃣ الفوز: أول فريق يوصل للهدف المحدد يربح البطولة والكأس 🏆."
    )
    fun offlineGuaranteeTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "📴 100% Offline & No Internet Needed" else "📴 100% أوفلاين وبدون إنترنت"
    fun offlineGuaranteeBody(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH)
        "Designed to work anywhere (desert, beach, rooftops, coffee shops) without any network or login."
    else
        "اللعبة مصممة لتشتغل في أي مكان (الصحراء، البحر، السطح، القهوة) بدون الحاجة لأي اتصال بالإنترنت أو حسابات."

    // --- Music Dialog ---
    fun musicDialogTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🎵 Algerian Music Studio" else "🎵 استوديو الموسيقى الجزائرية"
    fun musicDialogToggle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Play Background Music" else "تشغيل الموسيقى في الخلفية"
    fun closeBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Close" else "إغلاق"

    // --- Category Localized Names & Descriptions ---
    fun categoryName(cat: Category, lang: AppLanguage): String {
        if (lang == AppLanguage.ARABIC) return cat.displayName
        return when (cat) {
            Category.FOOD -> "Food & Dishes"
            Category.PEOPLE -> "Celebrities & Icons"
            Category.ACTIONS -> "Actions & Gestures"
            Category.OBJECTS -> "Objects & Items"
            Category.DZ -> "DZ Heritage & Culture"
            Category.FUNNY_SITUATIONS -> "Funny Situations"
            Category.EMBARRASSING_SITUATIONS -> "Awkward Moments"
            Category.PROVERBS_EXPRESSIONS -> "Proverbs & Idioms"
        }
    }

    fun categoryDescription(cat: Category, lang: AppLanguage): String {
        if (lang == AppLanguage.ARABIC) return cat.description
        return when (cat) {
            Category.FOOD -> "Mahjouba, Chakhchoukha, Tacos, traditional & international dishes"
            Category.PEOPLE -> "Athletes, artists, cartoon icons, and famous figures"
            Category.ACTIONS -> "Movements, daily gestures, and funny physical actions"
            Category.OBJECTS -> "Tools, furniture, and items found in Algerian homes & streets"
            Category.DZ -> "100% authentic Algerian culture, neighborhood vibes & traditions"
            Category.FUNNY_SITUATIONS -> "Hilarious comedic scenes from real gatherings"
            Category.EMBARRASSING_SITUATIONS -> "Embarrassing moments where you wished you could disappear"
            Category.PROVERBS_EXPRESSIONS -> "Popular Algerian sayings, proverbs, and witty expressions"
        }
    }
}
