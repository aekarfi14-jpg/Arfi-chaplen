package com.example

import com.example.data.model.Category
import com.example.data.model.DefaultWordsData
import com.example.data.model.Difficulty
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testDefaultWordsData_allInitialWordsValid() {
    val words = DefaultWordsData.getAllInitialWords()
    assertTrue("Word list must not be empty", words.isNotEmpty())
    assertEquals("Should contain 496 unique default words", 496, words.size)

    val seenTexts = mutableSetOf<String>()
    for (word in words) {
      assertNotNull("Word ID must not be null", word.id)
      assertTrue("Word text must not be blank", word.text.isNotBlank())
      assertNotNull("Word category must not be null", word.category)
      assertNotNull("Word difficulty must not be null", word.difficulty)

      // Verify no duplicates
      val trimmed = word.text.trim()
      assertFalse("Duplicate word found: $trimmed", seenTexts.contains(trimmed))
      seenTexts.add(trimmed)
    }

    // Verify all categories and difficulties have words
    val foodEasy = words.filter { it.category == Category.FOOD && it.difficulty == Difficulty.EASY }
    val foodHard = words.filter { it.category == Category.FOOD && it.difficulty == Difficulty.HARD }
    val peopleEasy = words.filter { it.category == Category.PEOPLE && it.difficulty == Difficulty.EASY }
    val actionsEasy = words.filter { it.category == Category.ACTIONS && it.difficulty == Difficulty.EASY }
    val actionsHard = words.filter { it.category == Category.ACTIONS && it.difficulty == Difficulty.HARD }
    val objectsEasy = words.filter { it.category == Category.OBJECTS && it.difficulty == Difficulty.EASY }
    val objectsHard = words.filter { it.category == Category.OBJECTS && it.difficulty == Difficulty.HARD }
    val dzEasy = words.filter { it.category == Category.DZ && it.difficulty == Difficulty.EASY }
    val dzHard = words.filter { it.category == Category.DZ && it.difficulty == Difficulty.HARD }
    val proverbsEasy = words.filter { it.category == Category.PROVERBS_EXPRESSIONS && it.difficulty == Difficulty.EASY }
    val proverbsHard = words.filter { it.category == Category.PROVERBS_EXPRESSIONS && it.difficulty == Difficulty.HARD }
    val funnyEasy = words.filter { it.category == Category.FUNNY_SITUATIONS && it.difficulty == Difficulty.EASY }
    val funnyHard = words.filter { it.category == Category.FUNNY_SITUATIONS && it.difficulty == Difficulty.HARD }
    val embarrEasy = words.filter { it.category == Category.EMBARRASSING_SITUATIONS && it.difficulty == Difficulty.EASY }
    val embarrHard = words.filter { it.category == Category.EMBARRASSING_SITUATIONS && it.difficulty == Difficulty.HARD }

    assertTrue("Food Easy should have words", foodEasy.isNotEmpty())
    assertTrue("Food Hard should have words", foodHard.isNotEmpty())
    assertTrue("People Easy should have words", peopleEasy.isNotEmpty())
    assertTrue("Actions Easy should have words", actionsEasy.isNotEmpty())
    assertTrue("Actions Hard should have words", actionsHard.isNotEmpty())
    assertTrue("Objects Easy should have words", objectsEasy.isNotEmpty())
    assertTrue("Objects Hard should have words", objectsHard.isNotEmpty())
    assertTrue("DZ Easy should have words", dzEasy.isNotEmpty())
    assertTrue("DZ Hard should have words", dzHard.isNotEmpty())
    assertTrue("Proverbs Easy should have words", proverbsEasy.isNotEmpty())
    assertTrue("Proverbs Hard should have words", proverbsHard.isNotEmpty())
    assertTrue("Funny Easy should have words", funnyEasy.isNotEmpty())
    assertTrue("Funny Hard should have words", funnyHard.isNotEmpty())
    assertTrue("Embarrassing Easy should have words", embarrEasy.isNotEmpty())
    assertTrue("Embarrassing Hard should have words", embarrHard.isNotEmpty())
  }
}
