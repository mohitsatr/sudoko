package com.mohitsatr.domain

import android.util.Log
import io.github.ilikeyourhat.kudoku.model.Board
import io.github.ilikeyourhat.kudoku.model.Cell
import io.github.ilikeyourhat.kudoku.model.Sudoku
import io.github.ilikeyourhat.kudoku.parsing.EmptyCellIndicator
import io.github.ilikeyourhat.kudoku.parsing.createFromString
import io.github.ilikeyourhat.kudoku.parsing.fromSingleLineString

/**
 * Wrapper around library's Board
 */
data class GameBoard(
    val sizeX: Int = 9,
    val sizeY: Int = 9,
    val cells: List<Int>,
) {

    private val internalBoard: Board = Board(sizeX, sizeY, cells)
//    private val internalCells: List<Cell> = cells.map { it.internalCell }

    fun updateValue(x: Int, y: Int, value: Int) = internalBoard.get(x, y).set(value)

    fun getCell(x: Int, y: Int): Cell = internalBoard.get(x, y)

    fun asString(): String {
        return internalBoard.toString()
    }

    fun countNumber(number: Int) : Int = internalBoard.cells().count { it.value == number }

    companion object {
        private const val TAG = "GameBoard"

        val nullCell = Cell(-1, -1, -1)
        fun parseToGameBoard(
            boardString: String,
        ): GameBoard {

            val sudoku = Sudoku.fromSingleLineString(boardString)
            return GameBoard(sudoku.sizeX(), sudoku.sizeY(), sudoku.values()).also {
                Log.d(TAG, " ${sudoku.values()}")
            }
        }
    }
}
