package com.game.sudoku.ui.game

data class GameScreenNavArgs(
    val gameUid: Long,
    val playedBefore: Boolean = false
)
