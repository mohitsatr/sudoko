package com.game.sudoku.data.datastore.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.game.sudoku.data.datastore.model.SudokuBoardModel

@Dao
interface BoardDao {

    @Query("SELECT * FROM boardModel WHERE uid == :gameUid")
    fun get(gameUid: Long): SudokuBoardModel

    @Insert
    fun insert(sudokuBoard: SudokuBoardModel): Long

    @Update
    fun update(sudokuBoard: SudokuBoardModel)
}
