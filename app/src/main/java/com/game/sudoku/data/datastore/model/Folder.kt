package com.game.sudoku.data.datastore.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
@Entity
class Folder(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "nameCreated") val createdAt: ZonedDateTime
)
