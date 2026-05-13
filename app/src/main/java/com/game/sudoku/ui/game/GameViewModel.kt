package com.game.sudoku.ui.game

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.game.sudoku.core.PreferencesConstants
import com.game.sudoku.data.datastore.AppSettingsManager
import com.game.sudoku.data.datastore.model.SavedGame
import com.game.sudoku.data.datastore.model.SudokuBoardModel
import com.game.sudoku.domain.GameBoard
import com.game.sudoku.domain.GameBoard.Companion.parseToGameBoard
import com.game.sudoku.domain.usecase.GetBoardUseCase
import com.game.sudoku.domain.usecase.GetSavedGameUseCase
import com.game.sudoku.domain.usecase.SaveGameUseCase
import com.game.sudoku.ui.core.Cell
import com.ramcosta.composedestinations.generated.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toKotlinDuration

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class GameViewModel @Inject constructor(
    appSettingsManager: AppSettingsManager,
    savedStateHandle: SavedStateHandle,
    private val getBoardUseCase: GetBoardUseCase,
    private val saveGameUseCase: SaveGameUseCase,
    private val getSavedGameUseCase: GetSavedGameUseCase,
) : ViewModel() {
    init {
        val navArgs: GameScreenNavArgs = savedStateHandle.navArgs()
        val continueSaved = navArgs.playedBefore

        viewModelScope.launch(Dispatchers.IO) {
            boardEntity = getBoardUseCase(navArgs.gameUid)
            val savedGame = getSavedGameUseCase(boardEntity.uid)
            withContext(Dispatchers.Default) {
                initialBoard = parseToGameBoard(boardEntity.initialBoard)
                gameDifficulty = boardEntity.difficulty

//                initialBoard.forEach { cells ->
//                    cells.forEach { cell ->
//                        cell.locked = cell.value != 0
//                    }
//                }
            }

            withContext(Dispatchers.Main) {
                if (savedGame != null && continueSaved) {
                     restoreSavedGame(savedGame)
                }
                else {
                    gameBoard = initialBoard
                }
                size = gameBoard.size
                remainingUsesList = countRemainingUses(gameBoard)
            }
            saveGame()
        }
    }

    private lateinit var boardEntity: SudokuBoardModel
    private lateinit var initialBoard: GameBoard

    var solvedBoard = GameBoard()

    var notesToggled by mutableStateOf(false)
    var gameBoard by mutableStateOf(GameBoard())
    var size by mutableIntStateOf(gameBoard.size)

    var gameDifficulty by mutableStateOf(Difficulty.EASY)

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

    var giveUpDialog by mutableStateOf(false)

    var showSolution by mutableStateOf(false)

    var remainingUse = appSettingsManager.remainingUse
    var remainingUsesList = emptyList<Int>()

    var digitFirstNumber by mutableIntStateOf(0)
    private val inputMethod = appSettingsManager.inputMethod
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 1
        )

    var currentCell by mutableStateOf(Cell(-1, -1, 0))

    var restartDialog by mutableStateOf(false)
    var gamePlaying by mutableStateOf(false)
    var endGame by mutableStateOf(false)
    private lateinit var timer: Timer
    private var duration = Duration.ZERO
    var timeText by mutableStateOf("00:00")

    fun startTimer() {
        if (!gamePlaying) {
            gamePlaying = true
            val updateRate = 50L

            timer = fixedRateTimer(initialDelay = updateRate, period = updateRate) {
                val prevTime = duration

                duration = duration.plus((updateRate * 1e6).toDuration(DurationUnit.NANOSECONDS))
                if (prevTime.toInt(DurationUnit.SECONDS)
                    != duration.toInt(DurationUnit.SECONDS)) {
                    timeText = duration.toFormattedString()

//                    if (gameBoard.any { it.any { cell -> cell.value != 0}}) {
//                        viewModelScope.launch(Dispatchers.IO) {
//                            saveGame()
//                            Log.d("StartTimer", "savedGame()")
//                        }
//                    }
                }
            }
        }
    }

    fun pauseTimer() {
        gamePlaying = false
        timer.cancel()
    }

    private fun restoreSavedGame(savedGame: SavedGame) {
        Log.d("restoreSavedGame", "Game restored: Game:${savedGame.uid} Board ${boardEntity.uid}")
        duration = savedGame.timer.toKotlinDuration()
        timeText = duration.toFormattedString()

        gameBoard = parseToGameBoard(savedGame.savedBoard)

//        for (i in gameBoard.indices) {
//            for (j in gameBoard[0].indices) {
//                gameBoard[i][j].locked = initialBoard[i][j].locked
//            }
    }

    private fun saveGame() {
        val savedGame = getSavedGameUseCase(boardEntity.uid)
        saveGameUseCase(savedGame, gameBoard, duration, boardEntity)
    }

    fun processInput(cell: Cell, remainingUse: Boolean, longTap: Boolean = false): Boolean {
        if (gamePlaying) {
            currentCell = if (currentCell.row == cell.row && currentCell.column == cell.column
                && digitFirstNumber == 0) {
                Cell(-1, -1)
            } else {
                cell
            }


            if (currentCell.row > -1 && currentCell.column > -1 && !gameBoard.isLocked(currentCell)) {

                if (inputMethod.value == 1 && digitFirstNumber > 0) {
                    if (!longTap) {
                        if (remainingUsesList.size >= digitFirstNumber
                            && remainingUsesList[digitFirstNumber - 1] > 0 || !remainingUse) {
                            processNumberInput(digitFirstNumber)
                            if (notesToggled) {
                                currentCell = Cell(currentCell.row, currentCell.column,
                                    digitFirstNumber)
                            }
                        }
                    }
                    else if (!currentCell.locked) {
                        gameBoard.setValue(currentCell.row, currentCell.column, 0)
                    }
                }
                remainingUsesList = countRemainingUses(gameBoard)
                return true
            }
            else {
                return false
            }
        } else {
            return false
        }
    }

//    private fun getBoardNoRef(): List<List<Cell>> =
//        gameBoard.map { items -> items.map { item -> item.copy() } }

//    private fun setValueCell(
//        value: Int,
//        row : Int = currCell.row,
//        col : Int = currCell.column
//    ): List<List<Cell>> {
//        var new = getBoardNoRef()
//
//        new[row][col].value = value
//        remainingUsesList = countRemainingUses(new)
//
//        if (currCell.row == row && currCell.column == col) {
//            currCell = currCell.copy(value = new[row][col].value)
//        }
//
//        if (value == 0) {
//            new[row][col].error = false
//            currCell.error = false
//            return new
//        }
//
//        return new
//    }

    fun processKeyboardInput(number: Int) {
//        digitFirstNumber = number
        if (gamePlaying) {
            if (inputMethod.value == 0 && !currentCell.locked && currentCell.column >= 0
                && currentCell.row >= 0) {
//              overrideInputMethodDF = false
                digitFirstNumber = 0
                processNumberInput(number)
//              undoRedoManager.addState(GameState(gameBoard, notes))
            } else if (inputMethod.value == 1) {
                digitFirstNumber = if (digitFirstNumber == number) 0 else number
                currentCell = Cell(-1, -1, digitFirstNumber)
            }
        }
//            eraseButtonToggled = false
    }

    private fun countRemainingUses(board: GameBoard): List<Int> {
        return (1..9).map { x -> size - board.countNumber(x + 1) }
    }

    private fun processNumberInput(number: Int) {
        if (currentCell.row > -1 && currentCell.column > -1 && gamePlaying && !currentCell.locked) {
            gameBoard.setValue(currentCell.row, currentCell.column, number)
            remainingUsesList = countRemainingUses(gameBoard)
        }
    }
}

fun Duration.toFormattedString(): String {
    return this.toComponents { hours, minutes, seconds, _ ->
        if (hours > 0) String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else String.format("%02d:%02d", minutes, seconds)
    }
}