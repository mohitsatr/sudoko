package com.game.sudoku.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.game.sudoku.core.PreferencesConstants
import com.game.sudoku.data.datastore.AppSettingsManager
import com.game.sudoku.ui.core.Cell
import com.game.sudoku.ui.core.qqwing.GameDifficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

@HiltViewModel
class GameViewModel @Inject constructor(
    private val appSettingsManager: AppSettingsManager,
) : ViewModel() {
    init {

//        val sudokuParser = SudokuPars
//        viewModelScope.launch(Dispatchers.IO) {
//            withContext(Dispatchers.Default) {
//                initialBoard = sudokuParser.parseBoard(
////                    boardEntity.initialBoard,
////                    boardEntity.type
////                ).toList()
////                initialBoard.forEach { cells ->
////                    cells.forEach { cell ->
////                        cell.locked = cell.value != 0
////                    }
////                }
////
//                if (boardEntity.solvedBoard.isNotBlank() && !boardEntity.solvedBoard.contains("0")) {
//                    solvedBoard = sudokuParser.parseBoard(
//                        boardEntity.solvedBoard,
//                        boardEntity.type
//                    )
//                    boardEntity.killerCages?.let { cagesString ->
//                        cages = sudokuParser.parseKillerSudokuCages(cagesString)
//                    }
//                    for (i in solvedBoard.indices) {
//                        for (j in solvedBoard.indices) {
//                            solvedBoard[i][j].locked = initialBoard[i][j].locked
//                        }
//                    }
//                } else {
//                    withContext(Dispatchers.Main) {
//                        solveBoard()
//                    }
//                }
//            }
//            withContext(Dispatchers.Main) {
//                if (savedGame != null && continueSaved) {
//                    restoreSavedGame(savedGame)
//                } else {
//                    gameBoard = initialBoard
//                }
//                size = gameBoard.size
//                undoRedoManager = UndoRedoManager(GameState(gameBoard, notes))
//                remainingUsesList = countRemainingUses(gameBoard)
//            }
//            saveGame()
//        }
    }

    fun startTimer() {
        TODO("Not yet implemented")
    }

    fun pauseTimer() {
        TODO("Not yet implemented")
    }

    val cages: Any
    val notes: Any
    val solvedBoard: List<List<Cell>>
    val gameBoard by mutableStateOf(List(9) { row -> List(9) { col -> Cell(row, col, 0) } })
    val timeText by mutableStateOf("00:00")
//    val timerEnabled = appSettingsManager.timerEnabled

    val mistakesCount by mutableIntStateOf(0)

    val mistakeLimit = appSettingsManager.mistakesLimit.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        PreferencesConstants.DEFAULT_MISTAKES_LIMIT
    )

    // mistake checking method
    val mistakesMethod = appSettingsManager.highlightMistakes.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        PreferencesConstants.DEFAULT_HIGHLIGHT_MISTAKES
    )
    val gameDifficulty by mutableStateOf(GameDifficulty.Unspecified)
    var giveUpDialog by mutableStateOf(false)
    var showMenu by mutableStateOf(false)

    var showSolution by mutableStateOf(false)

    var remainingUse = appSettingsManager.remainingUse

    // identical numbers highlight
    val identicalHighlight = appSettingsManager.highlightIdentical

    var restartDialog by mutableStateOf(false)
    var curCell by mutableStateOf(Cell(-1, -1, 0))
    var gamePlaying by mutableStateOf(false)
    var endGame by mutableStateOf(false)


    fun processInput(cell: Cell, remainingUse: Boolean, longTap: Boolean = false): Boolean {
        if (gamePlaying) {
            return true
        } else {
            return false
        }
    }
}

