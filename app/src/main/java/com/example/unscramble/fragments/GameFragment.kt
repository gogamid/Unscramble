package com.example.unscramble.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.unscramble.GameViewModel
import com.example.unscramble.R
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameFragment : Fragment() {
  private val viewModel: GameViewModel by viewModels()
  private lateinit var binding: GameFragmentBinding


  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.gameViewModel = viewModel

    binding.maxNoOfWords = MAX_NO_OF_WORDS

    binding.lifecycleOwner = viewLifecycleOwner

    // Setup a click listener for the Submit and Skip buttons.
    binding.submitBtn.setOnClickListener { onSubmitWord() }
    binding.skipBtn.setOnClickListener { onSkipWord() }
  }

  /*
  * Checks the user's word, and updates the score accordingly.
  * Displays the next scrambled word.
  */
  private fun onSubmitWord() {
    val playerWord = binding.editText.text.toString()

    if (viewModel.isUserWordCorrect(playerWord)) {
      setErrorTextField(false)
      if (!viewModel.nextWord()) {
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
    } else {
      showFinalScoreDialog()
    }
  }

  /*
   * Re-initializes the data in the ViewModel and updates the views with the new data, to
   * restart the game.
   */
  private fun restartGame() {
    viewModel.reinitializeData()
    setErrorTextField(false)
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
* Creates and shows an AlertDialog with the final score.
*/
  private fun showFinalScoreDialog() {
    MaterialAlertDialogBuilder(requireContext())
      .setTitle(getString(R.string.congratulations))
      .setMessage(getString(R.string.you_scored, viewModel.score.value))
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