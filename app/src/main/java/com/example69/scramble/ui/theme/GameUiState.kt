package com.example69.scramble.ui.theme

data class GameUiState(
    val currentScrambledWord: String="",
val isGussedWordWrong: Boolean=false,
    val score: Int=0,
    val currentWordCount: Int=0,
    val isGameOver: Boolean=false

)
