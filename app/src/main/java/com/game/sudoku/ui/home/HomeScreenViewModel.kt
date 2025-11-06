package com.game.sudoku.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.game.sudoku.core.parser.SudokuParser
import com.game.sudoku.data.datastore.AppSettingsManager
import com.game.sudoku.data.datastore.model.SudokuBoard
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val boardRepository: BoardRepository,
) : ViewModel() {

    var insertedBoardUid = -1L

    var isGenerating by mutableStateOf(false)
    var isSolving by mutableStateOf(false)
    var readyToPlay by mutableStateOf(false)

    fun startGame() {
        isSolving = false
        isGenerating = false

        val puzzle = List(9) { row -> List(9) { col -> Cell(row, col, 0) } }
        val solvedPuzzle = List(9) { row -> List(9) { col -> Cell(row, col, 0) } }

        viewModelScope.launch(Dispatchers.Default) {
            isGenerating = true;
            val generator = Sudoku.defaultGenerator()
            val generated = generator.generate(Classic9x9, Difficulty.EASY)
            isGenerating = false

            isSolving = true
            val solver = Sudoku.defaultSolver()
            val solved = solver.solve(generated)
            isSolving = false

            if (solved.isSolved()) {
                for (i in 0 until 9) {
                    for (j in 0 until 9) {
                        puzzle[i][j].value = generated[i, j].value
                        solvedPuzzle[i][j].value = solved[i, j].value
                    }
                }

                Log.d("insertI", insertedBoardUid.toString())
                withContext(Dispatchers.IO) {
                    val sudokuParser = SudokuParser()
                    insertedBoardUid = boardRepository.insert(
                        SudokuBoard(
                            0,
                            initialBoard = sudokuParser.boardToString(puzzle),
                            solvedBoard = sudokuParser.boardToString(solvedPuzzle),
                            difficulty = Difficulty.EASY,
                            type = Classic9x9
                        )
                    )
                }

                readyToPlay = true
                Log.d("rop", readyToPlay.toString())
            }
        }
    }
}
