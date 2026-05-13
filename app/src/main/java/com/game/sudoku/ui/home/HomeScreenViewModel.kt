package com.game.sudoku.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.game.sudoku.core.parser.SudokuParser
import com.game.sudoku.data.datastore.model.SavedGame
import com.game.sudoku.data.datastore.model.SudokuBoardModel
import com.game.sudoku.domain.GameBoard
import com.game.sudoku.domain.repository.BoardRepository
import com.game.sudoku.domain.repository.SavedGameRepository
import com.game.sudoku.ui.core.Cell
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.ilikeyourhat.kudoku.generating.defaultGenerator
import io.github.ilikeyourhat.kudoku.model.Sudoku
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import io.github.ilikeyourhat.kudoku.solving.defaultSolver
import io.github.ilikeyourhat.kudoku.type.Classic9x9
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.emptyMap

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val boardRepository: BoardRepository,
    private val savedGameRepository: SavedGameRepository,
) : ViewModel() {

    var insertedBoardUid = -1L

    var isGenerating by mutableStateOf(false)
    var isSolving by mutableStateOf(false)
    var readyToPlay by mutableStateOf(false)

//    val lastGames = savedGameRepository.getLastPlayable(5)
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.Eagerly,
//            initialValue =
//        )

    fun startGame() {
        isSolving = false
        isGenerating = false

        val initialPuzzle = GameBoard()
        val solvedPuzzle = GameBoard()

        viewModelScope.launch(Dispatchers.IO) {
            isGenerating = true
            val generator = Sudoku.defaultGenerator()
            val generated = generator.generate(Classic9x9, Difficulty.EASY)
            isGenerating = false

            isSolving = true
            val solver = Sudoku.defaultSolver()
            val solved = solver.solve(generated)
            isSolving = false

            if (solved.isSolved()) {
                initialPuzzle.fill(generated)
                solvedPuzzle.fill(solved)


                withContext(Dispatchers.IO) {
                    insertedBoardUid = boardRepository.insert(
                        SudokuBoardModel(
                            0,
                            initialBoard = initialPuzzle.asString(),
                            solvedBoard = solvedPuzzle.asString(),
                            difficulty = Difficulty.EASY,
                        )
                    )
                    Log.d("startGame", "$insertedBoardUid got inserted")
                }
                readyToPlay = true
            }
        }
    }
}
