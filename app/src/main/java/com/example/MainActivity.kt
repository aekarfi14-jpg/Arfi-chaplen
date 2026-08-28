package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.data.model.GameScreen
import com.example.engine.GameViewModel
import com.example.ui.screens.*
import com.example.ui.theme.ArfiChaplenTheme
import com.example.ui.theme.DarkBackground

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArfiChaplenTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = DarkBackground
                    ) {
                        ArfiApp(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ArfiApp(viewModel: GameViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle system back button properly based on current screen
    BackHandler(enabled = true) {
        when (uiState.currentScreen) {
            GameScreen.HOME -> {
                // Exit app or do default
            }
            GameScreen.SETUP_TEAMS -> viewModel.navigateTo(GameScreen.HOME)
            GameScreen.SETUP_CATEGORIES -> viewModel.navigateTo(GameScreen.SETUP_TEAMS)
            GameScreen.SETUP_REVIEW -> viewModel.navigateTo(GameScreen.SETUP_CATEGORIES)
            GameScreen.TURN_INTRO, GameScreen.PRIVATE_REVEAL, GameScreen.ACTIVE_PLAY, GameScreen.TURN_END_SUMMARY -> {
                viewModel.requestExitConfirmation(true)
            }
            GameScreen.VICTORY -> viewModel.navigateTo(GameScreen.HOME)
            GameScreen.STATS_DASHBOARD -> viewModel.navigateTo(GameScreen.HOME)
            GameScreen.CUSTOM_WORDS_MANAGER -> viewModel.navigateTo(GameScreen.HOME)
            GameScreen.ABOUT -> viewModel.navigateTo(GameScreen.HOME)
        }
    }

    AnimatedContent(
        targetState = uiState.currentScreen,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "screen_navigation"
    ) { targetScreen ->
        when (targetScreen) {
            GameScreen.HOME -> HomeScreen(viewModel = viewModel)
            GameScreen.SETUP_TEAMS -> TeamSetupScreen(viewModel = viewModel)
            GameScreen.SETUP_CATEGORIES -> CategorySettingsScreen(viewModel = viewModel)
            GameScreen.SETUP_REVIEW -> ReviewGameScreen(viewModel = viewModel)
            GameScreen.TURN_INTRO -> TurnIntroScreen(viewModel = viewModel)
            GameScreen.PRIVATE_REVEAL -> PrivateRevealScreen(viewModel = viewModel)
            GameScreen.ACTIVE_PLAY -> ActiveTurnScreen(viewModel = viewModel)
            GameScreen.TURN_END_SUMMARY -> TurnSummaryScreen(viewModel = viewModel)
            GameScreen.VICTORY -> VictoryScreen(viewModel = viewModel)
            GameScreen.STATS_DASHBOARD -> StatisticsScreen(viewModel = viewModel)
            GameScreen.CUSTOM_WORDS_MANAGER -> CustomWordsScreen(viewModel = viewModel)
            GameScreen.ABOUT -> AboutScreen(viewModel = viewModel)
        }
    }
}
