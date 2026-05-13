package com.game.sudoku.data.datastore.repository

import com.game.sudoku.data.datastore.dao.BoardDao
import com.game.sudoku.data.datastore.model.SudokuBoardModel
import com.game.sudoku.domain.repository.BoardRepository

class BoardRepositoryImpl(
    private val boardDao: BoardDao
) : BoardRepository {
    override fun get(gameUid: Long): SudokuBoardModel = boardDao.get(gameUid)

    override fun insert(sudokuBoard: SudokuBoardModel): Long = boardDao.insert(sudokuBoard)

    override fun update(sudokuBoard: SudokuBoardModel) = boardDao.update(sudokuBoard)
}
