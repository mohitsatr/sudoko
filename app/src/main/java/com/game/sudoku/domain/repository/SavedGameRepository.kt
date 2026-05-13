package com.game.sudoku.domain.repository

import com.game.sudoku.data.datastore.model.SavedGame
import com.game.sudoku.data.datastore.model.SudokuBoardModel

interface SavedGameRepository {

    fun get(boardUid: Long): SavedGame?

    fun insert(savedGame: SavedGame)

    fun update(savedGame: SavedGame)

//    fun getLastPlayable(last: Int): Map<SavedGame, SudokuBoardModel>
}
