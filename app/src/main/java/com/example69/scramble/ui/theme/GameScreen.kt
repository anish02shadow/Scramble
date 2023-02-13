package com.example69.scramble.ui.theme

import android.app.Activity
import android.app.AlertDialog
import android.net.wifi.WifiConfiguration.PairwiseCipher.strings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun GameScreen(modifier: Modifier = Modifier,
gameViewModel: GameViewModel= viewModel()) {

    val gameUiState by gameViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GameStatus(wordCount = gameUiState.currentWordCount,
        score = gameUiState.score)
        GameLayout(onUserGuessChanged = {gameViewModel.updateUserGuess(it)},
                    onKeyboardDone = {gameViewModel.checkUserGuess()},
                    currentScrambledWord = gameUiState.currentScrambledWord,
                    userGuess = gameViewModel.userGuess,
                    isGuessWrong = gameUiState.isGussedWordWrong)
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedButton(
                onClick = {gameViewModel.skipWord() },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(stringResource(com.example69.scramble.R.string.skip))
            }
            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 8.dp),
                onClick = { gameViewModel.checkUserGuess()}
            ) {
                Text(stringResource(com.example69.scramble.R.string.submit))
            }
        }
        if(gameUiState.isGameOver){
            FinalScoreDialog(onPlayAgain = { gameViewModel.resetGame() }, score =gameUiState.score )
        }
    }
}



@Composable
fun GameStatus(modifier: Modifier = Modifier,
wordCount:Int,
score: Int) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(48.dp),
    ) {
        Text(
            text = stringResource(com.example69.scramble.R.string.word_count, wordCount),
            fontSize = 18.sp,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            text = stringResource(com.example69.scramble.R.string.score, score),
            fontSize = 18.sp,
        )
    }
}


@Composable
private fun FinalScoreDialog(
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier,
    score: Int,
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = { Text(stringResource(com.example69.scramble.R.string.congratulations)) },
        text = { Text(stringResource(com.example69.scramble.R.string.you_scored, score)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(text = stringResource(com.example69.scramble.R.string.exit))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPlayAgain()
                }
            ) {
                Text(text = stringResource(com.example69.scramble.R.string.play_again))
            }
        }
    )
}
@Composable
fun GameLayout(
    currentScrambledWord: String,
    modifier: Modifier = Modifier,
    onKeyboardDone: ()->Unit,
    onUserGuessChanged:(String)->Unit,
    userGuess: String,
    isGuessWrong: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = currentScrambledWord,
            fontSize = 45.sp,
            modifier = modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(com.example69.scramble.R.string.instructions),
            fontSize = 17.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        OutlinedTextField(
            value = userGuess,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onUserGuessChanged,
            label = {
                if(isGuessWrong){
                    Text(stringResource(com.example69.scramble.R.string.wrong_guess))
                }
                else{
                    Text(stringResource(com.example69.scramble.R.string.enter_your_word))
                }},
            isError = isGuessWrong,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = androidx.compose.ui.text.input.ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onKeyboardDone() }
            ),
        )
    }
}
