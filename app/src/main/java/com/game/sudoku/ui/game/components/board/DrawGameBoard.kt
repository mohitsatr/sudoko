package com.game.sudoku.ui.game.components.board

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.game.sudoku.LocalBoardColors
import com.game.sudoku.domain.GameBoard
import com.game.sudoku.domain.GameBoard.Companion.parseToGameBoard
import com.game.sudoku.ui.components.board.drawPositionLines
import com.game.sudoku.ui.core.Cell
import com.game.sudoku.ui.theme.SudokuBoardColors.SudokuBoardColors
import com.game.sudoku.ui.theme.SudokuTheme
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DrawGameBoard(
    modifier: Modifier = Modifier,
    board: GameBoard,
    size: Int = board.size,
    mainTextSize: TextUnit = when (size) {
        6 -> 32.sp
        9 -> 26.sp
        12 -> 24.sp
        else -> 14.sp
    },
    autoFontSize: Boolean = false,
    selectedCell: Cell,
    onClick: (Cell) -> Unit,
    identicalNumbersHighlight: Boolean = true,
    errorsHighlight: Boolean = true,
    positionLines: Boolean = true,
    enabled: Boolean = true,
    questions: Boolean = false,
    cellsToHighLight: List<Cell>? = null,
    boardColors: SudokuBoardColors = LocalBoardColors.current,
) {
    BoxWithConstraints (modifier = modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .padding(4.dp)
    ) {

        val maxWidth = constraints.maxWidth.toFloat()
        val thickLineWidth = with(LocalDensity.current) { 2.dp.toPx() }
        val thinLineWidth = with(LocalDensity.current) { 1.3.dp.toPx() }

        // single cell size
        val cellSize by remember(size) { mutableFloatStateOf(maxWidth / size.toFloat()) }

        var zoom by remember(enabled) { mutableFloatStateOf(1f) }
        var offset by remember(enabled) { mutableStateOf(Offset.Zero) }

        val boardModifier = Modifier
            .fillMaxSize()
            .pointerInput(key1 = enabled, key2 = board) {
                detectTapGestures(
                    onTap = {
                        val totalOffset = it / zoom + offset
                        val row =
                            floor((totalOffset.y) / cellSize)
                                .toInt()
                                .coerceIn(0, board.size)
                        val column =
                            floor((totalOffset.x) / cellSize)
                                .toInt()
                                .coerceIn(0, board.size)
                        onClick(board.getCell(row, column))
                    }
                )
            }

        val errorColor = boardColors.errorColor
        val thickLineColor = boardColors.thickLineColor
        val thinLineColor = boardColors.thinLineColor
        val foregroundColor = boardColors.boardBackgroundColor
        val altForegroundColor = boardColors.altForegroundColor
        val nonSelectedHighlightColor = boardColors.nonSelectedHighlightColor
        val nonSelectedHighlightTextColor = boardColors.nonSelectedHighlightTextColor
        val selectedHighlightColor = boardColors.selectedHighlightColor
        val selectedHighlightTextColor = boardColors.selectedHighlightTextColor

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
            mutableStateOf(
                Paint().apply {
                    color = nonSelectedHighlightTextColor.toArgb()
                    isAntiAlias = true
                    textSize = fontSizePx
                }
            )
        }

        var selectedNumberPaint by remember(fontSizePx) {
        mutableStateOf(
            Paint().apply {
                color = selectedHighlightTextColor.toArgb()
                isAntiAlias = true
                textSize = fontSizePx
            }
        )
    }
        // errors
        var errorTextPaint by remember {
            mutableStateOf(
                Paint().apply {
                    color = errorColor.toArgb()
                    isAntiAlias = true
                    textSize = fontSizePx
                }
            )
        }
        // locked numbers
        var lockedTextPaint by remember {
            mutableStateOf(
                Paint().apply {
                    color = altForegroundColor.toArgb()
                    isAntiAlias = true
                    textSize = fontSizePx
                }
            )
        }

        Log.d("cellSize", "$cellSize")
        Canvas(modifier = boardModifier) {
            val cornerRadius = CornerRadius(15f, 15f)
            val clickOffset  = Offset(
                x = selectedCell.column * cellSize,
                y = selectedCell.row * cellSize
            )
            if (selectedCell.row >= 0 && selectedCell.column >= 0) {
                // current cell
                drawRoundCell(
                    row = selectedCell.row,
                    col = selectedCell.column,
                    gameSize = size,
                    rect = Rect(
                        offset = clickOffset,
                        size = Size(cellSize, cellSize)
                    ),
                    color = Color.Green,
                    cornerRadius = cornerRadius
                )
                if (positionLines) {
                    drawPositionLines(
                        row = selectedCell.row,
                        col = selectedCell.column,
                        gameSize = size,
                        color = Color.Green,
                        cellSize = cellSize,
                        lineLength = maxWidth,
                        cornerRadius = cornerRadius
                    )
                }
            }

            // non-selected bubble around numbers
            for (i in 0 until size) {
                for (j in 0 until size) {
                    val currentCell = board.getCell(i, j)
                    if (currentCell.locked) {
                        drawRoundCell(
                            row = currentCell.row,
                            col = currentCell.column,
                            gameSize = size,
                            rect = Rect(
                                offset = Offset(
                                    x = currentCell.column * cellSize,
                                    y = currentCell.row * cellSize),
                                size = Size(cellSize, cellSize)
                            ),
                            color = Color.Red
                        )
                    }
                }
            }
            if (identicalNumbersHighlight) {
                for (i in 0 until size) {
                    for (j in 0 until size) {
                        val currentCell = board.getCell(i, j)
                        if (currentCell.value == selectedCell.value && currentCell.value != 0) {
                            drawRoundCell(
                                row = currentCell.row,
                                col = currentCell.column,
                                gameSize = size,
                                rect = Rect(
                                    offset = Offset(
                                        x = currentCell.column * cellSize,
                                        y = currentCell.row * cellSize
                                    ),
                                    size = Size(cellSize, cellSize)
                                ),
                                color = selectedHighlightColor
                            )
                        }
                    }
                }
            }

            Log.d("cellsToHighLight", "$cellsToHighLight")
            cellsToHighLight?.forEach {
                drawRoundCell(
                    row = it.row,
                    col = it.column,
                    gameSize = size,
                    color = nonSelectedHighlightColor,
                    rect = Rect(
                        Offset(
                            x = it.column * cellSize,
                            y = it.row * cellSize
                        ),
                        size = Size(cellSize, cellSize)
                    ),
                    cornerRadius = cornerRadius
                )
            }

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

            drawNumbers(
                size = size,
                board = board,
                highlightErrors = errorsHighlight,
                errorTextPaint = errorTextPaint,
                nonSelectedHighlightPaint = nonSelectedNumberPaint,
                selectedHighlightPaint = selectedNumberPaint,
                questions = questions,
                cellSize = cellSize,
                selectedCell = selectedCell
            )
        }
    }
}


const val fakeGameString = "530070000600195000098000060800060003400803001700020006060000280000419005000080079"
val fakeGameBoard = parseToGameBoard(fakeGameString)

@Preview
@Composable
fun GameBoardPreview() {
    val emptyBoard = GameBoard()
    val cell0 = Cell(0, 0, 1, locked = true)
    val cell1 = Cell(0, 1, 2, locked = true)
    emptyBoard.setValue(cell0)
    emptyBoard.setValue(cell1)
    emptyBoard.setValue(0, 2, 3)
    emptyBoard.setValue(0, 3, 4)
    SudokuTheme {
        DrawGameBoard(
            board = fakeGameBoard,
            size = 9,
            selectedCell = Cell(0, 0, 3),
            onClick = {},
        )
    }
}
