package com.game.sudoku.domain

import com.game.sudoku.domain.GameBoard.Companion.EMPTY_SEPARATORS
import com.game.sudoku.ui.core.Cell
import io.github.ilikeyourhat.kudoku.type.Classic9x9

data class GameBoard (
    val x: Int = 9,
    val y: Int = 9,
    val size: Int = 9,
    val board: List<List<Cell>> = List(x) { row ->
        List(y) { col -> Cell(row, col, 0) }
    },
) {
    companion object {
        val EMPTY_SEPARATORS = listOf('0', '.', '-', '_')
    }
}

fun GameBoard.isLocked(cell: Cell): Boolean {
    return board[cell.row][cell.column].locked
}

fun parseToGameBoard(
    board: String,
    gameType: Classic9x9 = Classic9x9,
    locked: Boolean = false,
    emptySeparator: Char? = null
): GameBoard {

    val result = com.game.sudoku.domain.toString(gameType.sizeX, gameType.sizeY)

    for (i in board.indices) {
        val value = if (emptySeparator != null) {
            if (board[i] == emptySeparator) 0 else boardDigitToInt(board[i])
        } else {
            if (board[i] in EMPTY_SEPARATORS) 0 else boardDigitToInt(board[i])
        }

        result.setValue(i / gameType.sizeX, i % gameType.sizeX, value)
        result.setLocked(i / gameType.sizeX, i % gameType.sizeX, locked)
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
    return result
}



private fun boardDigitToInt(char: Char, radix: Int = 13): Int {
    return char.digitToInt(radix)
}

fun GameBoard.toString(emptySeparator: Char = '0'): String {
    var boardString = ""
    this.board.forEach { cells ->
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

fun GameBoard.setValue(selectedCell: Cell) {
    board[selectedCell.row][selectedCell.column].value = selectedCell.value
}

fun GameBoard.setValue(row: Int, col: Int, value: Int) {
    board[row][col].value = value
}

private fun GameBoard.setLocked(row: Int, col: Int, value: Boolean) {
    board[row][col].locked= value
}



fun GameBoard.countNumber(number: Int) : Int {
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
