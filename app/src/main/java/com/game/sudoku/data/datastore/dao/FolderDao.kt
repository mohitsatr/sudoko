package com.game.sudoku.data.datastore.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.game.sudoku.data.datastore.model.Folder
import com.game.sudoku.data.datastore.model.SavedGame
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {

    @Query("SELECT * FROM Folder")
    fun get(): Flow<List<Folder>>

    @Query("SELECT * FROM Folder WHERE uid = :uid")
    fun get(uid: Long): Flow<List<Folder>>

    @Insert
    fun insert(folder: Folder): Long

    @Insert
    fun insert(folders: List<Folder>): List<Long>

    @Update
    fun update(folder: Folder)

    @Delete
    fun delete(folder: Folder)

//    @Query("SELECT * FROM saved_game" +
//
//    " INNER JOIN board ON board.folder_id NOT NULL AND board_uid = board.uid AND can_continue" +
//    " ORDER BY last_played DESC" +
//    " LIMIT :gamesCount"
//    )
//    fun getLastSavedGameAnyFolder(gameCount: Int): Flow<List<SavedGame>>
}
