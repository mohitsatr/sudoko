package com.mohitsatr.data.di.datastore.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mohitsatr.data.di.datastore.model.SavedGameEntity
import com.mohitsatr.data.datastore.model.SudokuBoardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedGameDao {

    @Query("SELECT * FROM savedGame WHERE board_uid == :boardUid")
    suspend fun get(boardUid: Long): SavedGameEntity?

    @Query("""
        SELECT * FROM savedGame
        ORDER BY lastPlayed DESC 
        LIMIT :limit
    """)
    fun get(limit: Int): Flow<List<SavedGameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(savedGame: SavedGameEntity): Long

    @Update
    suspend fun update(game: SavedGameEntity)

    @Query("""
        SELECT * FROM savedGame
        ORDER BY lastPlayed DESC 
        LIMIT :last
    """)
    fun getLast(last: Int): Flow<List<SavedGameEntity>>
}
