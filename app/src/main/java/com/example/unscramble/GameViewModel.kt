package com.example.unscramble

import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.allWordsList

class GameViewModel : ViewModel() {
  private var _score = 0
  private var _currentWordCount = 0
  private lateinit var _currentScrambledWord: String

  val score: Int
    get() = _score

  val currentWordCount: Int
    get() = _currentWordCount

  val currentScrambledWord: String
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
      _currentScrambledWord = String(tempWord)
      ++_currentWordCount
      wordsList.add(currentWord)
    }
  }

  /*
  * Returns true if the current word count is less than MAX_NO_OF_WORDS.
  * Updates the next word.
  */
  fun nextWord(): Boolean {
    return if (currentWordCount < MAX_NO_OF_WORDS) {
      getNextWord()
      true
    } else false
  }


  init {
    getNextWord()
  }

}