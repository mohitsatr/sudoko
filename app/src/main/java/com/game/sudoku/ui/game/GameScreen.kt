package com.game.sudoku.ui.game

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.game.sudoku.R
import com.game.sudoku.ui.core.Cell
import com.game.sudoku.ui.data.core.PreferencesConstants
import com.game.sudoku.ui.game.components.GameMenu
import com.game.sudoku.ui.game.components.board.Board
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {

    val localView = LocalView.current // for vibration
    var restartButtonAngleState by remember { mutableFloatStateOf(0f) };
    val restartButtonAnimation: Float by animateFloatAsState(
        targetValue = restartButtonAngleState,
        animationSpec = tween(durationMillis = 250), label = "restartButtonAnimation"
    )
//
    val mistakeLimit by viewModel.mistakeLimit.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_MISTAKES_LIMIT
    )
    val errorHighlight by viewModel.mistakesMethod.collectAsStateWithLifecycle(
        initialValue = PreferencesConstants.DEFAULT_HIGHLIGHT_MISTAKES
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    // probably for the case when game is paused
                    AnimatedVisibility(visible = !viewModel.endGame) {
                        val rotationAngle by animateFloatAsState(
                            targetValue = 360f,
                            label = "Play/Pause game icon rotation"
                        )
                        IconButton(onClick = {
                            if (!viewModel.gamePlaying) {
                               viewModel.startTimer()
                            } else {
                                viewModel.pauseTimer()
                            }
                            viewModel.curCell = Cell(-1, -1, 0)
                        }) {
                            Icon(
                                // never seen rotate before
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
                    TopBoardSection(stringResource(viewModel.gameDifficulty.resName))
                    if (mistakeLimit && errorHighlight != 0) {
                        TopBoardSection(
                            stringResource(
                                id = R.string.mistakes_number_out_of,
                                viewModel.mistakesCount,
                                3
                            )
                        )
                    }
                    val timerEnabled by viewModel.timerEnabled.collectAsStateWithLifecycle(
                        initialValue = PreferencesConstants.DEFAULT_SHOW_TIMER
                    )
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
                        visible = !viewModel.gamePlaying && !viewModel.endGame
                        // enter
                        // exit
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
                Board(
                    modifier = Modifier
                        .blur(boardBlur)
                        .scale(boardScale, boardScale),
                    board = if (!viewModel.showSolution) viewModel.gameBoard else viewModel.solvedBoard,
                    size = viewModel.size,
                    mainTextSize = fontSizeValue,
                    autoFontSize = fontSizeFactor == 0,
                    notes = viewModel.notes,
                    selectedCell = viewModel.curCell,
                    onClick = { cell ->
                        viewModel.processInput(
                            cell = cell,
                            remainingUse = remainingUse,
                        )
                        if (!viewModel.gamePlaying) {
                            localView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                            viewModel.startTimer()
                        } },
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
                    questions = null,
                    renderNotes = false,
                    zoomable = false,
                    crossHighlight = false,
                    cages = viewModel.cages,
                    cellsToHighLight = null
                )
            }
        }

    }
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
