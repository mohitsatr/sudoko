package com.game.sudoku.core.parser

import com.game.sudoku.ui.core.Cell
import io.github.ilikeyourhat.kudoku.type.Classic9x9

class SudokuParser {

    private val radix = 13

    fun parseBoard(
        board: String,
        gameType: Classic9x9,
        locked: Boolean = false,
        emptySeparator: Char? = null
    ): MutableList<MutableList<Cell>> {

        val listBoard = MutableList(gameType.sizeX) { row ->
            MutableList(gameType.sizeY) { col ->
                Cell(row, col, 0)
            }
        }

        for (i in board.indices) {
            val value = if (emptySeparator != null) {
                if (board[i] == emptySeparator) 0 else boardDigitToInt(board[i])
            } else {
                if (board[i] in EMPTY_SEPARATORS) 0 else boardDigitToInt(board[i])
            }

            listBoard[i / gameType.sizeX][i % gameType.sizeX].value = value
            listBoard[i / gameType.sizeX][i % gameType.sizeX].locked = locked
        }

        return listBoard
    }

    private fun boardDigitToInt(char: Char): Int {
        return char.digitToInt(radix)
    }

    fun boardToString(boardList: List<List<Cell>>, emptySeparator: Char = '0'): String {
        var boardString = ""
        boardList.forEach { cells ->
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

    fun stringToBoard(boardString: String, emptySeparator: Char = '0'): List<List<Cell>> {
        val board = mutableListOf<Cell>()
        boardString.forEach {
            board.add(Cell(0, 0, it.digitToInt(radix)))
        }
        return mutableListOf(board)
    }

    fun boardToString(board: IntArray, emptySeparator: Char = '0'): String {
        var boardString = ""
        board.forEach {
            boardString += if (it != 0) it.toString(radix) else emptySeparator
        }
        return boardString
    }

    fun notesToString(notes: List<Any>): String {
        return ""
    }

    companion object {
        val EMPTY_SEPARATORS = listOf('0', '.', '-', '_')
    }
}