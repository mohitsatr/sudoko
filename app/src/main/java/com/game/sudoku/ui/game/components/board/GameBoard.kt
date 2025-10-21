package com.game.sudoku.ui.game.components.board

//
import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.game.sudoku.LocalBoardColors
import com.game.sudoku.ui.core.Cell
import com.game.sudoku.ui.theme.SudokuBoardColors.BoardColors
import com.game.sudoku.ui.theme.SudokuBoardColors.SudokuBoardColors
import com.game.sudoku.ui.theme.SudokuBoardColors.SudokuBoardColorsImpl
import com.game.sudoku.ui.theme.SudokuTheme
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameBoard(
    modifier: Modifier = Modifier,
    board: List<List<Cell>>,
    size: Int = board.size,
    mainTextSize: TextUnit = when (size) {
        6 -> 32.sp
        9 -> 26.sp
        12 -> 24.sp
        else -> 14.sp
    },
    autoFontSize: Boolean = false,
    notes: Any? = null,
    selectedCell: Cell,
    onClick: (Cell) -> Unit,
    onLongClick: (Cell) -> Unit = { },
    identicalNumbersHighlight: Boolean = true,
    errorsHighlight: Boolean = true,
    positionLines: Boolean = true,
    notesToHighLight: List<String> = emptyList(),
    enabled: Boolean = true,
    questions: Boolean = false,
    renderNotes: Boolean = true,
    zoomable: Boolean = false,
//    boardColors: SudokuBoardColors = LocalBoardColors.current,
    crossHighlight: Boolean = false,
    cellsToHighLight: List<Cell>? = null,
    boardColors: SudokuBoardColors = LocalBoardColors.current,
) {

    BoxWithConstraints ( modifier = modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .padding(4.dp)
    ) {

        val maxWidth = constraints.maxWidth.toFloat()
        val thickLineWidth = with(LocalDensity.current) { 1.3.dp.toPx() }
        val thinLineWidth = with(LocalDensity.current) { 1.3.dp.toPx() }

        // single cell size
        val cellSize by remember(size) { mutableFloatStateOf(maxWidth / size.toFloat()) }
        val boardModifier = Modifier

        val errorColor = boardColors.errorColor
        val thickLineColor = boardColors.thickLineColor
        val thinLineColor = boardColors.thinLineColor
        val foregroundColor = boardColors.foregroundColor
        val altForegroundColor = boardColors.altForegroundColor
        val highlightColor = boardColors.highlightColor

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
        var textPaint by remember(fontSizePx) {
            mutableStateOf(
                Paint().apply {
                    color = errorColor.toArgb()
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


        Canvas(modifier = boardModifier) {
            val cornerRadius = CornerRadius(15f, 15f)

            if (selectedCell.row >= 0 && selectedCell.column >= 0) {
                // current cell
                drawRoundCell(
                    row = selectedCell.row,
                    col = selectedCell.column,
                    gameSize = size,
                    rect = Rect(
                        offset = Offset(
                            x = selectedCell.row * cellSize,
                            y = selectedCell.column * cellSize
                        ),
                        size = Size(cellSize, cellSize)
                    ),
                    color = highlightColor,
                    cornerRadius = cornerRadius
                )
                if (positionLines) {

                }
            }
            if (identicalNumbersHighlight) {

            }

            cellsToHighLight?.forEach {
            }

            drawBoardFrame(
                thickLineColor = thickLineColor,
                thickLineWidth = thickLineWidth,
                maxWidth = maxWidth,
                cornerRadius = CornerRadius(15f, 15f)
            )

            // horizontal lines
            for (i in 1 until size) {
                val isThickLine = 1 % horThick == 0
                drawLine(
                    color = thickLineColor,
                    start = Offset(cellSize * i.toFloat(), 0f),
                    end = Offset(cellSize * i.toFloat(), maxWidth),
                    strokeWidth = if (isThickLine) thickLineWidth else thinLineWidth
                )
            }

            // vertical
            for (i in 1 until size) {
                val isThickLine = 1 % vertThick == 0
                if (maxWidth >= cellSize * i) {
                    drawLine(
                        color = if (isThickLine) thickLineColor else thinLineColor,
                        start = Offset(0f, cellSize * i.toFloat()),
                        end = Offset(maxWidth, cellSize * i.toFloat()),
                        strokeWidth = if (isThickLine) thickLineWidth else thinLineWidth
                    )
                }
            }

            drawNumbers(
                size = size,
                board = board,
                highlightErrors = errorsHighlight,
                errorTextPaint = errorTextPaint,
                lockedTextPaint = lockedTextPaint,
                textPaint = textPaint,
                questions = questions,
                cellSize = cellSize
            )


        }
    }
}

@Preview
@Composable
private fun BoardPreviewLight() {
    SudokuTheme {
        Surface {
//            val sudokuParser = SudokuParser()
            val v1 =
                listOf(
                    Cell(0, 0), Cell(0, 1), Cell(0,2),
                    Cell(1, 0), Cell(1, 1), Cell(1,2),
                    Cell(2, 0), Cell(2, 1), Cell(2,2),
                )

            val board = listOf(v1)

//            val board by remember {
//                mutableStateOf(
//                    sudokuParser.parseBoard(
//                        board = "....1........4.............7...........9........68...............5...............",
//                        gameType = GameType.Default9x9,
//                        emptySeparator = '.'
//                    ).toList()
//                )
//            }
//            val notes = listOf(Note(2, 3, 1), Note(2, 3, 5))
            GameBoard(
                board = board,
                notes = null,
                selectedCell = Cell(-1, -1),
                onClick = { },
                boardColors = SudokuBoardColorsImpl(
                    foregroundColor = BoardColors.foregroundColor,
                    notesColor = BoardColors.notesColor,
                    altForegroundColor = BoardColors.altForegroundColor,
                    errorColor = BoardColors.errorColor,
                    highlightColor = BoardColors.highlightColor,
                    thickLineColor = BoardColors.thickLineColor,
                    thinLineColor = BoardColors.thinLineColor
                )
            )
        }
    }
}
