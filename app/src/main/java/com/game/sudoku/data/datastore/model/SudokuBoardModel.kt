package com.game.sudoku.data.datastore.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "boardModel"
)
data class SudokuBoardModel(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    val difficulty: Difficulty,
    val initialBoard: String,
    val solvedBoard: String
)
