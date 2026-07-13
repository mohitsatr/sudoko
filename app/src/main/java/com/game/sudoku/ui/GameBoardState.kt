package com.game.sudoku.ui

import com.game.sudoku.domain.GameBoard
import com.game.sudoku.ui.core.Cell

data class GameBoardState(
    val gameBoard: GameBoard = GameBoard(),
    val cell: Cell = Cell(-1, -1),
    var remainingUsesList: List<Int> = emptyList()
)
