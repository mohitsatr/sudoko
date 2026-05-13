package com.game.sudoku.domain

import com.game.sudoku.ui.core.Cell
import io.github.ilikeyourhat.kudoku.model.Sudoku
import io.github.ilikeyourhat.kudoku.type.Classic9x9

data class GameBoard (
    val x: Int = 9,
    val y: Int = 9,
    val size: Int = 9,
    val board: List<List<Cell>> =
        List(x) { row -> List(y) { col -> Cell(row, col, 0) } }
) {
    fun isLocked(cell: Cell): Boolean {
        return board[cell.row][cell.column].locked
    }

    fun getCell(x: Int, y: Int): Cell {
        return board[x][y]
    }

    fun getValue(x: Int, y: Int): Int {
        return board[x][y].value
    }

    fun asString(emptySeparator: Char = '0'): String {
        var boardString = ""
        board.forEach { cells ->
            cells.forEach { cell ->
                boardString +=
                    if (cell.value != 0) {
                        cell.value.toString()
                    } else {
                        emptySeparator
                    }
            }
        }
        return boardString
    }

    fun setValue(selectedCell: Cell) {
        board[selectedCell.row][selectedCell.column].value = selectedCell.value
    }

    fun setValue(row: Int, col: Int, value: Int) {
        board[row][col].value = value
    }

    private fun setLocked(row: Int, col: Int, value: Boolean) {
        board[row][col].locked= value
    }

    fun countNumber(number: Int) : Int {
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

    fun fill(generated: Sudoku) {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                this.setValue(i, j, generated[i, j].value)
            }
        }
    }

    companion object {
        val EMPTY_SEPARATORS = listOf('0', '.', '-', '_')

        fun parseToGameBoard(
            board: String,
            gameType: Classic9x9 = Classic9x9,
            locked: Boolean = false,
            emptySeparator: Char? = null
        ): GameBoard {

            val result = GameBoard(gameType.sizeX, gameType.sizeY)

            for (i in board.indices) {
                val value = if (emptySeparator != null) {
                    if (board[i] == emptySeparator) 0 else boardDigitToInt(board[i])
                } else {
                    if (board[i] in EMPTY_SEPARATORS) 0 else boardDigitToInt(board[i])
                }

                result.setValue(i / gameType.sizeX, i % gameType.sizeX, value)
                result.setLocked(i / gameType.sizeX, i % gameType.sizeX, locked)
            }
            return result
        }
//    result.board.forEach { row ->
//        row.forEach { cell->
//            cell.locked =
//        }
//    }
//    for (i in 0 ) {
//        for (j in 0 until y) {
//            gameBoard[i][j].locked = initialBoard[i][j].locked
//        }
    }
}

private fun boardDigitToInt(char: Char, radix: Int = 13): Int {
    return char.digitToInt(radix)
}
