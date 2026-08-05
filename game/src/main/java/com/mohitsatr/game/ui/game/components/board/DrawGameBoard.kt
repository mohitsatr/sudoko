package com.mohitsatr.game.ui.game.components.board

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohitsatr.domain.GameBoard
import com.mohitsatr.domain.GameBoard.Companion.parseToGameBoard
import com.mohitsatr.ui.SudokuBoardColors.LightThemeSudokuColorsImpl
import com.mohitsatr.ui.SudokuBoardColors.LocalBoardColors
import com.mohitsatr.ui.SudokuBoardColors.SudokuColors
import com.mohitsatr.ui.theme.SudokuTheme
import io.github.ilikeyourhat.kudoku.model.Cell
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DrawGameBoard(
    board: GameBoard,
    size: Int = 9,
    mainTextSize: TextUnit = when (size) {
        6 -> 32.sp
        9 -> 26.sp
        12 -> 24.sp
        else -> 14.sp
    },
    autoFontSize: Boolean = false,
    selectedCell: Cell,
    onClick: (Cell) -> Unit,
    enabled: Boolean = true,
    boardColors: SudokuColors = LocalBoardColors.current,
    maxWidth: Float,
) {

//    val boardSizePx = minOf(constraints.maxWidth, constraints.maxHeight).toFloat()

    val thickLineWidth = with(LocalDensity.current) { 3.dp.toPx() }
    val thinLineWidth = with(LocalDensity.current) { 1.5.dp.toPx() }

    // single cell size - 99 / 9 = 11 - 11 * 11 sq^2
    val cellSize by remember(size) { mutableFloatStateOf(maxWidth / size.toFloat()) }

    var zoom by remember(enabled) { mutableFloatStateOf(1f) }
    var offset by remember(enabled) { mutableStateOf(Offset.Zero) }

    val boardModifier = Modifier
        .fillMaxSize()
        .pointerInput(key1 = enabled, key2 = board) {
            detectTapGestures(
                onTap = {
                    val totalOffset = it / zoom + offset
                    val row = floor((totalOffset.y) / cellSize).toInt()
                        .coerceIn(0, size)
                    val column = floor((totalOffset.x) / cellSize).toInt()
                        .coerceIn(0, size)
                    onClick(board.getCell(row, column))
                }
            )
        }

    val thickLineColor = boardColors.thickLineColor
    val thinLineColor = boardColors.thinLineColor

    val nonSelectedCellBackgroundColor = boardColors.nonSelectedBubbleColor
    val nonSelectedCellNumberColor = boardColors.nonSelectedNumberColor

    val selectedCellBackgroundColor = boardColors.selectedBubbleColor
    val selectedCellNumberColor = boardColors.selectedNumberColor

    val vertThick by remember(size) { mutableIntStateOf(floor(sqrt(size.toFloat())).toInt()) }
    val horThick by remember(size) { mutableIntStateOf(ceil(sqrt(size.toFloat())).toInt()) }
    var fontSizePx by remember { mutableFloatStateOf(1f) }

    with(LocalDensity.current) {
        LaunchedEffect(autoFontSize, size, mainTextSize) {
            fontSizePx = if (autoFontSize) {
                (cellSize * 0.9f).toSp().toPx()
            } else {
                mainTextSize.toPx()
            }
        }
    }

    // numbers
    var nonSelectedNumberPaint by remember(fontSizePx) {
        mutableStateOf(Paint().apply {
            color = nonSelectedCellNumberColor.toArgb()
            isAntiAlias = true
            textSize = fontSizePx
        })
    }

    var selectedNumberPaint by remember(fontSizePx) {
        mutableStateOf(Paint().apply {
            color = selectedCellNumberColor.toArgb()
            isAntiAlias = true
            textSize = fontSizePx
        })
    }

    var nonSelectedCellBackgroundPaint by remember(fontSizePx) {
        mutableStateOf(Paint().apply {
            color = nonSelectedCellBackgroundColor.toArgb()
            isAntiAlias = true
            textSize = fontSizePx
        })
    }

    var selectedCellBackgroundPaint by remember(fontSizePx) {
        mutableStateOf(Paint().apply {
            color = selectedCellBackgroundColor.toArgb()
            isAntiAlias = true
            textSize = fontSizePx
        })
    }

    Log.d("cellSize", "$cellSize")
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = boardModifier) {
        val cornerRadius = CornerRadius(15f, 15f)
        val clickOffset = Offset(
            x = selectedCell.y * cellSize,
            y = selectedCell.x * cellSize
        )

//            cellsToHighLight?.forEach {
//                drawRoundCell(
//                    row = it.row,
//                    col = it.column,
//                    gameSize = size,
//                    color = nonSelectedCellBackgroundColor,
//                    rect = Rect(
//                        Offset(
//                            x = it.column * cellSize,
//                            y = it.row * cellSize
//                        ),
//                        size = Size(cellSize, cellSize)
//                    ),
//                    cornerRadius = cornerRadius
//                )
//            }

        // horizontal lines
        for (i in 1 until size) {
            drawLine(
                color = if (i % 3 == 0) thickLineColor else thinLineColor,
                start = Offset(cellSize * i.toFloat(), 0f),
                end = Offset(cellSize * i.toFloat(), maxWidth),
                strokeWidth = if (i % 3 == 0) thickLineWidth else thinLineWidth
            )
        }

        // vertical
        for (i in 1 until size) {
            if (maxWidth >= cellSize * i) {
                drawLine(
                    color = if (i % 3 == 0) thickLineColor else thinLineColor,
                    start = Offset(0f, cellSize * i.toFloat()),
                    end = Offset(maxWidth, cellSize * i.toFloat()),
                    strokeWidth = if (i % 3 == 0) thickLineWidth else thinLineWidth
                )
            }
        }

        for (row in 0 until size) {
            for (col in 0 until size) {
                val cell = board.getCell(row, col)
                if (cell.value != 0) {
                    val isSelected =
                        cell.x == selectedCell.x && cell.y == selectedCell.y

                    val textStyle = TextStyle(
                        color = if (isSelected) selectedCellNumberColor else nonSelectedCellNumberColor,
                        fontSize = mainTextSize
                    )
                    val cellCenter = Offset(
                        x = col * cellSize + cellSize / 2f,
                        y = row * cellSize + cellSize / 2f
                    )

                    val textLayoutResult = textMeasurer.measure(
                        text = cell.value.toString(),
                        style = textStyle
                    )

                    val textX = col * cellSize + (cellSize - textLayoutResult.size.width) / 2f
                    val textY = row * cellSize + (cellSize - textLayoutResult.size.height) / 2f

                    drawCircle(
                        color = if (isSelected) selectedCellBackgroundColor else nonSelectedCellBackgroundColor,
                        radius = (cellSize / 2) * 0.80f,
                        center = cellCenter
                    )
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(textX, textY)
                    )
                }
            }
        }
    }

}


fun DrawScope.SingleCell(
    textMeasurer: TextMeasurer,
    cellSize: Float,
    currentCell: Cell,
    offset: Offset,
    color: Color,
    text: String,
    textColor: Paint,
    isSelectedCell: Boolean,
) {
//    if (isSelectedCell) {
//        drawCircle(
//            color = color,
//            radius = 17.dp.toPx(),
//            center = offset,
//        )
//    }
    drawText(
        textMeasurer = textMeasurer,
        currentCell.value.toString(),
        topLeft = offset,
        style = TextStyle(fontSize = 30.sp)
    )
    drawIntoCanvas { canvas ->
        if (currentCell.value != 0) {
            val textToDraw = currentCell.value.toString().uppercase()
            val textBounds = Rect()
//            textColor.getTextBounds(textToDraw, 0, 1, textBounds)
            val textWidth = textColor.measureText(textToDraw)

            val x = currentCell.y * cellSize + (cellSize - textWidth) / 2f
            val y = currentCell.x * cellSize + (cellSize + textBounds.height()) / 2f

            canvas.nativeCanvas.drawText(textToDraw, x, y, textColor)
        }
    }
}

@Preview
@Composable
fun SingleCellPreview() {

    val fakeGameString =
        "530070000600195000098000060800060003400803001700020006060000280000419005000080079"
    val fakeGameBoard = parseToGameBoard(fakeGameString)
    val fakeBoardColors = LightThemeSudokuColorsImpl()
    SudokuTheme {
        var nonSelectedNumberPaint by remember(20f) {
            mutableStateOf(Paint().apply {
                color = fakeBoardColors.nonSelectedNumberColor.toArgb()
                isAntiAlias = true
                textSize = 20f
            })
        }
        var selectedNumberPaint by remember(110) {
            mutableStateOf(Paint().apply {
                color = fakeBoardColors.selectedNumberColor.toArgb()
                isAntiAlias = true
                textSize = 110f
            })
        }
        val cornerRadius = CornerRadius(15f, 15f)
        val textMeasurer = rememberTextMeasurer()
        Canvas(modifier = Modifier.size(80.dp)) {
            SingleCell(
                textMeasurer = textMeasurer,
                cellSize = 110f,
                currentCell = Cell(0, 0, 1),
                offset = Offset(110f, 100f),
                color = fakeBoardColors.selectedBubbleColor,
                text = "1",
                isSelectedCell = true,
                textColor = selectedNumberPaint,
            )
        }
    }
}

@Preview
@Composable
fun GameBoardPreview() {
    val fakeGameString =
        "530070000600195000098000060800060003400803001700020006060000280000419005000080079"
    val fakeGameBoard = parseToGameBoard(fakeGameString)
    val fakeBoardColors = LightThemeSudokuColorsImpl()
    SudokuTheme {
        DrawGameBoard(
            board = fakeGameBoard,
            size = 9,
            selectedCell = Cell(4, 0, 8),
            onClick = {},
            boardColors = fakeBoardColors,
            maxWidth = 200f,
        )
    }
}
