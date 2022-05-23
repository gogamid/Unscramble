package com.example.unscramble

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWordsList

class GameViewModel : ViewModel() {
  private val _score = MutableLiveData(0)
  val score: LiveData<Int>
    get() = _score

  private var _currentWordCount = MutableLiveData(0)
  val currentWordCount: LiveData<Int>
    get() = _currentWordCount

  private val _currentScrambledWord = MutableLiveData<String>()
  val currentScrambledWord: LiveData<String>
    get() = _currentScrambledWord

  private var wordsList: MutableList<String> = mutableListOf()
  private lateinit var currentWord: String

  /*
  * Updates currentWord and currentScrambledWord with the next word.
  */
  private fun getNextWord() {
    currentWord = allWordsList.random()
    val tempWord = currentWord.toCharArray()
    tempWord.shuffle()

    while (String(tempWord).equals(currentWord, false)) {
      tempWord.shuffle()
    }
    if (wordsList.contains(currentWord)) {
      getNextWord()
    } else {
      _currentScrambledWord.value = String(tempWord)
      _currentWordCount.value = (_currentWordCount.value)?.inc()
      wordsList.add(currentWord)
    }
  }

  /*
  * Returns true if the current word count is less than MAX_NO_OF_WORDS.
  * Updates the next word.
  */
  fun nextWord(): Boolean {
    return if (currentWordCount.value!! < MAX_NO_OF_WORDS) {
      getNextWord()
      true
    } else false
  }

  private fun increaseScore() {
    _score.value = _score.value?.plus(SCORE_INCREASE)
  }

  fun isUserWordCorrect(playerWord: String): Boolean {
    if (playerWord.equals(currentWord, true)) {
      increaseScore()
      return true
    }
    return false
  }

  init {
    getNextWord()
  }

  /*
* Re-initializes the game data to restart the game.
*/
  fun reinitializeData() {
    _score.value = 0
    _currentWordCount.value = 0
    wordsList.clear()
    getNextWord()
  }

}