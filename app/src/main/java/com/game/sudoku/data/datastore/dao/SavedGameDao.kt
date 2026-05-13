package com.game.sudoku.data.datastore.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.game.sudoku.data.datastore.model.SavedGame

@Dao
interface SavedGameDao {

    @Query("SELECT * FROM savedGame WHERE board_uid == :boardUid")
    fun get(boardUid: Long): SavedGame?

    @Insert
    fun insert(savedGame: SavedGame)

    @Update
    fun update(game: SavedGame)
}
