package com.game.sudoku.ui.game.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.game.sudoku.LocalBoardColors
import com.game.sudoku.ui.theme.SudokuBoardColors.SudokuBoardColors
import com.game.sudoku.ui.theme.SudokuTheme

//@Composable
//fun KeyboardItem(
//    modifier: Modifier = Modifier,
//    number: Int,
//    onClick: (Int) -> Unit,
//    remainingUses: Int? = null,
//    selected: Boolean -> false
//) {
//
//}

@Composable
fun GameKeyboard(
    numbers: List<Int> = (1..9).toList(),
    remainingUse: List<Int>? = null,
    onClick: (Int) -> Unit,
    size: Int = 9,
    selected: Int = 0,
    keyboardColors: SudokuBoardColors = LocalBoardColors.current,
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (1..5).forEach { number ->
                KeyboardButton(
                    modifier = Modifier
                        .weight(1f),
                    number = number.toString(),
                    onClick = { onClick(number) },
                    remainingUses = if (remainingUse != null && remainingUse.size >= size) {
                        remainingUse[number - 1].toString()
                    } else {
                        "0"
                    },
                    selected = selected == number,
                    onSelectedTextColor = keyboardColors.selectedNumberColor,
                    nonSelectedTextColor = keyboardColors.nonSelectedNumberColor,
                    onSelectedBackgroundColor = keyboardColors.selectedBubbleColor,
                    boardBackgroundColor = keyboardColors.backgroundColor,
                    buttonOutlineColor = keyboardColors.thickLineColor
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (6..9).forEach { number ->
                KeyboardButton(
                    modifier = Modifier
                        .weight(1f),
                    number = number.toString(),
                    onClick = { onClick(number) },
                    remainingUses = if (remainingUse != null && remainingUse.size >= size) {
                        remainingUse[number - 1].toString()
                    } else {
                        "0"
                    },
                    selected = selected == number,
                    onSelectedTextColor = keyboardColors.selectedNumberColor,
                    nonSelectedTextColor = keyboardColors.nonSelectedNumberColor,
                    onSelectedBackgroundColor = keyboardColors.selectedBubbleColor,
                    boardBackgroundColor = keyboardColors.backgroundColor,
                    buttonOutlineColor = keyboardColors.thickLineColor
                )
            }
            KeyboardButton(
                modifier = Modifier
                    .weight(1f),
                number = "X",
                onClick = {},
                remainingUses = "",
                selected = false,
                onSelectedTextColor = keyboardColors.selectedNumberColor,
                nonSelectedTextColor = keyboardColors.nonSelectedNumberColor,
                onSelectedBackgroundColor = keyboardColors.selectedBubbleColor,
                boardBackgroundColor = keyboardColors.backgroundColor,
                buttonOutlineColor = keyboardColors.thickLineColor
            )

        }
    }
}

@Composable
fun KeyboardButton(
    modifier: Modifier,
    number: String,
    onClick: () -> Unit,
    remainingUses: String,
    selected: Boolean,
    onSelectedTextColor: Color,
    nonSelectedTextColor: Color,
    onSelectedBackgroundColor: Color,
    boardBackgroundColor: Color,
    buttonOutlineColor: Color,
) {
    val textColor by animateColorAsState(
        targetValue = if (selected) onSelectedTextColor
        else nonSelectedTextColor
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) onSelectedBackgroundColor
        else boardBackgroundColor
    )
    Box(
        modifier = modifier
            .clip(CircleShape)
            .aspectRatio(1f)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = buttonOutlineColor,
                shape = CircleShape
            )
            .combinedClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = number.uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = textColor,
                style = LocalTextStyle.current.copy(
                    lineHeight = 25.sp,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = remainingUses,
                fontSize = 10.sp,
                color = textColor,
                style = LocalTextStyle.current.copy(
                    lineHeight = 7.sp,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
    }
}

@Preview
@Composable
fun GameKeyboardPreview() {
    SudokuTheme {
        Surface {
            GameKeyboard(
                numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
                remainingUse = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
                onClick = {},
            )
        }
    }
}