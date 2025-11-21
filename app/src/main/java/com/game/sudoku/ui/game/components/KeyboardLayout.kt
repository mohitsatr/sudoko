package com.game.sudoku.ui.game.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.copy
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
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
fun DefaultKeyboard(
    modifier: Modifier = Modifier,
    numbers: List<Int> = (1..9).toList(),
    itemModifier: Modifier = Modifier,
    remainingUse: List<Int>? = null,
    onClick: (Int) -> Unit,
    size: Int = 9,
    selected: Int = 0,
    keyboardColors: SudokuBoardColors = LocalBoardColors.current,
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .then(modifier),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            numbers.subList(0, 5).forEach { number ->
                val hide = false
                KeyboardItem(
                    modifier = itemModifier
                        .weight(1f)
                        .alpha(if (hide) 0f else 1f),
                    number = number,
                    onClick = {
                        if (!hide) {
                            onClick(number)
                        }
                    },
                    remainingUses = if (remainingUse != null && remainingUse.size >= size) {
                        remainingUse[number - 1]
                    } else {
                        null
                    },
                    selected = selected == number,
                    onSelectedTextColor = keyboardColors.selectedHighlightTextColor,
                    nonSelectedTextColor = keyboardColors.nonSelectedHighlightTextColor,
                    onSelectedBackgroundColor = keyboardColors.selectedHighlightColor,
                    boardBackgroundColor = keyboardColors.boardBackgroundColor,
                    buttonOutlineColor = keyboardColors.thickLineColor
                )
            }
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .then(modifier),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            numbers.subList(5, numbers.size).forEach { number ->
                val hide = false
                KeyboardItem(
                    modifier = itemModifier
                        .weight(1f)
                        .alpha(if (hide) 0f else 1f),
                    number = number,
                    onClick = {
                        if (!hide) {
                            onClick(number)
                        }
                    },
                    remainingUses = if (remainingUse != null && remainingUse.size >= size) {
                        remainingUse[number - 1]
                    } else {
                        null
                    },
                    selected = selected == number,
                    onSelectedTextColor = keyboardColors.selectedHighlightTextColor,
                    nonSelectedTextColor = keyboardColors.nonSelectedHighlightTextColor,
                    onSelectedBackgroundColor = keyboardColors.selectedHighlightColor,
                    boardBackgroundColor = keyboardColors.boardBackgroundColor,
                    buttonOutlineColor = keyboardColors.thickLineColor
                )
            }
            EraserItem(
                modifier = itemModifier
                    .weight(1f),
                number = "X",
                onSelectedTextColor = keyboardColors.selectedHighlightTextColor,
                nonSelectedTextColor = keyboardColors.nonSelectedHighlightTextColor,
                onSelectedBackgroundColor = keyboardColors.selectedHighlightColor,
                boardBackgroundColor = keyboardColors.boardBackgroundColor,
                buttonOutlineColor = keyboardColors.thickLineColor,
                onClick = {},
                selected = false
            )
        }
    }
}

@Composable
fun EraserItem(
    modifier: Modifier,
    number: String,
    onClick: () -> Unit,
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
//                color = Color.DarkGray,
                shape = CircleShape
            )
            .combinedClickable(
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center
    ) {
        Column (
            modifier = Modifier.padding(7.dp)

        ) {
            Text(
                text = number,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
//                color = Color.Black
                color = textColor
            )
        }
    }
}
@Composable
fun KeyboardItem(
    modifier: Modifier,
    number: Int,
    onClick: () -> Unit,
    remainingUses: Int?,
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
//                color = Color.DarkGray,
                shape = CircleShape
            )
            .combinedClickable(
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center
    ) {
        Column (
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = number.toString(16).uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = textColor,
//                    color = Color.Black,
                style = LocalTextStyle.current.copy(
                    lineHeight = 25.sp,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )

            )
            if (remainingUses != null) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = remainingUses.toString(),
                    fontSize = 7.sp,
                    color = textColor,
//                    color = Color.Black,
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
}

@PreviewLightDark
@Composable
fun DefaultKeyboardPreview() {
    SudokuTheme {
        Surface {
            DefaultKeyboard(
                numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
                remainingUse = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
                onClick = {},
            )
        }
    }
}