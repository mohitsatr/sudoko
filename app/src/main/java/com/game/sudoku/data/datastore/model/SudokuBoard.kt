package com.game.sudoku.data.datastore.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.game.sudoku.domain.GameBoard
import io.github.ilikeyourhat.kudoku.model.SudokuType
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import kotlinx.serialization.Serializable

@Serializable
data class SudokuBoard(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    val difficulty: Difficulty,
    val initialBoard: String
)
