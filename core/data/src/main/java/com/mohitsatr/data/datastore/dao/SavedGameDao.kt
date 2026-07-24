package com.mohitsatr.data.di.datastore.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mohitsatr.data.di.datastore.model.SavedGameEntity
import com.mohitsatr.data.datastore.model.SudokuBoardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedGameDao {

    @Query("SELECT * FROM savedGame WHERE board_uid == :boardUid")
    fun get(boardUid: Long): SavedGameEntity?

    @Insert
    fun insert(savedGame: SavedGameEntity)

    @Update
    fun update(game: SavedGameEntity)

    @Query("""
        SELECT * FROM savedGame 
        JOIN boardModel ON savedGame.board_uid = boardModel.uid 
        ORDER BY lastPlayed DESC 
        LIMIT :last
    """)
    fun getLast(last: Int): Flow<Map<SavedGameEntity, SudokuBoardEntity>>
}
