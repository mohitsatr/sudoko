package com.game.sudoku.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.game.sudoku.R
import com.mohitsatr.domain.repository.SavedGameModel
import com.mohitsatr.domain.repository.SudokuBoardModel
import com.mohitsatr.ui.SudokuBoardColors.LocalBoardColors
import com.mohitsatr.ui.theme.SudokuTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.collections.emptyMap

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
    val lastGames = viewModel.lastGames.collectAsStateWithLifecycle(emptyMap())

    LaunchedEffect(viewModel.readyToPlay) {
        if (viewModel.readyToPlay && viewModel.insertedBoardUid != -1L) {
//            navigator.navigate(
//                GameScreenDestination(
//                    gameUid = viewModel.insertedBoardUid,
//                    playedBefore = false,
//                )
//            )
            viewModel.readyToPlay = false
        }
    }

    HomeScreenContent(
        onStartNewGameClick = { viewModel.startGame() },
        onContinueOldGameClick = { continueLastGame = true },
        onContinueLastDialogDismissed = { continueLastGame = false },
        resumeGame = { uid ->
//            navigator.navigate(
//                GameScreenDestination(
//                    gameUid = uid,
//                    playedBefore = true
//                )
//            )
            continueLastGame = false
        },
        isContinueLastGame = continueLastGame,
        isGenerating = viewModel.isGenerating,
        isSolving = viewModel.isSolving,
        lastGames = lastGames,
        onThemeIconClick = { viewModel.toggleTheme() }
    )
}

@SuppressLint("RememberInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    onStartNewGameClick: () -> Unit,
    onContinueOldGameClick: () -> Unit,
    onThemeIconClick: () -> Unit,
    isContinueLastGame: Boolean,
    isGenerating: Boolean,
    isSolving: Boolean,
    onContinueLastDialogDismissed: () -> Unit,
    resumeGame: (Long) -> Unit,
    lastGames: State<Map<SavedGameModel, SudokuBoardModel>>,
) {
    val localColors = LocalBoardColors.current
    val newGameButtonInteractionSource = remember { MutableInteractionSource() }
    val continueButtonInteractionSource = remember { MutableInteractionSource() }
    Scaffold(
        topBar = { HomeTopBar(onThemeIconClick = onThemeIconClick) },
        contentWindowInsets = WindowInsets(0.dp, top = 0.dp, right = 0.dp, bottom = 0.dp),
        containerColor = localColors.backgroundColor,
        contentColor = localColors.backgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val isNewButtonPressed =
                    isGenerating || newGameButtonInteractionSource.collectIsPressedAsState().value
                GameButton(
                    "Start New Game",
                    isNewButtonPressed,
                    newGameButtonInteractionSource
                ) {
                    onStartNewGameClick()
                }
                Spacer(modifier = Modifier.height(12.dp))
                val isResumeButtonPressed =
                    isContinueLastGame || continueButtonInteractionSource.collectIsPressedAsState().value
                GameButton(
                    "Resume",
                    isResumeButtonPressed,
                    continueButtonInteractionSource
                ) {
                    onContinueOldGameClick()
                }
            }

            val games = lastGames.value.toList()
            games.forEachIndexed { index,pair ->
                Log.d("getting last games ", "$index $pair")
            }
            if (isContinueLastGame) {
                ModalBottomSheet(
                    onDismissRequest = onContinueLastDialogDismissed,
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                       items(items = games, key = { it.first.uid }) { savedGame ->
                           Text(
                               text = savedGame.first.lastPlayed.toString()
                           )
                           LastGameCard(
                               lastPlayed = savedGame.first.lastPlayed.toString(),
                               duration = savedGame.first.timer.toString(),
                               savedBoard = savedGame.second.initialBoard,
                           ) {}
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
}

@Composable
fun GameButton(
    text: String,
    selected: Boolean,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
) {
    val localColors = LocalBoardColors.current

    val backgroundColor = if (selected)
        localColors.selectedButtonBackground
    else
        localColors.nonSelectedButtonBackground
    val outlineColor = if (selected) localColors.selectedButtonBackground
    else localColors.thinLineColor
    val textColor = if (selected) localColors.selectedButtonTextColor
    else localColors.nonSelectedButtonTextColor

    Log.d("GameButton", "$selected")
    Surface(
        shape = RoundedCornerShape(40.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, outlineColor),
        modifier = Modifier
            .height(40.dp)
            .clip(RoundedCornerShape(40.dp))
            .combinedClickable(
                interactionSource = interactionSource,
                onClick = onClick
            )
            .width(240.dp),
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 16.sp
            )
        }
    }
}

@SuppressLint("RememberInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onThemeIconClick: () -> Unit,
) {
    val localColors = LocalBoardColors.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(localColors.backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                ImageVector.vectorResource(R.drawable.palette_24px),
                tint = localColors.selectedButtonBackground,
                modifier = Modifier
                    .size(28.dp)
                    .clickable(onClick = onThemeIconClick),
                contentDescription = ""
            )
        }
        Spacer(Modifier.size(40.dp))
        Text(
            text = stringResource(R.string.app_name),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
            color = localColors.selectedButtonBackground
        )
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

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun MainMenuPreview() {
    SudokuTheme {
        HomeScreenContent(
            onStartNewGameClick = {},
            onContinueOldGameClick = {},
            onThemeIconClick = {},
            isContinueLastGame = false,
            isGenerating = false,
            isSolving = false,
            onContinueLastDialogDismissed = {},
            resumeGame = {},
            lastGames = mutableStateOf(emptyMap())
        )
    }
}
