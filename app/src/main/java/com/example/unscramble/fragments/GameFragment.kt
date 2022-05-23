package com.example.unscramble.fragments

import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.unscramble.GameViewModel
import com.example.unscramble.R
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWordsList
import com.example.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameFragment : Fragment() {
  private val viewModel: GameViewModel by viewModels()
  private lateinit var binding: GameFragmentBinding


  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = GameFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Setup a click listener for the Submit and Skip buttons.
    binding.submitBtn.setOnClickListener { onSubmitWord() }
    binding.skipBtn.setOnClickListener { onSkipWord() }
    // Update the UI
    updateNextWordOnScreen()
    binding.score.text = getString(R.string.score_string, 0)
    binding.countWord.text = getString(
      R.string.words, 0, MAX_NO_OF_WORDS
    )
  }

  /*
  * Checks the user's word, and updates the score accordingly.
  * Displays the next scrambled word.
  */
  private fun onSubmitWord() {
    val playerWord = binding.editText.text.toString()

    if (viewModel.isUserWordCorrect(playerWord)) {
      setErrorTextField(false)
      if (viewModel.nextWord()) {
        updateNextWordOnScreen()
      } else {
        showFinalScoreDialog()
      }
    } else {
      setErrorTextField(true)
    }
  }

  /*
   * Skips the current word without changing the score.
   * Increases the word count.
   */
  private fun onSkipWord() {
    if (viewModel.nextWord()) {
      setErrorTextField(false)
      updateNextWordOnScreen()
    } else {
      showFinalScoreDialog()
    }
  }

  /*
   * Gets a random word for the list of words and shuffles the letters in it.
   */
  private fun getNextScrambledWord(): String {
    val tempWord = allWordsList.random().toCharArray()
    tempWord.shuffle()
    return String(tempWord)
  }

  /*
   * Re-initializes the data in the ViewModel and updates the views with the new data, to
   * restart the game.
   */
  private fun restartGame() {
    viewModel.reinitializeData()
    setErrorTextField(false)
    updateNextWordOnScreen()
  }

  /*
   * Exits the game.
   */
  private fun exitGame() {
    activity?.finish()
  }

  /*
  * Sets and resets the text field error status.
  */
  private fun setErrorTextField(error: Boolean) {
    if (error) {
      binding.inputLayout.isErrorEnabled = true
      binding.inputLayout.error = getString(R.string.try_again)
    } else {
      binding.inputLayout.isErrorEnabled = false
      binding.editText.text = null
    }
  }

  /*
   * Displays the next scrambled word on screen.
   */
  private fun updateNextWordOnScreen() {
    binding.word.text = viewModel.currentScrambledWord
  }

  /*
* Creates and shows an AlertDialog with the final score.
*/
  private fun showFinalScoreDialog() {
    MaterialAlertDialogBuilder(requireContext())
      .setTitle(getString(R.string.congratulations))
      .setMessage(getString(R.string.you_scored, viewModel.score))
      .setCancelable(false)
      .setNegativeButton(getString(R.string.exit)) { _, _ ->
        exitGame()
      }
      .setPositiveButton(getString(R.string.play_again)) { _, _ ->
        restartGame()
      }
      .show()
  }
}