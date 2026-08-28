package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.*
import com.example.data.repository.GameContentRepository
import com.example.engine.GameViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context matches ARFI CHAPLEN`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ARFI CHAPLEN", appName)
    }

    @Test
    fun `repository has rich Algerian words library across all 8 categories`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val repo = GameContentRepository(app)
        val allCategories = Category.values().toSet()
        val words = repo.getAvailableWords(allCategories)

        assertTrue("Words library must contain extensive words", words.size >= 500)

        // Verify each category has items
        Category.values().forEach { cat ->
            val catWords = words.filter { it.category == cat }
            assertTrue("Category ${cat.name} should have items", catWords.isNotEmpty())
        }
    }

    @Test
    fun `custom word can be added and retrieved`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val repo = GameContentRepository(app)
        val initialCount = repo.getTotalWordsCount()

        val custom = repo.addCustomWord("كسكسي باللبن والقرعة", Category.FOOD, Difficulty.EASY)
        assertEquals("كسكسي باللبن والقرعة", custom.text)
        assertEquals(Category.FOOD, custom.category)
        assertTrue(custom.isCustom)
        assertEquals(initialCount + 1, repo.getTotalWordsCount())
    }

    @Test
    fun `game view model initializes default 4 teams and fair rotation`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = GameViewModel(app)
        val state = viewModel.uiState.value

        assertEquals(4, state.teams.size)
        val firstTeam = state.teams.first()
        assertNotNull(firstTeam.currentRepresentative)
        assertNotNull(firstTeam.currentJudge)
        assertNotEquals(firstTeam.currentRepresentative?.id, firstTeam.currentJudge?.id)
    }

    @Test
    fun `game scoring adds exact points for correct and penalizes skips and bad performance`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = GameViewModel(app)

        viewModel.startNewMatch()
        viewModel.prepareTurnReveal()
        viewModel.startActiveTurn()

        val initialScore = viewModel.uiState.value.activeTeam?.score ?: 0

        // Resolve correct
        val wordPoints = viewModel.uiState.value.currentWord?.points ?: 50
        viewModel.resolveWord(ResolutionType.CORRECT)
        assertEquals(initialScore + wordPoints, viewModel.uiState.value.activeTeam?.score)

        // Resolve skip (-20 DA)
        val afterCorrectScore = viewModel.uiState.value.activeTeam?.score ?: 0
        viewModel.resolveWord(ResolutionType.SKIP)
        assertEquals(afterCorrectScore - 20, viewModel.uiState.value.activeTeam?.score)

        // Resolve bad performance (-5 DA)
        val afterSkipScore = viewModel.uiState.value.activeTeam?.score ?: 0
        viewModel.resolveWord(ResolutionType.BAD_PERFORMANCE)
        assertEquals(afterSkipScore - 5, viewModel.uiState.value.activeTeam?.score)
    }
}
