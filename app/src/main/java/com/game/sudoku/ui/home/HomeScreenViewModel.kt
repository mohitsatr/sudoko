package com.game.sudoku.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.game.sudoku.GameGeneratingState
import com.game.sudoku.GenerationStatus
import com.game.sudoku.data.datastore.model.SudokuBoardModel
import com.game.sudoku.domain.GameBoard
import com.game.sudoku.domain.repository.BoardRepository
import com.game.sudoku.domain.repository.SavedGameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.ilikeyourhat.kudoku.generating.defaultGenerator
import io.github.ilikeyourhat.kudoku.model.Sudoku
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import io.github.ilikeyourhat.kudoku.solving.defaultSolver
import io.github.ilikeyourhat.kudoku.type.Classic9x9
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val boardRepository: BoardRepository,
    private val savedGameRepository: SavedGameRepository,
    private val toggleThemeUseCase: ToggleThemeUseCase,
) : ViewModel() {

    private val _gameGeneratingState = MutableStateFlow(GameGeneratingState())
    val gameGeneratingStateFlow = _gameGeneratingState.asStateFlow()

//    val lastGames = savedGameRepository.getLastPlayable(5)
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.Eagerly,
//            initialValue =
//        )

    fun toggleTheme() = viewModelScope.launch { toggleThemeUseCase() }

    fun startGame() {
        val initialPuzzle = GameBoard()
        val solvedPuzzle = GameBoard()

        viewModelScope.launch(Dispatchers.IO) {
            _gameGeneratingState.update { it.copy(generationStatus = GenerationStatus.GENERATING) }
            val generator = Sudoku.defaultGenerator()
            val generated = generator.generate(Classic9x9, Difficulty.EASY)

            _gameGeneratingState.update { it.copy(generationStatus = GenerationStatus.SOLVING) }
            val solver = Sudoku.defaultSolver()
            val solved = solver.solve(generated)

            if (solved.isSolved()) {
                initialPuzzle.fill(generated)
                solvedPuzzle.fill(solved)

                val uid = withContext(Dispatchers.IO) {
                    boardRepository.insert(
                        SudokuBoardModel(
                            0,
                            initialBoard = initialPuzzle.asString(),
                            solvedBoard = solvedPuzzle.asString(),
                            difficulty = Difficulty.EASY
                        )
                    )
                }
                _gameGeneratingState.update {
                    it.copy(insertedBoardUid = uid, generationStatus = GenerationStatus.READY)
                }
            }
        }
    }

    fun onNavigationBackHandled() {
        _gameGeneratingState.update { GameGeneratingState() }
    }
}
