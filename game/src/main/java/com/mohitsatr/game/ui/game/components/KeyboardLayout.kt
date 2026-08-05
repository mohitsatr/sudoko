package com.mohitsatr.game.ui.game.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohitsatr.ui.SudokuBoardColors.LocalBoardColors
import com.mohitsatr.ui.theme.SudokuTheme

@Composable
fun GameKeyboard(
    remainingUse: List<Int>,
    onClick: (Int) -> Unit,
    size: Int = 9,
    selectedKey: Int = -1,
    keySize: Float,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            maxItemsInEachRow = 5
        ) {
            (1..size + 1).forEach { number ->
                val usesLeft = remainingUse.getOrNull(number) ?: 0
                val isVisible = usesLeft > 0

                KeyboardButton(
                    number = if (number <= size) number.toString() else "X",
                    onClick = { onClick(number) },
                    remainingUses = usesLeft,
                    isKeyPressed = selectedKey == number,
                    keySize = keySize
                )
            }
        }
    }
}

@Composable
fun KeyboardButton(
    modifier: Modifier = Modifier,
    number: String,
    onClick: () -> Unit,
    remainingUses: Int,
    isKeyPressed: Boolean,
    keySize: Float,
) {
    val keyboardColors = LocalBoardColors.current

    val textColor by animateColorAsState(
        targetValue = if (isKeyPressed) keyboardColors.selectedNumberColor
        else keyboardColors.nonSelectedNumberColor
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (isKeyPressed) keyboardColors.selectedBubbleColor
        else keyboardColors.backgroundColor
    )
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(keySize.dp)
            .aspectRatio(1f)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = keyboardColors.thickLineColor,
                shape = CircleShape
            )
            .combinedClickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = number.uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = textColor,
                style = LocalTextStyle.current.copy(
                    lineHeight = 25.sp,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )

            if (remainingUses > 0) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = remainingUses.toString(),
                    fontSize = 8.sp,
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
}

@Preview
@Composable
fun GameKeyboardPreview() {
    SudokuTheme {
        Surface {
            GameKeyboard(
                remainingUse = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
                onClick = {},
                keySize = 40f
            )
        }
    }
}