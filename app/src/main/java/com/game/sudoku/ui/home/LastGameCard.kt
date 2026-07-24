package com.game.sudoku.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.mohitsatr.domain.GameBoard
import com.mohitsatr.ui.SudokuBoardColors.LocalBoardColors
import io.github.ilikeyourhat.kudoku.model.Cell

@Composable
fun LastGameCard(
    lastPlayed: String,
    duration: String,
    savedBoard: String,
    onClick: () -> Unit,
) {
//    Row {
//        DrawGameBoard(
//            board = GameBoard(),
//            maxWidth = 100f,
//            selectedCell = Cell(-1, -1, -1),
//            onClick = {},
//            enabled = true,
//            cellSize = 20f,
//        )
//        Text(
//            text = "Last Played: $lastPlayed for $duration",
//            color = LocalBoardColors.current.thinLineColor
//        )
//    }
}
