package com.mohitsatr.game.ui.game

import android.annotation.SuppressLint
import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohitsatr.domain.GameBoard.Companion.parseToGameBoard
import com.mohitsatr.game.R
import com.mohitsatr.game.ui.game.components.GameKeyboard
import com.mohitsatr.game.ui.game.components.GameMenu
import com.mohitsatr.game.ui.game.components.board.DrawGameBoard
import com.mohitsatr.ui.SudokuBoardColors.LocalBoardColors
import com.mohitsatr.ui.theme.SudokuTheme
import io.github.ilikeyourhat.kudoku.model.Cell

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameUid: Long,
    playedBefore: Boolean,
    onBack: () -> Unit,
    viewModel: GameViewModel = hiltViewModel(),
) {
    val localView = LocalView.current // for vibration

    val gamePlayUiState by viewModel.gamePlayState.collectAsStateWithLifecycle()
    val boardUi by viewModel.boardState.collectAsStateWithLifecycle()

    LaunchedEffect(gameUid, playedBefore) {
        viewModel.loadGame(gameUid, playedBefore)
    }

    var restartButtonAngleState by remember { mutableFloatStateOf(0f) }
    val restartButtonAnimation: Float by animateFloatAsState(
        targetValue = restartButtonAngleState,
        animationSpec = tween(durationMillis = 250), label = "restartButtonAnimation"
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    // without this, timer won't start when board is loaded
    LaunchedEffect(Unit) {
        if (!viewModel.endGame) {
            viewModel.startGame()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_PAUSE) {
                viewModel.pauseGame()

            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    GameScreenContent(
        boardUi = boardUi,
        isGameRunning = gamePlayUiState is GamePlayUiState.Running,
        hasGameEnded = viewModel.endGame,
        boardSize = viewModel.size,
        onBackClick = onBack,
        onPauseButtonClick = {
            if (gamePlayUiState is GamePlayUiState.Running) viewModel.pauseGame()
            else viewModel.startGame()
        },
        onGiveUp = {
            viewModel.finishGame()
        },
        onKeyboardClick = { number ->
            viewModel.processKeyboardInput(number)
        },
        onBoardCellClick = { cell ->
            viewModel.processInput(
                cell = cell,
            )
            if (gamePlayUiState is GamePlayUiState.Running) {
                localView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                viewModel.startGame()
            }
        },
    )
}

@Suppress("ParamsComparedByRef")
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameScreenContent(
    boardUi: GameBoardState,
    isGameRunning: Boolean,
    hasGameEnded: Boolean,
    boardSize: Int,
    onBackClick: () -> Unit,
    onPauseButtonClick: () -> Unit,
    onGiveUp: () -> Unit,
    onKeyboardClick: (Int) -> Unit,
    onBoardCellClick: (Cell) -> Unit,
) {
    val boardScale by animateFloatAsState(
        targetValue = if (isGameRunning || hasGameEnded) 1f else 0.90f,
        label = "Game board scale"
    )

    var cellSize by remember(boardSize) { mutableFloatStateOf(-1f) }

    Scaffold(
        topBar = {
            GameHeader(
                isGameRunning = isGameRunning,
                hasGameEnded = hasGameEnded,
                onBackClick = onBackClick,
                timerText = boardUi.timeText,
                onPauseButtonClick = onPauseButtonClick,
                onRestartButtonClick = {},
                onGiveUp = onGiveUp,
                onGameMenuDismiss = {},
                onMenuClick = {}
            )
        },
        containerColor = LocalBoardColors.current.backgroundColor,
    ) { scaffoldPaddings ->
        Column(
            modifier = Modifier
                .padding(scaffoldPaddings)
                .padding(horizontal = 12.dp)
        ) {
            Box(modifier = Modifier.weight(0.7f)) {
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
                BoxWithConstraints(
                    modifier = Modifier.scale(boardScale, boardScale)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    val maxWidth = constraints.maxWidth.toFloat()
                    // single cell size - 99 / 9 = 11 - 11 * 11 sq^2
                    cellSize = maxWidth / boardSize.toFloat()

                    DrawGameBoard(
                        board = boardUi.displayBoard,
                        maxWidth = maxWidth,
                        selectedCell = boardUi.selectedCell,
                        onClick = onBoardCellClick,
                        enabled = true,
                    )
                }
            }
            Box(modifier = Modifier.weight(0.3f)) {
                GameKeyboard(
                    keysAndCount = boardUi.remainingKeyUseCount,
                    onClick = onKeyboardClick,
                    keySize = cellSize * 0.5f,
                    selectedKey = boardUi.selectedKey
                )
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
    onGiveUp: () -> Unit,
    timerText: String,
) {
    val localColors = LocalBoardColors.current
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TopBarTimer(timerText)
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_arrow_back_24),
                    contentDescription = null,
                    tint = localColors.nonSelectedNumberColor
                )
            }
        },
        actions = {
            AnimatedVisibility(visible = isGameRunning) {
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
                        contentDescription = null,
                        tint = localColors.nonSelectedNumberColor

                    )
                }
            }
            AnimatedVisibility(visible = isGameRunning) {
                IconButton(onClick = onRestartButtonClick) {
                    Icon(
                        modifier = Modifier.rotate(180f),
                        painter = painterResource(R.drawable.ic_round_replay_24),
                        contentDescription = null,
                        tint = localColors.nonSelectedNumberColor

                    )
                }
            }
            AnimatedVisibility(visible = !hasGameEnded) {
                Box {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = localColors.nonSelectedNumberColor

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
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = localColors.backgroundColor
        )
    )
}

@Composable
fun TopBarTimer(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 4.dp),
            color = LocalBoardColors.current.nonSelectedNumberColor
        )
    }
}


@Preview
@Composable
fun GameScreenPreview() {
    val fakeGameString =
        "530070000600195000098000060800060003400803001700020006060000280000419005000080079"
    val fakeGameBoard = parseToGameBoard(fakeGameString)
    val fakeBoardState = GameBoardState()
    SudokuTheme {
        GameScreenContent(
            boardUi = fakeBoardState,
            isGameRunning = true,
            hasGameEnded = true,
            boardSize = 9,
            onBackClick = {},
            onPauseButtonClick = {},
            onGiveUp = {},
            onKeyboardClick = {},
            onBoardCellClick = {},
        )
    }
}
