package com.game.sudoku.data.datastore.repository

import android.util.Log
import com.game.sudoku.data.datastore.dao.BoardDao
import com.game.sudoku.data.datastore.model.SudokuBoard
import com.game.sudoku.domain.repository.BoardRepository
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import kotlinx.coroutines.flow.Flow

class BoardRepositoryImpl(
    private val boardDao: BoardDao
) : BoardRepository {

}
