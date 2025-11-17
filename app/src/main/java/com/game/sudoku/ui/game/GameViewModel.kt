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
import com.game.sudoku.core.parser.SudokuParser
import com.game.sudoku.core.sudokuUtils.SudokuUtils
import com.game.sudoku.data.datastore.AppSettingsManager
import com.game.sudoku.data.datastore.model.SavedGame
import com.game.sudoku.data.datastore.model.SudokuBoard
import com.game.sudoku.domain.repository.BoardRepository
import com.game.sudoku.domain.repository.SavedGameRepository
import com.game.sudoku.domain.usecase.GetBoardUseCase
import com.game.sudoku.ui.core.Cell
import com.ramcosta.composedestinations.generated.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import io.github.ilikeyourhat.kudoku.type.Classic9x9
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class GameViewModel @Inject constructor(
    private val savedGameRepository: SavedGameRepository,
    private val appSettingsManager: AppSettingsManager,
    private val savedStateHandle: SavedStateHandle,
    private val getBoardUseCase: GetBoardUseCase,
    private val boardRepository: BoardRepository
) : ViewModel() {
    init {
        val sudokuParser = SudokuParser()
        val navArgs: GameScreenNavArgs = savedStateHandle.navArgs()
        val continueSaved = navArgs.playedBefore

        viewModelScope.launch(Dispatchers.IO) {
            boardEntity = getBoardUseCase(navArgs.gameUid)
            val savedGame = savedGameRepository.get(boardEntity.uid - 1)

            withContext(Dispatchers.Main) {
                gameType = boardEntity.type as Classic9x9
                gameDifficulty = boardEntity.difficulty
            }

            withContext(Dispatchers.Default) {
                initialBoard = sudokuParser.parseBoard(
                    boardEntity.initialBoard,
                    boardEntity.type as Classic9x9
                ).toList()
                initialBoard.forEach { cells ->
                    cells.forEach { cell ->
                        cell.locked = cell.value != 0
                    }
                }
            }

            withContext(Dispatchers.Main) {
//                if (savedGame != null && continueSaved) {
//                     restoreSavedGame()
//                }
//                else {
                gameBoard = initialBoard
//                }
                size = gameBoard.size
                remainingUsesList = countRemainingUses(gameBoard)
            }
            saveGame()
        }
    }

    private lateinit var boardEntity: SudokuBoard
    private lateinit var initialBoard: List<List<Cell>>

    var solvedBoard = emptyList<List<Cell>>()
//    var cages by mutableStateOf(emptyList<Cage>())

    var size by mutableIntStateOf(9)
    var notesToggled by mutableStateOf(false)
    var notes by mutableStateOf(emptyList<Any>())
    var gameBoard by mutableStateOf(List(9) { row -> List(9) { col -> Cell(row, col, 0) } })
//    val timerEnabled = appSettingsManager.timerEnabled

    var gameType by mutableStateOf(Classic9x9)
    var gameDifficulty by mutableStateOf(Difficulty.EASY)

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

    var giveUpDialog by mutableStateOf(false)
    var showMenu by mutableStateOf(false)

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

    // identical numbers highlight
    val identicalHighlight = appSettingsManager.highlightIdentical

//    private var undoRedoManager = UndoRedoManager(GameState(gameBoard, notes))
    var currCell by mutableStateOf(Cell(-1, -1, 0))

    private var sudokuUtils = SudokuUtils()

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

                    if (gameBoard.any { it.any { cell -> cell.value != 0}}) {
                        viewModelScope.launch(Dispatchers.IO) {
                            saveGame()
                        }
                    }
                }
            }
        }
    }

    fun pauseTimer() {
        gamePlaying = false
        timer.cancel()
    }

    private fun solveBoard() {

    }

    private fun restoreSavedGame(savedGame: SavedGame?) {

    }

    private suspend fun saveGame() {
        val savedGame = savedGameRepository.get(boardEntity.uid)
        val sudokuParser = SudokuParser()
        if (savedGame != null) {
            savedGameRepository.update(
                savedGame.copy(
                    timer = java.time.Duration.ofSeconds(duration.inWholeSeconds),
                    currentBoard = sudokuParser.boardToString(gameBoard),
                    notes = sudokuParser.notesToString(notes),
                    lastPlayed = ZonedDateTime.now()
                )
            )
        }
        else {
            savedGameRepository.insert(
                SavedGame(
                    uid = boardEntity.uid,
                    currentBoard = sudokuParser.boardToString(gameBoard),
                    notes = sudokuParser.notesToString(notes),
                    timer = java.time.Duration.ofSeconds(duration.inWholeSeconds),
                    lastPlayed = ZonedDateTime.now(),
                )
            )
        }
    }

    fun processInput(cell: Cell, remainingUse: Boolean, longTap: Boolean = false): Boolean {
        if (gamePlaying) {
            currCell = if (currCell.row == cell.row && currCell.column == cell.column
                && digitFirstNumber == 0) {
                Cell(-1, -1)
            } else {
                cell
            }
            Log.d("processInput", "currCell: $currCell")

            if (currCell.row > -1 && currCell.column > -1
                && !gameBoard[currCell.row][currCell.column].locked) {

                if (inputMethod.value == 1 && digitFirstNumber > 0) {
                    if (!longTap) {
                        if (remainingUsesList.size >= digitFirstNumber
                            && remainingUsesList[digitFirstNumber - 1] > 0 || !remainingUse) {
                            processNumberInput(digitFirstNumber)
//                            undoRedoManager.
                            if (notesToggled) {
                                currCell = Cell(currCell.row, currCell.column,
                                    digitFirstNumber)
                            }
                        }
                    }
                    else if (!currCell.locked) {
                        gameBoard = setValueCell(0)
                        // setNote
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

    private fun getBoardNoRef(): List<List<Cell>> =
        gameBoard.map { items -> items.map { item -> item.copy() } }

    private fun setValueCell(
        value: Int,
        row : Int = currCell.row,
        col : Int = currCell.column
    ): List<List<Cell>> {
        var new = getBoardNoRef()

        new[row][col].value = value
        remainingUsesList = countRemainingUses(new)

        if (currCell.row == row && currCell.column == col) {
            currCell = currCell.copy(value = new[row][col].value)
        }

        if (value == 0) {
            new[row][col].error = false
            currCell.error = false
            return new
        }

        return new
    }

    private fun countRemainingUses(board: List<List<Cell>>): MutableList<Int> {
        val uses = mutableListOf<Int>()

        for (i in 0..size) {
            uses.add(size - sudokuUtils.countNumberInBoard(board, i + 1))
        }
        return uses
    }

    private fun processNumberInput(number: Int) {
        if (currCell.row > -1 && currCell.column > -1 && gamePlaying && !currCell.locked) {
            if (!notesToggled) {
                gameBoard = setValueCell(
                    if (gameBoard[currCell.row][currCell.column].value == number) 0 else number
                )
            } else {
                gameBoard = setValueCell(0)
                remainingUsesList = countRemainingUses(gameBoard)
            }
        }
    }
}

fun Duration.toFormattedString(): String {
    return this.toComponents { hours, minutes, seconds, _ ->
        if (hours > 0) String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else String.format("%02d:%02d", minutes, seconds)
    }
}