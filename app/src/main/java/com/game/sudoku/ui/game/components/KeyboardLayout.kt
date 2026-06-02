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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.game.sudoku.LocalBoardColors
import com.game.sudoku.ui.theme.SudokuTheme

@Composable
fun GameKeyboard(
    remainingUse: List<Int>? = null,
    onClick: (Int) -> Unit,
    size: Int = 9,
    selected: Int = -1,
    keySize: Float,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            (1..5).forEach { number ->
                val usesLeft = remainingUse?.getOrNull(number) ?: 0
                val isVisible = usesLeft > 0

                Box(modifier = Modifier
                    .alpha(1f)
                ) {
                    KeyboardButton(
                        number = number.toString(),
                        onClick = { onClick(number) },
                        remainingUses = usesLeft.toString(),
                        isKeyPressed = selected == number,
                        keySize = keySize
                    )
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            (6..9).forEach { number ->
                val usesLeft = remainingUse?.getOrNull(number) ?: 0
                val isVisible = usesLeft > 0
                Box(
                    modifier = Modifier) {
                    KeyboardButton(
                        number = number.toString(),
                        onClick = { onClick(number) },
                        remainingUses = usesLeft.toString(),
                        isKeyPressed = selected == number,
                        keySize = keySize
                    )
                }
            }

            KeyboardButton(
                number = "X",
                onClick = {},
                remainingUses = "",
                isKeyPressed = selected == 0,
                keySize = keySize
            )
        }
    }
}

@Composable
fun KeyboardButton(
    modifier: Modifier = Modifier,
    number: String,
    onClick: () -> Unit,
    remainingUses: String,
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

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = remainingUses,
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