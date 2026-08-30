package com.example.i18n

import com.example.data.model.AppLanguage
import com.example.data.model.Category

/**
 * Centralized Bilingual Localization Engine (Arabic & English)
 * Complete string coverage for all screens and components.
 */
object AppStrings {

    // --- Home Screen ---
    fun appTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "ARFI CHAPLEN" else "عارفينك شارلي شابلن"
    fun appSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "The Algerian Charades & Acting Party Game" else "لعبة التمثيل والشاراد الجزائرية الكبرى"
    fun offlineBadge(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "100% Offline" else "أوفلاين 100%"
    fun playButton(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Start Tournament 🎭" else "ابدأ المنافسة 🎭"
    fun settingsButton(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Categories & Timer ⚙️" else "الفئات والوقت ⚙️"
    fun customWordsButton(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Custom Words Bank 📚" else "بنك الكلمات المخصصة 📚"
    fun statsButton(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Statistics & Hall of Fame 🏆" else "الإحصائيات وسجل المباريات 🏆"
    fun quickMatch(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Quick Match" else "ماتش سريع"
    fun quickMatchSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Start immediately with 2 default teams" else "ابدأ فوراً بفريقين جاهزين وإعدادات قياسية"
    fun tournament(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Tournament Setup" else "تهيئة الفرق والمباراة"
    fun tournamentSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Configure 2 to 4 custom teams & players" else "خصص 2 إلى 4 فرق وأسماء اللاعبين"
    fun rulesTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Rules & Origin Story" else "قواعد اللعبة وقصة الصنع"
    fun rulesSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Origin story & fair play guidelines" else "حكاية صنع اللعبة وكيفية التناوب العادل"
    fun creatorHeader(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Created by Younes Chekour 🗿" else "صُنعت بواسطة يونس الشكور 🗿"
    fun creatorStoryHeader(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Created by Younes Chekour 🗿" else "فكرة وتطوير: يونس الشكور 🗿"
    fun categoriesCapsuleTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Categories & Timer" else "الفئات والوقت"
    fun categoriesCapsuleSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Manage word categories and match timer" else "اختر فئات الكلمات ومدة عداد التمثيل"
    fun startNewMatch(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Start New Match" else "ابدأ ماتش جديد"
    fun wordBankTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Custom Words Bank" else "بنك الكلمات المخصصة"
    fun wordBankSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Add custom inside jokes and words" else "أضف كلمات خاصة بقعدتكم ونكت أصدقائكم"
    fun statsTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Statistics & History" else "الإحصائيات والسجل"
    fun statsSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "View past games and top actors" else "سجل المباريات السابقة وجوائز الأوسكار"

    // --- Team Setup Screen ---
    fun teamSetupTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Setup Teams & Players" else "تهيئة الفرق واللاعبين"
    fun targetScoreLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Winning Target Score:" else "هدف الفوز المطلوب:"
    fun addTeamButton(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Add Team ➕" else "إضافة فريق ➕"
    fun addPlayerButton(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Add Player ➕" else "إضافة لاعب ➕"
    fun editTeamName(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Edit Team Name" else "تعديل اسم الفريق"
    fun enterPlayerName(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Player Name" else "اسم اللاعب"
    fun saveButton(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Save" else "حفظ"
    fun saveBtn(lang: AppLanguage) = saveButton(lang)
    fun cancelButton(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Cancel" else "إلغاء"
    fun cancelBtn(lang: AppLanguage) = cancelButton(lang)
    fun addBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Add" else "إضافة"
    fun minPlayersWarning(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Each team needs at least 1 player to play!" else "كل فريق يحتاج على الأقل لاعب واحد!"
    fun minPlayerWarning(lang: AppLanguage) = minPlayersWarning(lang)
    fun startGameReviewButton(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Review & Start Match 🚀" else "مراجعة وتأكيد البداية 🚀"
    fun continueToCategories(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Continue to Categories ⚙️" else "متابعة لاختيار الفئات ⚙️"
    fun teamCountLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Number of Teams:" else "عدد الفرق المشاركة:"
    fun twoTeams(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "2 Teams" else "فريقين"
    fun threeTeams(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "3 Teams" else "3 فرق"
    fun fourTeams(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "4 Teams" else "4 فرق"
    fun roleActorTag(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Actor 🎭" else "الممثل 🎭"
    fun roleJudgeTag(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Judge 📱" else "الحاكم 📱"
    fun addPlayerToTeam(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Add Player" else "إضافة لاعب"
    fun editTeamNameDialogTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Edit Team Name" else "تعديل اسم الفريق"
    fun teamNameField(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Team Name" else "اسم الفريق"
    fun editPlayerNameDialogTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Edit Player Name" else "تعديل اسم اللاعب"
    fun playerNameField(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Player Name" else "اسم اللاعب"
    fun addNewPlayerDialogTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Add New Player" else "إضافة لاعب جديد"

    // --- Review Game Screen ---
    fun reviewMatchTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Review & Confirm Match" else "مراجعة وتأكيد المباراة"
    fun reviewTitle(lang: AppLanguage) = reviewMatchTitle(lang)
    fun reviewSubtitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Check participating teams and settings before start" else "تأكد من الفرق المشاركة والإعدادات قبل الانطلاق"
    fun startMatchConfirmed(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🔥 Let's Go! Start Match" else "🔥 ابدأ الماتش الآن!"
    fun startBattleBtn(lang: AppLanguage) = startMatchConfirmed(lang)
    fun participatingTeams(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Participating Teams:" else "الفرق المشاركة:"
    fun matchSettingsSummary(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Match Settings:" else "إعدادات المباراة:"
    fun turnDurationLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Turn Duration:" else "مدة الدور:"
    fun winningScoreLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Winning Score:" else "نقاط الفوز:"
    fun randomEventsLabel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Surprise Events:" else "الأحداث المفاجئة:"
    fun selectedCategories(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Selected Categories:" else "الفئات المختارة:"

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
    fun secretRevealTitle(lang: AppLanguage) = privateRevealTitle(lang)
    fun privateRevealWarning(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Hand the phone to the actor only! Don't let other players see!" else "أعط الهاتف للممثل فقط ولا تدع الآخرين يشاهدون الكلمة!"
    fun secretRevealInstructions(lang: AppLanguage) = privateRevealWarning(lang)
    fun tapToReveal(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Tap to reveal first word 👁️" else "اضغط لكشف الكلمة الأولى 👁️"
    fun readyStartFrenzy(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Ready? Start timer and show your skills! 🔥" else "جاهز؟ ابدأ العداد وورينا شطارتك! 🔥"
    fun startActiveTurn(lang: AppLanguage) = startFrenzyBtn(lang)

    // --- Active Play Screen ---
    fun doublePointsBanner(lang: AppLanguage, pts: Int) = if (lang == AppLanguage.ENGLISH) "✨ Double Points (+$pts pts) ✨" else "✨ نقاط مضاعفة (+$pts دج) ✨"
    fun whoGuessedFirst(lang: AppLanguage, gain: Int) = if (lang == AppLanguage.ENGLISH) "Which team guessed correctly first? (+$gain pts)" else "من الفريق الذي عرف الكلمة أولاً؟ (+$gain دج)"
    fun skipBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "⏭️ Skip (-20 pts)" else "⏭️ تخطي (-20 دج)"
    fun badPerformanceBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Rule Break (-5 pts)" else "لم يمثل (-5 دج)"
    fun pauseTimer(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Pause Timer" else "إيقاف العداد"
    fun resumeTimer(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Resume Timer" else "استئناف العداد"
    fun secondsUnit(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "s" else "ثانية"
    fun exitMatchConfirmTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Exit Match?" else "هل تريد الخروج من الماتش؟"
    fun exitMatchConfirmBody(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Match progress will be saved in your match history." else "سيتم حفظ مجريات الجولة الحالية في السجل وتعود للقائمة الرئيسية."
    fun exitMatchYes(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Yes, Exit" else "نعم، خروج"
    fun exitMatchCancel(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Cancel" else "إلغاء"

    // --- Turn Summary Screen ---
    fun turnSummaryTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "⏱️ Time's Up! Turn Summary" else "⏱️ خلص الوقت! ملخص الدور"
    fun turnPointsGained(lang: AppLanguage, pts: Int) = if (lang == AppLanguage.ENGLISH) "+$pts pts gained" else "+$pts دج مكسب"
    fun turnPointsLost(lang: AppLanguage, pts: Int) = if (lang == AppLanguage.ENGLISH) "$pts pts lost" else "$pts دج خسارة"
    fun currentStandings(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🏆 Current Standings:" else "🏆 الترتيب الحالي للفرق:"
    fun turnWordsList(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Words Played this Turn:" else "الكلمات الملعوبة في هذا الدور:"
    fun nextTeamTurn(lang: AppLanguage, teamName: String) = if (lang == AppLanguage.ENGLISH) "Next Turn: $teamName ➡️" else "الدور القادم: $teamName ➡️"

    // --- Victory Screen ---
    fun victoryTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🎊 Match Champions! 🎊" else "🎊 أبطال الماتش رسمياً! 🎊"
    fun winningTeamBanner(lang: AppLanguage, name: String) = if (lang == AppLanguage.ENGLISH) "Winner: $name" else "الفائز: $name"
    fun winningScoreBanner(lang: AppLanguage, score: Int) = if (lang == AppLanguage.ENGLISH) "Final Score: $score pts 🏆" else "الرصيد النهائي: $score دج 🏆"
    fun finalStandings(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🏆 Final Tournament Podium:" else "🏆 الترتيب النهائي للبطولة:"
    fun viewStatsBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Full Match Statistics 📊" else "إحصائيات الماتش الكاملة 📊"
    fun rematchBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Rematch / Play Again 🔄" else "ماتش جديد بنفس الفرق 🔄"
    fun returnHomeBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Return to Main Menu 🏠" else "العودة للقائمة الرئيسية 🏠"
    fun currencyUnit(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "pts" else "دج"

    // --- Settings & Categories Screen ---
    fun settingsTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "⚙️ Game Settings & Categories" else "⚙️ إعدادات اللعبة والفئات"
    fun randomEventsToggle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Surprise Meme Events" else "الأحداث الميمية المفاجئة"
    fun soundEffectsToggle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Sound Effects & Audio Feedback" else "المؤثرات الصوتية والنغمات"
    fun algerianMusicStudio(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Algerian Music Studio (4 Genres)" else "استوديو الموسيقى الجزائرية (4 طبوع)"
    fun activeCategoriesHeader(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Active Word Categories:" else "الفئات المفعلة في اللعبة:"
    fun selectAllBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Select All" else "تحديد الكل"
    fun deselectAllBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Deselect All" else "إلغاء التحديد"
    fun saveSettingsBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Save Settings" else "حفظ الإعدادات"
    fun defaultSettingsBtn(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Restore Defaults" else "استعادة الافتراضي"

    // --- Statistics Screen ---
    fun statsScreenTitle(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "🏆 Hall of Fame & Stats" else "🏆 الإحصائيات وسجل المباريات"
    fun topActorAward(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Golden Oscar Actor" else "أفضل ممثل أوسكار"
    fun skipChampAward(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Skip Champion" else "بطل الهروب والتخطي"
    fun badActorAward(lang: AppLanguage) = if (lang == AppLanguage.ENGLISH) "Rule Break Award" else "عقوبات كسر القواعد"
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
