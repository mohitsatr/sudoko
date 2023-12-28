package com.game.sudoku.kudoku

import androidx.compose.animation.scaleOut
import io.github.ilikeyourhat.kudoku.model.Sudoku
import io.github.ilikeyourhat.kudoku.parsing.EmptyCellIndicator
import io.github.ilikeyourhat.kudoku.parsing.fromSingleLineString
import io.github.ilikeyourhat.kudoku.parsing.toSingleLineString
import io.github.ilikeyourhat.kudoku.solving.defaultSolver
import io.github.ilikeyourhat.kudoku.type.Classic9x9
import org.junit.Test


fun main(args: Array<String>) {
    solve9x9_ReturnsTrue()
}

fun solve9x9_ReturnsTrue() {
    val puzzle = "057760004804095200003007581000742019761000840029180070140903700500600493930074008";

    val board = Sudoku.fromSingleLineString(puzzle)
    val solutionByKudoku = Sudoku.defaultSolver().solve(board)

    println(solutionByKudoku.board)
}
