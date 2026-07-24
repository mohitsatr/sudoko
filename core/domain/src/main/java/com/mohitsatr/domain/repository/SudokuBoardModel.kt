package com.mohitsatr.domain.repository

import io.github.ilikeyourhat.kudoku.rating.Difficulty

data class SudokuBoardModel(
    val uid: Long = 0,
    val difficulty: Difficulty,
    val initialBoard: String,
    val solvedBoard: String,
)
