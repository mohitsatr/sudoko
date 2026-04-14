package com.game.sudoku.domain.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.game.sudoku.data.datastore.dao.SavedGameDao
import com.game.sudoku.data.datastore.model.SavedGame
import com.game.sudoku.data.datastore.model.SudokuBoard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet

class SavedGameRepositoryImpl(
    private val savedGameDao: SavedGameDao
): SavedGameRepository {
    override fun getAll(): Flow<List<SavedGame>> = savedGameDao.getAll()

    override suspend fun get(uid: Long): SavedGame? = savedGameDao.get(uid)

    override fun getWithBoards(): Flow<Map<SavedGame, SudokuBoard>> = savedGameDao.getSavedWithBoards()

    override fun getLast(): Flow<SavedGame?> = savedGameDao.getLast()

    override fun getLastPlayable(limit: Int): Flow<Map<SavedGame, SudokuBoard>> {
        return savedGameDao.getLastPlayable(limit)
//            .onEach { dataMap ->
//                if (dataMap.isEmpty()) {
//                    Log.d("getLastPlayable", "result is empty")
//                }
//                else {
//                    dataMap.forEach { (game, board) ->
//                        Log.d("getLastPlayable", "game: ${game.uid}, board: ${board.uid}")
//                    }
//                }
//            }
    }

    override suspend fun insert(savedGame: SavedGame): Long = savedGameDao.insert(savedGame)

    override suspend fun insert(savedGames: List<SavedGame>) = savedGameDao.insert(savedGames)

    override suspend fun update(savedGame: SavedGame) = savedGameDao.update(savedGame)

    override suspend fun delete(savedGame: SavedGame) = savedGameDao.delete(savedGame)
}
