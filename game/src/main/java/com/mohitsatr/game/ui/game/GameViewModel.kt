package com.mohitsatr.game.ui.game

//import com.mohitsatr.data.datastore.AppSettingsManager
import android.annotation.SuppressLint
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
import com.mohitsatr.domain.GameBoard
import com.mohitsatr.domain.GameBoard.Companion.parseToGameBoard
import com.mohitsatr.domain.repository.SavedGameModel
import com.mohitsatr.domain.usecase.GetSavedGameUseCase
import com.mohitsatr.domain.usecase.SaveGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.ilikeyourhat.kudoku.model.Cell
import io.github.ilikeyourhat.kudoku.parsing.EmptyCellIndicator
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    savedStateHandle: SavedStateHandle,
    private val saveGameUseCase: SaveGameUseCase,
    private val getSavedGameUseCase: GetSavedGameUseCase,
) : ViewModel() {
    init {
        val navArgs: GameScreenNavArgs? = savedStateHandle.get<GameScreenNavArgs>("args")
        if (navArgs != null) {
            loadGame(navArgs.gameUid, navArgs.newGame)
        }
    }

    private val _gameBoardUiState = MutableStateFlow(GameBoardState())
    val boardState: StateFlow<GameBoardState> = _gameBoardUiState.asStateFlow()

    private val _gamePlayUiState = MutableStateFlow<GamePlayUiState>(GamePlayUiState.Paused)
    val gamePlayState: StateFlow<GamePlayUiState> = _gamePlayUiState.asStateFlow()

    fun loadGame(gameUid: Long, newGame: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedGame = getSavedGameUseCase(gameUid)
            if (savedGame != null) {
                val board = parseToGameBoard(savedGame.initialBoard)
                gameDifficulty = savedGame.difficulty
                solvedBoard = parseToGameBoard(savedGame.solvedBoard)
                savedGameModel = savedGame

                withContext(Dispatchers.Main) {
                    if (!newGame) {
                        restoreSavedGame(savedGame)
                        Log.d(TAG, "Restoring Game -> $savedGameModel")
                    } else {
                        _gameBoardUiState.update {
                            it.copy(
                                displayBoard = board,
                                duration = savedGameModel.timer.toKotlinDuration(),
                            )
                        }
                    }
                }
            }
            saveGame(gameUid)
        }
    }

    private lateinit var savedGameModel: SavedGameModel
    private lateinit var timer: Timer
    private lateinit var solvedBoard: GameBoard

    var size by mutableIntStateOf(9)
    var gameDifficulty by mutableStateOf(Difficulty.EASY)
    var showSolution by mutableStateOf(false)
    var endGame by mutableStateOf(false)

    fun startGame() {
        if (_gamePlayUiState.value !is GamePlayUiState.Running) {
            _gameBoardUiState.update { it.copy(selectedCell = GameBoard.nullCell) }
            _gamePlayUiState.value = GamePlayUiState.Running
            val updateRate = 50L
            timer = fixedRateTimer(initialDelay = updateRate, period = updateRate) {
                val curDuration = _gameBoardUiState.value.duration
                _gameBoardUiState.update {
                    it.copy(
                        duration = curDuration.plus((updateRate * 1e6).toDuration(DurationUnit.NANOSECONDS))
                    )
                }
//                if (prevTime.toInt(DurationUnit.SECONDS) != _gameBoardUiState.value.duration.toInt(DurationUnit.SECONDS)) {
//
//                    timeText = _gameBoardUiState.value.duration.toFormattedString()

//                    if (gameBoard.any { it.any { cell -> cell.value != 0}}) {
//                        viewModelScope.launch(Dispatchers.IO) {
//                            saveGame()
//                            Log.d("StartTimer", "savedGame()")
//                        }
//                    }
            }
        }
    }

    fun pauseGame() {
        _gamePlayUiState.value = GamePlayUiState.Paused

        _gameBoardUiState.update {
            it.copy(selectedCell = GameBoard.nullCell)
        }
        timer.cancel()
    }

    private fun restoreSavedGame(savedGameModel: SavedGameModel) {
        _gameBoardUiState.update {
            it.copy(
                duration = savedGameModel.timer.toKotlinDuration(),
                displayBoard = parseToGameBoard(savedGameModel.initialBoard)
            )
        }

        Log.d(TAG, "restoreSavedGame(SavedGameModel) inGameBoard has been updated")
    }

    suspend fun saveGame(uid: Long) {
        val savedGame = getSavedGameUseCase(uid)
        Log.d("GameViewModel", "saveGame(uid) $savedGame")
        saveGameUseCase(
            uid = uid,
            inGameBoard = _gameBoardUiState.value.displayBoard,
            gameDifficulty = gameDifficulty,
            savedGameModel = savedGame,
            solvedBoard = solvedBoard,
            duration = _gameBoardUiState.value.duration,
        )
    }

    fun processInput(cell: Cell): Boolean {
        val curKeyIndex = _gameBoardUiState.value.selectedKey - 1
        val countLeft = _gameBoardUiState.value.remainingKeyUseCount[curKeyIndex]

        if (curKeyIndex == _gameBoardUiState.value.gameSize && !cell.isLocked()) {
            cell.clear()
            _gameBoardUiState.update {
                it.copy(selectedCell = cell)
            }
        }
        else if (countLeft > 0 && !cell.isLocked()) {
            cell.set(_gameBoardUiState.value.selectedKey)
            _gameBoardUiState.update {
                it.copy(selectedCell = cell)
            }
        }
        return true
    }

    fun processKeyboardInput(key: Int) {
        _gameBoardUiState.update { it.copy(selectedKey = key) }
    }

    fun restartGame() {
//        _gameBoardUiState.update {
//            it.copy(inGameBoard = initialBoard)
//        }
    }

    fun finishGame() {
//        giveUpDialog = true
        _gameBoardUiState.update {
            it.copy(displayBoard = solvedBoard)
        }
        _gamePlayUiState.value = GamePlayUiState.GiveUp
    }

    fun Cell.isLocked(): Boolean = _gameBoardUiState.value.displayBoard.lockedCells.contains(this)

    companion object {
        private const val TAG = "GameViewModel"
    }
}


@SuppressLint("DefaultLocale")
fun Duration.toFormattedString(): String {
    return this.toComponents { hours, minutes, seconds, _ ->
        if (hours > 0) String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else String.format("%02d:%02d", minutes, seconds)
    }
}

data class GameBoardState(
    val gameSize: Int = 9,
    val selectedCell: Cell = GameBoard.nullCell,
    val selectedKey: Int = -1,
    val displayBoard: GameBoard = GameBoard(9, 9, List(gameSize * gameSize) { -1 }),
    val duration: Duration = Duration.ZERO,
) {
    val timeText: String get() = duration.toFormattedString()

    val getAllCells: List<Cell> get() = displayBoard.allCells

    val remainingKeyUseCount: List<Int>
        get() = (0..gameSize).map { gameSize - displayBoard.countNumber(it + 1) }
}

sealed interface GamePlayUiState {

    data object Paused : GamePlayUiState

    data object Running : GamePlayUiState

    data object GiveUp : GamePlayUiState

    data object EndGame : GamePlayUiState
}
