package com.mohitsatr.data.datastore.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
class FolderEntity(
    @PrimaryKey(autoGenerate = true) val uid: Long,
)
