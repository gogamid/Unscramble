package com.example.unscramble

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
  private var _score = 0
  private var _currentWordCount = 0
  private var _currentScrambledWord = "test"

  val score: Int
    get() = _score

  val currentWordCount: Int
    get() = _currentWordCount

  val currentScrambledWord: String
    get() = _currentScrambledWord
}