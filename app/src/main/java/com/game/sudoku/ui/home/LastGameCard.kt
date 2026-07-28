package com.game.sudoku.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mohitsatr.ui.SudokuBoardColors.LocalBoardColors

@Composable
fun LastGameCard(
    lastPlayed: String,
    duration: String,
    savedBoard: String,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Text(
            text = "Last Played: $lastPlayed for $duration",
            color = LocalBoardColors.current.thinLineColor
        )
    }
}
