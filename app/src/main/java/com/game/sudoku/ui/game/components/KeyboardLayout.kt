package com.game.sudoku.ui.game.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    numbers : List<Int> = (1..9).toList(),
    itemModifier: Modifier = Modifier,
    remainingUse: List<Int>? = null,
    onClick: (Int) -> Unit,
    size: Int = 9,
    selected: Int = 0
) {
    Column (
        modifier = Modifier.fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Row (
            modifier = Modifier.fillMaxWidth()
                .then(modifier),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            numbers.forEach { number ->
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
                    selected = selected == number
                )
            }
        }
    }
}

@Composable
fun KeyboardItem(
    modifier: Modifier,
    number: Int,
    onClick: () -> Unit,
    remainingUses: Int?,
    selected: Boolean
) {
    val color by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        else Color.Transparent
    )
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color)
            .combinedClickable(
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center
    ) {
        Column (
            modifier = Modifier.padding(7.dp)

        ) {
            Text(
                text = number.toString(16).uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp

            )
            if (remainingUses != null) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = remainingUses.toString(),
                    fontSize = 11.sp,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun defaultKeyboardPreview() {
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