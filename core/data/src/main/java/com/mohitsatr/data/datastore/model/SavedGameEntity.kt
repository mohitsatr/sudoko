package com.mohitsatr.data.di.datastore.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.ZonedDateTime

@Serializable
@Entity(
    tableName = "savedGame"
)
data class SavedGameEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "board_uid") val uid: Long,
    val initialBoard: String,
    val solvedBoard: String,
    val difficulty: Difficulty,
    val timer: Duration,
    val lastPlayed: ZonedDateTime,
)
