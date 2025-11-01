package com.game.sudoku.kudoku

import com.game.sudoku.R

enum class GameType(
    val size: Int,
    val sectionHeight: Int,
    val sectionWidth: Int,
    val resName: Int
) {
    Default9x9(9, 3, 3, R.string.default_9x9),
    Default6x6(6, 2, 3, R.string.type_default_6x6),
}
