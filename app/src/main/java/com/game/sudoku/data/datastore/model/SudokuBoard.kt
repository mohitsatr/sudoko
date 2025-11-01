package com.game.sudoku.data.datastore.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.github.ilikeyourhat.kudoku.model.SudokuType
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "board",
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = Folder::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("folder_id")
        )
    ]
)
data class SudokuBoard(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "initial_board") val initialBoard: String,
    @ColumnInfo("solved_board") val solvedBoard: String,
    @ColumnInfo("difficulty") val difficulty: Difficulty,
    @ColumnInfo("type") val type: SudokuType,
    @ColumnInfo(name = "folder_id", defaultValue = "null") val folderId: Long? = null,
    @ColumnInfo(name = "killer_cages", defaultValue = "null") val killerCages: String? = null
)
