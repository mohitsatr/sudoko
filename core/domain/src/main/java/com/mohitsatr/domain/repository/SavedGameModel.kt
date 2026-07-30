package com.mohitsatr.domain.repository

import io.github.ilikeyourhat.kudoku.rating.Difficulty
import java.time.Duration
import java.time.ZonedDateTime

data class SavedGameModel(
    val uid: Long,
    val initialBoard: String,
    val solvedBoard: String,
    val difficulty: Difficulty,
    val timer: Duration,
    val lastPlayed: ZonedDateTime,
)
