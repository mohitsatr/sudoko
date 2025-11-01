package com.game.sudoku.data.datastore.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.game.sudoku.data.datastore.model.SudokuBoard
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardDao {

    @Query("SELECT * FROM board")
    fun getAll(): Flow<List<SudokuBoard>>

    @Query("SELECT * FROM board WHERE difficulty = :difficulty")
    fun getAll(difficulty: Difficulty): Flow<List<SudokuBoard>>

    @Query("SELECT * FROM board WHERE folder_id == :folderId")
    fun getAllInFolder(folderId: Long): Flow<List<SudokuBoard>>

    @Query("SELECT * FROM board WHERE folder_id == :folderUid")
    fun getAllInFolderList(folderUid: Long): Flow<List<SudokuBoard>>

    @Query("SELECT * FROM board WHERE uid == :uid")
    fun get(uid: Long): SudokuBoard

    @Insert
    suspend fun insert(boards: List<SudokuBoard>): List<Long>

    @Insert
    suspend fun insert(board: SudokuBoard): Long

    @Delete
    suspend fun delete(board: SudokuBoard)

    @Delete
    suspend fun delete(boards: List<SudokuBoard>)

    @Update
    suspend fun update(boards: List<SudokuBoard>)

    @Update
    suspend fun update(board: SudokuBoard)
}
