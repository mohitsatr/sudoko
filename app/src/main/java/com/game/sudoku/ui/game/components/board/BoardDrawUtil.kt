package com.game.sudoku.ui.game.components.board

import android.graphics.Paint
import android.util.Log
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.game.sudoku.ui.core.Cell


fun DrawScope.drawRoundCell(
    row: Int,
    col: Int,
    gameSize: Int,
    rect: Rect,
    color: Color,
    cornerRadius: CornerRadius = CornerRadius.Zero
) {
    val path = Path().apply {
        addRoundRect(
            roundRectForCell(
                row = row,
                col = col,
                gameSize = gameSize,
                rect = rect,
                cornerRadius = cornerRadius
            )
        )
    }

    drawPath(
        path = path,
        color = color
    )
}

fun DrawScope.drawBoardFrame(
    thickLineColor: Color,
    thickLineWidth: Float,
    maxWidth: Float,
    cornerRadius: CornerRadius
) {
    drawRoundRect(
        color = thickLineColor,
        topLeft = Offset.Zero,
        size = Size(maxWidth, maxWidth),
        cornerRadius = cornerRadius,
        style = Stroke(width = thickLineWidth)
    )
}

fun roundRectForCell(
    row: Int,
    col: Int,
    gameSize: Int,
    rect: Rect,
    cornerRadius: CornerRadius
): RoundRect {
    val topLeft = if (row == 0 && col == 0) cornerRadius else CornerRadius.Zero
    val topRight = if (row == 0 && col == gameSize - 1) cornerRadius else CornerRadius.Zero
    val bottomLeft = if (row == gameSize - 1 && col == 0) cornerRadius else CornerRadius.Zero
    val bottomRight =
        if (row == gameSize - 1 && col == gameSize - 1) cornerRadius else CornerRadius.Zero

    return RoundRect(
        rect = rect,
        topLeft = topLeft,
        topRight = topRight,
        bottomLeft = bottomLeft,
        bottomRight = bottomRight
    )
}

fun DrawScope.drawNumbers(
    size: Int,
    board: List<List<Cell>>,
    highlightErrors: Boolean,
    errorTextPaint: Paint,
    nonSelectedHighlightPaint: Paint,
    selectedHighlightPaint: Paint,
    questions: Boolean,
    cellSize: Float,
    selectedCell: Cell
) {
    drawIntoCanvas { canvas ->
        for (i in 0 until size) {
            for (j in 0 until size) {
                val isSelected = board[i][j].value == selectedCell.value && selectedCell.value != 0

                if (board[i][j].value != 0) {
                    val paint = when {
                        isSelected -> selectedHighlightPaint
                        else -> nonSelectedHighlightPaint
                    }
                    val textToDraw =
                        if (questions) "?" else board[i][j].value.toString(16).uppercase()

                    Log.d("texttodraw", textToDraw)

                    val textBounds = android.graphics.Rect()
                    selectedHighlightPaint.getTextBounds(textToDraw, 0, 1, textBounds)
                    val textWidth = paint.measureText(textToDraw)

                    val x = board[i][j].column * cellSize + (cellSize - textWidth) / 2f
                    val y = board[i][j].row * cellSize + (cellSize + textBounds.height()) / 2f

                    Log.d("Text", "width=$textWidth, x=$x, y=$y")
                    Log.d("Attribute", "cellSize=$cellSize, textBound=$textBounds")

                    canvas.nativeCanvas.drawText(
                        textToDraw,
                        x,
                        y,
                        paint
                    )
                }
            }
        }
    }
}
// cellSize =
// 9 *
