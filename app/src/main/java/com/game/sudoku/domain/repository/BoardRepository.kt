package com.game.sudoku.domain.repository

import com.game.sudoku.data.datastore.model.SudokuBoardModel

interface BoardRepository {

    fun get(gameUid: Long): SudokuBoardModel

    fun insert(sudokuBoard: SudokuBoardModel): Long

    fun update(sudokuBoard: SudokuBoardModel)
}
