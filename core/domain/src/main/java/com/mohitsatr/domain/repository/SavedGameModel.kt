package com.mohitsatr.domain.repository

import android.os.Build
import androidx.annotation.RequiresApi
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import java.time.Duration
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class SavedGameModel(
    val uid: Long,
    val initialBoard: String,
    val solvedBoard: String,
    val difficulty: Difficulty,
    val timer: Duration = Duration.ofSeconds(kotlin.time.Duration.ZERO.inWholeSeconds),
    val lastPlayed: ZonedDateTime = ZonedDateTime.now(),
)
