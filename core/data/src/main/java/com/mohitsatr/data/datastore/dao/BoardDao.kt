package com.mohitsatr.data.datastore.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mohitsatr.data.datastore.model.SudokuBoardEntity

@Dao
interface BoardDao {

    @Query("SELECT * FROM boardModel WHERE uid == :gameUid")
    fun get(gameUid: Long): SudokuBoardEntity

    @Insert
    fun insert(sudokuBoard: SudokuBoardEntity): Long

    @Update
    fun update(sudokuBoard: SudokuBoardEntity)
}
