package com.game.sudoku.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohitsatr.domain.repository.BoardRepository
import com.mohitsatr.domain.repository.SavedGameModel
import com.mohitsatr.domain.repository.SavedGameRepository
import com.mohitsatr.ui.theme.ToggleThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.ilikeyourhat.kudoku.generating.SudokuGenerator
import io.github.ilikeyourhat.kudoku.parsing.toSingleLineString
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import io.github.ilikeyourhat.kudoku.solving.SudokuSolver
import io.github.ilikeyourhat.kudoku.type.Classic9x9
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import kotlin.time.Duration

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val boardRepository: BoardRepository,
    private val savedGameRepository: SavedGameRepository,
    private val toggleThemeUseCase: ToggleThemeUseCase,
    private val sudokuGenerator: SudokuGenerator,
    private val sudokuSolver: SudokuSolver,
) : ViewModel() {

    private val _gameGeneratingState =
        MutableStateFlow<SudokuGeneratingState>(SudokuGeneratingState.Loading)
    val gameGeneratingState = _gameGeneratingState.asStateFlow()

    var isGenerating by mutableStateOf(false)
    var isSolving by mutableStateOf(false)
    var readyToPlay by mutableStateOf(false)

    //    val lastXSavedGames = savedGameRepository.getLast(5)
    val lastXSavedGames: StateFlow<List<SavedGameModel>> = savedGameRepository.getLast(5)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleTheme() = viewModelScope.launch { toggleThemeUseCase() }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startNewGame() {
        viewModelScope.launch(Dispatchers.IO) {
            val newPuzzle = sudokuGenerator.generate(Classic9x9, Difficulty.EASY)
            val newPuzzleSolved = sudokuSolver.solve(newPuzzle)

            val insertedBoardUid = withContext(Dispatchers.IO) {
                savedGameRepository.insert(
                    SavedGameModel(
                        0,
                        initialBoard = newPuzzle.toSingleLineString(),
                        solvedBoard = newPuzzleSolved.toSingleLineString(),
                        difficulty = Difficulty.EASY,
                    )
                )
            }
            _gameGeneratingState.update {
                SudokuGeneratingState.ReadyToPlay(
                    insertedBoardUid = insertedBoardUid,
                    generatedPuzzle = newPuzzle.toSingleLineString(),
                    solvedPuzzle = newPuzzleSolved.toSingleLineString()
                )
            }
        }
    }
}

sealed interface SudokuGeneratingState {

    data object Loading : SudokuGeneratingState

    data class ReadyToPlay(
        val insertedBoardUid: Long = -1L,
        val generatedPuzzle: String,
        val solvedPuzzle: String,
    ) : SudokuGeneratingState

    data object NotReadyToPlay: SudokuGeneratingState
}
