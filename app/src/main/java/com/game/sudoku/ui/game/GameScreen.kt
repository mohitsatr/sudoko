package com.game.sudoku.ui.game

import android.os.Build
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.game.sudoku.R
import com.game.sudoku.core.PreferencesConstants
import com.game.sudoku.core.parser.SudokuParser
import com.game.sudoku.ui.core.Cell
import com.game.sudoku.ui.game.components.AnimatedNavigation
import com.game.sudoku.ui.game.components.DefaultKeyboard
import com.game.sudoku.ui.game.components.GameMenu
import com.game.sudoku.ui.game.components.board.DrawGameBoard
import com.game.sudoku.ui.theme.SudokuTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RequiresApi(Build.VERSION_CODES.O)
@Destination<RootGraph>(
    style = AnimatedNavigation::class,
    navArgs = GameScreenNavArgs::class
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val localView = LocalView.current // for vibration
    var restartButtonAngleState by remember { mutableFloatStateOf(0f) }
    val restartButtonAnimation: Float by animateFloatAsState(
        targetValue = restartButtonAngleState,
        animationSpec = tween(durationMillis = 250), label = "restartButtonAnimation"
    )

    val mistakeLimit by viewModel.mistakeLimit.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_MISTAKES_LIMIT
    )
    val errorHighlight by viewModel.mistakesMethod.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_HIGHLIGHT_MISTAKES
    )

    val remainingUse by viewModel.remainingUse.collectAsStateWithLifecycle(initialValue = false)
    val highlightIdentical by viewModel.identicalHighlight.collectAsStateWithLifecycle(
        initialValue = true
    )
    val boardScale by animateFloatAsState(
        targetValue = if (viewModel.gamePlaying || viewModel.endGame) 1f else 0.90f,
        label = "Game board scale"
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.popBackStack() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    AnimatedVisibility(visible = !viewModel.endGame) {
                        Log.d("GP", viewModel.gamePlaying.toString())
                        val rotationAngle by animateFloatAsState(
                            targetValue = if (viewModel.gamePlaying) 0f else 360f,
                            label = "Play/Pause game icon rotation"
                        )
                        IconButton(onClick = {
                            if (!viewModel.gamePlaying) viewModel.startTimer() else viewModel.pauseTimer()
                            viewModel.currCell = Cell(-1, -1, 0)
                        }) {
                            Icon(
                                modifier = Modifier.rotate(rotationAngle),
                                painter = painterResource(
                                    if (viewModel.gamePlaying) {
                                        R.drawable.ic_round_pause_24
                                    } else {
                                        R.drawable.ic_round_play_24
                                    }
                                ),
                                contentDescription = null
                            )
                        }
                    }
                    AnimatedVisibility(visible = !viewModel.endGame) {
                        IconButton(onClick = { viewModel.restartDialog = true }) {
                            Icon(
                                // why is not restartButtonAnimation defined closer?
                                modifier = Modifier.rotate(restartButtonAnimation),
                                painter = painterResource(R.drawable.ic_round_replay_24),
                                contentDescription = null
                            )
                        }
                    }
                    AnimatedVisibility(visible = !viewModel.endGame) {
                       Box {
                           IconButton(onClick = { viewModel.showMenu != !viewModel.showMenu}) {
                               Icon(
                                   Icons.Default.MoreVert,
                                   contentDescription = null
                               )
                           }
                       }
                       GameMenu(
                           expanded = viewModel.showMenu,
                           onDismiss = { viewModel.showMenu = false },
                           onGiveUpClick = {
                               viewModel.pauseTimer()
                               viewModel.giveUpDialog = true
                           },
                           onSettingsClick = {
                               // navigate to settings
                               viewModel.showMenu = false
                           },
                           onExportClicked = {}
                       )
                   }
                }
            )
        }
    ) { scaffoldPaddings ->
        Column(
            modifier = Modifier
                .padding(scaffoldPaddings)
                .padding(horizontal = 12.dp),
            // https://stackoverflow.com/questions/77858902/difference-between-verticalarrangement-arrangement-center-and-textalign
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            AnimatedVisibility(visible = !viewModel.endGame) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TopBoardSection(viewModel.gameDifficulty.name)
                    if (mistakeLimit && errorHighlight != 0) {
                        TopBoardSection(
                            stringResource(
                                id = R.string.mistakes_number_out_of,
                                viewModel.mistakesCount,
                                3
                            )
                        )
                    }
//                    val timerEnabled by viewModel.timerEnabled.collectAsStateWithLifecycle(
//                        initialValue = PreferencesConstants.DEFAULT_SHOW_TIMER
//                    )
                    val timerEnabled = true
                    AnimatedVisibility(visible = timerEnabled || viewModel.endGame) {
                        TopBoardSection(viewModel.timeText)
                    }
                }
            }

//            render
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    AnimatedVisibility(
                        visible = !viewModel.gamePlaying && !viewModel.endGame,
                        enter = expandVertically(clip = false) + fadeIn(),
                        exit = shrinkVertically(clip = false) + fadeOut()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(12.dp)
                        )
                    }
                }
                 Log.d("currCell", "${viewModel.currCell}")
                // viewModel.currCell is correctly being updated here.
                DrawGameBoard(
                    modifier = Modifier
                        .scale(boardScale, boardScale),
                    board = if (!viewModel.showSolution) viewModel.gameBoard else viewModel.solvedBoard,
                    notes = viewModel.notes,
                    selectedCell = viewModel.currCell,
                    onClick = { cell ->
                        viewModel.processInput(
                            cell = cell,
                            remainingUse = remainingUse,
                        )
                        if (!viewModel.gamePlaying) {
                            localView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                            viewModel.startTimer()
                        }
                    },
                    onLongClick = { cell ->
                        if (viewModel.processInput(cell, remainingUse, longTap = true)) {
                            localView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                        }
                    },
                    identicalNumbersHighlight = highlightIdentical,
                    errorsHighlight = errorHighlight != 0,
                    positionLines = false,
                    notesToHighLight = emptyList(),
                    enabled = viewModel.gamePlaying && !viewModel.endGame,
                    renderNotes = false,
                    zoomable = false,
                    crossHighlight = false,
                    cellsToHighLight = null
                )
            }
            AnimatedContent(!viewModel.endGame, label = "") { contentState ->
                if (contentState) {
                    Column (
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        DefaultKeyboard(
                            size = viewModel.size,
                            remainingUse = if (remainingUse) viewModel.remainingUsesList else null,
                            onClick = {
//                                    viewModel.processInput
                            },
                            selected = 3
                        )
                    }
                }
            }
        }
    }

    // without this, timer won't start when board is loaded
    LaunchedEffect(Unit) {
        if (!viewModel.endGame) {
            viewModel.startTimer()
        }
    }
//
//    OnLifeCycleEvent { _, event ->
//        when (event) {
//            Lifecycle.Event.ON_RESUME -> {
//                if (viewModel.gamePlaying) viewModel.startTimer()
//            }
//
//            Lifecycle.Event.ON_PAUSE -> {
//                viewModel.pauseTimer()
//                viewModel.currCell = Cell(-1, -1, 0)
//            }
//
//            Lifecycle.Event.ON_DESTROY -> viewModel.pauseTimer()
//            else -> {}
//        }
//    }
}

@Composable
fun TopBoardSection(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

//@Preview()
//@Composable
//fun PreviewScreen() {
//    val sudokuParser = SudokuParser()
//    sudokuParser.
//    SudokuTheme {
//        Surface {
//            DrawGameBoard(
//            )
//        }
//    }
//}
