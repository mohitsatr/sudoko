package com.game.sudoku


sealed interface SudokuNavKey {

    object Home: SudokuNavKey

    data class Game(val gameUid: Long, val playedBefore: Boolean): SudokuNavKey
}
