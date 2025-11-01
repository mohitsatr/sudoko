package com.game.sudoku.core.sudokuUtils

import com.game.sudoku.ui.core.Cell

class SudokuUtils {

    fun countNumberInBoard(board: List<List<Cell>>, number: Int) : Int {
        var count = 0
        board.forEach { cells ->
            cells.forEach { cell ->
                if (cell.value == number) {
                    count++
                }
            }
        }
        return count
    }
}
