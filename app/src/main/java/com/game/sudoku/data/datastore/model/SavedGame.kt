package com.game.sudoku.data.datastore.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.ZonedDateTime

@Serializable
data class SavedGame(
    @PrimaryKey
    @ColumnInfo(name = "board_uid") val uid: Long,
    val savedBoard: String,
    val timer: Duration,
    val lastPlayed: ZonedDateTime
)
