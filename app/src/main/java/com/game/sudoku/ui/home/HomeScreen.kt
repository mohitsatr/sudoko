package com.game.sudoku.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.game.sudoku.R
import com.game.sudoku.ui.theme.SudokuTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.GameScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Destination<RootGraph>(start = true)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {

    Log.d("rop", viewModel.readyToPlay.toString())

    var continueLastGame by remember { mutableStateOf(false) }
//    val lastGames = viewModel.lastGames.collectAsStateWithLifecycle(emptyMap())
//    val lastGamesSize = lastGames.value.size

//    val lastGames1 by viewModel.lastGames.collectAsStateWithLifecycle(initialValue = emptyMap())

    LaunchedEffect(viewModel.readyToPlay) {
        if (viewModel.readyToPlay && viewModel.insertedBoardUid != -1L) {
            navigator.navigate(
                GameScreenDestination(
                    gameUid = viewModel.insertedBoardUid,
                    playedBefore = false,
                )
            )
            viewModel.readyToPlay = false
        }
    }

    HomeScreenContent(
        onStartNewGameClick = { viewModel.startGame() },
        onContinueOldGameClick = { continueLastGame = true },
        onContinueLastDialogDismissed = { continueLastGame = false },
        resumeGame = { uid ->
            navigator.navigate(
                GameScreenDestination(
                    gameUid = uid,
                    playedBefore = true
                )
            )
            continueLastGame = false
        },
        isContinueLastGame = continueLastGame,
        isGenerating = viewModel.isGenerating,
        isSolving = viewModel.isSolving,
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    onStartNewGameClick: () -> Unit,
    onContinueOldGameClick: () -> Unit,
    isContinueLastGame: Boolean,
    isGenerating: Boolean,
    isSolving: Boolean,
    onContinueLastDialogDismissed: () -> Unit,
    resumeGame: (Long) -> Unit,
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onStartNewGameClick
            ) {
                Text("Start New Game")
            }
            Button(
                onClick = onContinueOldGameClick
            ) {
                Text("Continue Game")
            }
        }

        if (isContinueLastGame) {
            ModalBottomSheet(
                onDismissRequest = onContinueLastDialogDismissed,
            ) {
                Text("Showing last $ games")
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(2) {
                        Button(
                            onClick = { resumeGame(it.toLong()) }
                        ) {
//                            Text(text = "Last Played: ${it.first.lastPlayed.toString()} for ${it.first.timer} in ${it.second.difficulty.name} mode")
                        }
                    }
                }
            }
        }
        if (isGenerating || isSolving) {
            GeneratingDialog(
                onDismiss = {},
                text = when {
                    isGenerating -> stringResource(R.string.dialog_generating)
                    isSolving -> stringResource(R.string.dialog_solving)
                    else -> ""
                }
            )
        }
    }
}

@Composable
fun GeneratingDialog(
    onDismiss: () -> Unit,
    text: String,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Column(
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MainMenuPreview() {
    SudokuTheme {
        HomeScreenContent(
            onStartNewGameClick = {},
            onContinueOldGameClick = {},
            isContinueLastGame = false,
            isGenerating = false,
            isSolving = false,
            onContinueLastDialogDismissed = {},
            resumeGame = {}
        )
    }
}
