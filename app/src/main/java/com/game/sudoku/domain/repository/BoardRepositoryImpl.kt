package com.game.sudoku.domain.repository

import androidx.room.Query
import com.game.sudoku.data.datastore.dao.BoardDao
import com.game.sudoku.data.datastore.model.SudokuBoard
import io.github.ilikeyourhat.kudoku.model.Sudoku
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import kotlinx.coroutines.flow.Flow
import kotlin.text.insert

class BoardRepositoryImpl(
    private val boardDao: BoardDao
) : BoardRepository {

    override fun getAll(): Flow<List<SudokuBoard>> = boardDao.getAll()
    override fun getAll(difficulty: Difficulty): Flow<List<SudokuBoard>> =
        boardDao.getAll(difficulty)

    override suspend fun get(uid: Long): SudokuBoard = boardDao.get(uid)

    override suspend fun insert(boards: List<SudokuBoard>): List<Long> = boardDao.insert(boards)
    override suspend fun insert(board: SudokuBoard): Long = boardDao.insert(board)

    override suspend fun delete(board: SudokuBoard) = boardDao.delete(board)
    override suspend fun delete(boards: List<SudokuBoard>) = boardDao.delete(boards)

    override suspend fun update(board: SudokuBoard) = boardDao.update(board)
    override suspend fun update(boards: List<SudokuBoard>) = boardDao.update(boards)
}
