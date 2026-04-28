package com.game.sudoku.data.datastore.repository

import com.game.sudoku.data.datastore.dao.SavedGameDao
import com.game.sudoku.data.datastore.model.SavedGame
import com.game.sudoku.data.datastore.model.SudokuBoard
import com.game.sudoku.domain.repository.SavedGameRepository
import kotlinx.coroutines.flow.Flow

class SavedGameRepositoryImpl(
    private val savedGameDao: SavedGameDao
): SavedGameRepository {
}
