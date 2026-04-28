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
data class SavedGame(
    @PrimaryKey
    @ColumnInfo(name = "board_uid") val uid: Long,
)
