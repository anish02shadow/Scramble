package com.example69.scramble.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example69.scramble.data.MAX_NO_OF_WORDS
import com.example69.scramble.data.SCORE_INCREASE
import com.example69.scramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel(){
    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<GameUiState> =_uiState.asStateFlow()

    private lateinit var currentWord: String

    var userGuess by mutableStateOf("")
        private set

    // Set of words used in the game
    private var usedWords: MutableSet<String> = mutableSetOf()

    private fun pickRandomWordAndShuffle(): String {
        currentWord= allWords.random()
        if(usedWords.contains(currentWord)){
            return pickRandomWordAndShuffle()
        }
        else{
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    private fun shuffleCurrentWord(word: String): String{
        val tempWord=word.toCharArray()
        tempWord.shuffle()
        while (String(tempWord).equals(word)){
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    fun resetGame(){
        usedWords.clear()
        _uiState.value= GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }
    init {
        resetGame()
    }

    fun updateUserGuess(gussedWord:String){
        userGuess=gussedWord
    }

    fun checkUserGuess(){
        if(userGuess.equals(currentWord, ignoreCase = true)){
            val updateScore=_uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updateScore)
        }
        else{
            _uiState.update { currentState->
                currentState.copy(isGussedWordWrong=true)
            }
        }
        updateUserGuess("")
    }

    private fun updateGameState(updatedScore: Int){
        if(usedWords.size== MAX_NO_OF_WORDS){
            _uiState.update { currentState ->
                currentState.copy(
                    isGussedWordWrong = false,
                    score = updatedScore,
                    isGameOver=true
                )
            }
        }
        else{
        _uiState.update { currentState ->
            currentState.copy(
                isGussedWordWrong = false,
                currentScrambledWord = pickRandomWordAndShuffle(),
                score = updatedScore,
                currentWordCount = currentState.currentWordCount.inc()
            )
        }
        }
    }

    fun skipWord(){
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }


}