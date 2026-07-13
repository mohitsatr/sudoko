package com.game.sudoku.ui

data class GameStatusState(
    val status: GameStatus = GameStatus.IDLE
)

enum class GameStatus {
    RUNNING, PAUSE, IDLE
}
