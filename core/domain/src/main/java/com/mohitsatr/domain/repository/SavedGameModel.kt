package com.mohitsatr.domain.repository

import java.time.Duration
import java.time.ZonedDateTime

data class SavedGameModel(
    val uid: Long,
    val savedBoard: String,
    val timer: Duration,
    val lastPlayed: ZonedDateTime
)
