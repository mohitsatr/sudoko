package com.game.sudoku.ui.core

// needs to be serialized
data class Cell(
    val row: Int,
    val column: Int,
    val value: Int = 0,
    val error: Boolean = false,
    val locked: Boolean = false
)
