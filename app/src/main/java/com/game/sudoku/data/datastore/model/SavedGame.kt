package com.game.sudoku.data.datastore.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.game.sudoku.data.backups.serializers.DurationLongSerializer
import com.game.sudoku.data.backups.serializers.ZonedDateTimerLongSerializer
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.ZonedDateTime

@Serializable
@Entity(
    tableName = "saved_game",
    foreignKeys = [ForeignKey(
        onDelete = CASCADE,
        entity = SudokuBoard::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("board_uid")
    )]
)
data class SavedGame(
    @PrimaryKey
    @ColumnInfo(name = "board_uid") val uid: Long,
    @ColumnInfo(name = "current_board") val currentBoard: String,
    @ColumnInfo(name = "notes") val notes: String,

    @Serializable(with = DurationLongSerializer::class)
    @ColumnInfo(name = "timer") val timer: Duration,
    @ColumnInfo(name = "can_continue") val canContinue: Boolean = true,
    @Serializable(with = ZonedDateTimerLongSerializer::class)
    @ColumnInfo(name = "last_played") val lastPlayed: ZonedDateTime? = null,
)
