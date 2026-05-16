package com.game.sudoku.ui.game.components.board

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.tooling.preview.Preview
import com.game.sudoku.domain.GameBoard
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
    board: GameBoard,
    highlightErrors: Boolean,
    nonSelectedHighlightPaint: Paint,
    selectedHighlightPaint: Paint,
    questions: Boolean,
    cellSize: Float,
    selectedCell: Cell
) {
    drawIntoCanvas { canvas ->
        for (i in 0 until size) {
            for (j in 0 until size) {
                val isSelected = board.getValue(i, j) == selectedCell.value && selectedCell.value != 0

                if (board.getValue(i, j) != 0) {
                    val paint = when {
                        isSelected -> selectedHighlightPaint
                        else -> nonSelectedHighlightPaint
                    }
                    val textToDraw =
                        if (questions) "?" else board.getValue(i, j).toString(16).uppercase()

                    val textBounds = android.graphics.Rect()
                    selectedHighlightPaint.getTextBounds(textToDraw, 0, 1, textBounds)
                    val textWidth = paint.measureText(textToDraw)

                    val x = board.getCell(i, j).column * cellSize + (cellSize - textWidth) / 2f
                    val y = board.getCell(i, j).row * cellSize + (cellSize + textBounds.height()) / 2f

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

//@Preview
//@Composable
//fun drawRoundCellPreview() {
//    Canvas(modifier = Modifier.fillMaxSize()) {
//        drawRoundCell(
//            0,0,9,Rect(
//                Offset(0, 0),
//                size = Size(110f, 110f)
//            )
//        )
//    }
//}
