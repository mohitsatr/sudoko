package com.game.sudoku.domain.repository

import com.game.sudoku.data.datastore.model.SavedGame
import com.game.sudoku.data.datastore.model.SudokuBoard
import kotlinx.coroutines.flow.Flow

interface SavedGameRepository {

    fun get(boardUid: Long): SavedGame

    fun insert(savedGame: SavedGame)

    fun update(savedGame: SavedGame)
}
