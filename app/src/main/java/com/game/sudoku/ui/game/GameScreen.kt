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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.game.sudoku.R
import com.game.sudoku.core.PreferencesConstants
import com.game.sudoku.domain.GameBoard
import com.game.sudoku.domain.GameBoard.Companion.parseToGameBoard
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
    )1
    val mistakeLimit by viewModel.mistakeLimit.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_MISTAKES_LIMIT
    )
    val errorHighlight by viewModel.mistakesMethod.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_HIGHLIGHT_MISTAKES
    )
    val remainingUse by viewModel.remainingUse.collectAsStateWithLifecycle(initialValue = false)
    val lifecycleOwner = LocalLifecycleOwner.current

    // without this, timer won't start when board is loaded
    LaunchedEffect(Unit) {
        if (!viewModel.endGame) {
            viewModel.startTimer()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                viewModel.pauseTimer()
                viewModel.currentCell = Cell(-1, -1, 0)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    GameScreenContent(
        isGameRunning = viewModel.gamePlaying,
        hasGameEnded = viewModel.endGame,
        boardSize = viewModel.size,
        difficultyLevel = viewModel.gameDifficulty.name,
        mistakeLimit = mistakeLimit,
        errorHighlight = errorHighlight,
        remainingUsesList = viewModel.remainingUsesList,
        remainingUse = remainingUse,
        showSolution = viewModel.showSolution,
        unSolvedBoard = viewModel.gameBoard,
        solvedBoard = viewModel.solvedBoard,
        curCell = viewModel.currentCell,
        timeText = viewModel.timeText,
        onBackClick = { navigator.popBackStack() },
        onPauseButtonClick = {
            if (!viewModel.gamePlaying) viewModel.startTimer() else viewModel.pauseTimer()
            viewModel.currentCell = Cell(-1, -1, 0)
        },
        onRestartButtonClick = {},
        onGiveUp = {
            viewModel.pauseTimer()
            viewModel.giveUpDialog = true
        },
        onKeyboardClick = { number ->
            viewModel.processKeyboardInput(number)
        },
        onCellClick = { cell ->
            viewModel.processInput(
                cell = cell,
                remainingUse = remainingUse,
            )
            if (viewModel.gamePlaying) {
                localView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                viewModel.startTimer()
            }
        },
    )
}

@Composable
fun GameScreenContent(
    isGameRunning: Boolean,
    hasGameEnded: Boolean,
    boardSize: Int,
    onBackClick: () -> Unit,
    onPauseButtonClick: () -> Unit,
    onRestartButtonClick: () -> Unit,
    onGiveUp: () -> Unit,
    onKeyboardClick: (Int) -> Unit,
    onCellClick: (Cell) -> Unit,
    difficultyLevel: String,
    mistakeLimit: Boolean,
    errorHighlight: Int,
    remainingUsesList: List<Int>,
    remainingUse: Boolean,
    showSolution: Boolean,
    unSolvedBoard: GameBoard,
    solvedBoard: GameBoard,
    curCell: Cell,
    timeText: String
) {
    val boardScale by animateFloatAsState(
        targetValue = if (isGameRunning || hasGameEnded) 1f else 0.90f,
        label = "Game board scale"
    )

    Scaffold(
        topBar = {
            GameHeader(
                isGameRunning = isGameRunning,
                hasGameEnded = hasGameEnded,
                onBackClick = onBackClick ,
                onPauseButtonClick = onPauseButtonClick,
                onRestartButtonClick = {},
                onGiveUp = onGiveUp,
                onGameMenuDismiss = {},
                onMenuClick = {}
            )
        }
    ) { scaffoldPaddings ->
        Column(
            modifier = Modifier
                .padding(scaffoldPaddings)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            AnimatedVisibility(visible = !hasGameEnded) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TopBoardSection(difficultyLevel)
                    if (mistakeLimit && errorHighlight != 0) {
                        TopBoardSection(
                            stringResource(
                                id = R.string.mistakes_number_out_of, 3
                            )
                        )
                    }
//                    val timerEnabled by viewModel.timerEnabled.collectAsStateWithLifecycle(
//                        initialValue = PreferencesConstants.DEFAULT_SHOW_TIMER
//                    )
                    val timerEnabled = true
                    AnimatedVisibility(visible = timerEnabled || hasGameEnded) {
                        TopBoardSection(timeText)
                    }
                }
            }

//          render
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    AnimatedVisibility(
                        visible = !isGameRunning && !hasGameEnded,
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
                // viewModel.currCell is correctly being updated here.
                DrawGameBoard(
                    modifier = Modifier
                        .scale(boardScale, boardScale),
                    board = if (!showSolution) unSolvedBoard else solvedBoard,
                    selectedCell = curCell,
                    onClick = onCellClick,
                    identicalNumbersHighlight = true,
                    errorsHighlight = errorHighlight != 0,
                    positionLines = false,
                    enabled = true,
                    cellsToHighLight = null
                )
            }
            AnimatedContent(!hasGameEnded, label = "") { contentState ->
                if (contentState) {
                    Column (
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        DefaultKeyboard(
                            size = boardSize,
                            remainingUse = if (remainingUse) remainingUsesList else null,
                            onClick = onKeyboardClick,
                            selected = 0
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameHeader(
    isGameRunning: Boolean,
    hasGameEnded: Boolean,
    onBackClick: () -> Unit,
    onPauseButtonClick: () -> Unit,
    onRestartButtonClick: () -> Unit,
    onMenuClick: () -> Unit,
    onGameMenuDismiss: () -> Unit,
    onGiveUp: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_arrow_back_24),
                    contentDescription = null
                )
            }
        },
        actions = {
            AnimatedVisibility(visible = !hasGameEnded) {
                Log.d("GP", "$isGameRunning")
                val rotationAngle by animateFloatAsState(
                    targetValue = if (isGameRunning) 0f else 360f,
                    label = "Play/Pause game icon rotation"
                )
                IconButton(onClick = onPauseButtonClick) {
                    Icon(
                        modifier = Modifier.rotate(rotationAngle),
                        painter = painterResource(
                            if (isGameRunning) {
                                R.drawable.ic_round_pause_24
                            } else {
                                R.drawable.ic_round_play_24
                            }
                        ),
                        contentDescription = null
                    )
                }
            }
            AnimatedVisibility(visible = hasGameEnded) {
                IconButton(onClick = onRestartButtonClick) {
                    Icon(
                        modifier = Modifier.rotate(180f),
                        painter = painterResource(R.drawable.ic_round_replay_24),
                        contentDescription = null
                    )
                }
            }
            AnimatedVisibility(visible = !hasGameEnded) {
                Box {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null
                        )
                    }
                }
                GameMenu(
                    expanded = false,
                    onDismiss = onGameMenuDismiss,
                    onGiveUpClick = onGiveUp,
                    onSettingsClick = {},
                    onExportClicked = {}
                )
            }
        }
    )
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


const val fakeGameString = "530070000600195000098000060800060003400803001700020006060000280000419005000080079"
val fakeGameBoard = parseToGameBoard(fakeGameString)

@Preview
@Composable
fun PreviewScreen() {
    SudokuTheme {
        GameScreenContent(
            isGameRunning = true,
            hasGameEnded = false,
            boardSize = 9,
            onBackClick = {},
            onPauseButtonClick = {},
            onRestartButtonClick = {},
            onGiveUp = {},
            onKeyboardClick = {},
            onCellClick = {},
            difficultyLevel = "Easy",
            mistakeLimit = true,
            errorHighlight = 1,
            remainingUsesList = emptyList(),
            remainingUse = true,
            showSolution = false,
            unSolvedBoard = fakeGameBoard,
            solvedBoard = fakeGameBoard,
            curCell = Cell(0, 0, 0),
            timeText = "0:0",
        )
    }
}
