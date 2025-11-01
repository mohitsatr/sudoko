package com.game.sudoku.domain.repository

import com.game.sudoku.data.datastore.model.SudokuBoard
import com.game.sudoku.ui.core.qqwing.GameDifficulty

import io.github.ilikeyourhat.kudoku.model.Sudoku
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import kotlinx.coroutines.flow.Flow

interface BoardRepository {
    fun getAll(): Flow<List<SudokuBoard>>
    fun getAll(difficulty: Difficulty): Flow<List<SudokuBoard>>

    suspend fun get(uid: Long): SudokuBoard

    suspend fun insert(boards: List<SudokuBoard>): List<Long>
    suspend fun insert(board: SudokuBoard): Long

    suspend fun delete(board: SudokuBoard)
    suspend fun delete(boards: List<SudokuBoard>)

    suspend fun update(board: SudokuBoard)
    suspend fun update(boards: List<SudokuBoard>)
}
