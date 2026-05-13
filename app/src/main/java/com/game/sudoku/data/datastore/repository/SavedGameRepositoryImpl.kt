package com.game.sudoku.data.datastore.repository

import com.game.sudoku.data.datastore.dao.SavedGameDao
import com.game.sudoku.data.datastore.model.SavedGame
import com.game.sudoku.data.datastore.model.SudokuBoardModel
import com.game.sudoku.domain.repository.SavedGameRepository

class SavedGameRepositoryImpl(
    private val savedGameDao: SavedGameDao
): SavedGameRepository {
    override fun get(boardUid: Long): SavedGame? = savedGameDao.get(boardUid)

    override fun insert(savedGame: SavedGame) = savedGameDao.insert(savedGame)

    override fun update(savedGame: SavedGame) = savedGameDao.update(savedGame)

//    override fun getLastPlayable(last: Int): Map<SavedGame, SudokuBoardModel>
}
