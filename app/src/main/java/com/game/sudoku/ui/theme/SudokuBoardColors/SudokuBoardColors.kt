package com.game.sudoku.ui.theme.SudokuBoardColors

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.game.sudoku.ui.theme.ColorUtils.blend
import com.game.sudoku.ui.theme.ColorUtils.harmonizeWithPrimary

object BoardColors {
    inline val thickLineColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.primaryContainer.copy()

    inline val thinLineColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.outline.copy()

    inline val nonSelectedHighlightColor: Color
    @Composable
    get() = MaterialTheme.colorScheme.primary.copy()

    inline val nonSelectedHighlightTextColor: Color
    @Composable
    get() = MaterialTheme.colorScheme.onPrimary.copy()

    inline val selectedHighlightColor: Color
    @Composable
    get() = MaterialTheme.colorScheme.primaryContainer.copy()

    inline val selectedHighlightTextColor: Color
    @Composable
    get() = MaterialTheme.colorScheme.onPrimaryContainer.copy()

    inline val errorColor: Color
        @Composable
        get() = Color(230, 67, 83).harmonizeWithPrimary()

    inline val boardBackgroundColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.surface

    inline val notesColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.onSurfaceVariant.blend(
            MaterialTheme.colorScheme.secondary,
            0.4f
        )
    inline val altForegroundColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.onSurfaceVariant.blend(
            MaterialTheme.colorScheme.secondary,
            0.5f
        ).copy(alpha = 0.85f)
}
interface SudokuBoardColors {
    val backgroundColor: Color
    val altForegroundColor: Color
    val thickLineColor : Color
    val thinLineColor: Color
    val errorColor: Color
    val nonSelectedBubbleColor: Color
    val selectedBubbleColor: Color
    val emptyBubbleColor: Color

    val nonSelectedNumberColor : Color
    val selectedNumberColor: Color


    val nonSelectedKeywordBackgroundColor: Color
    val nonSelectedKeywordNumberColor: Color

    val selectedKeywordBackgroundColor: Color
    val selectedKeywordNumberColor: Color

    val notesColor: Color
}

class LightThemeSudokuBoardColorsImpl (
    override val backgroundColor: Color = Color(0xFFF9F9F9),
    override val thickLineColor: Color = Color(0xFFA89D79),
    override val thinLineColor: Color = Color(0xFFDFDFDF),

    override val altForegroundColor: Color = Color.White,
    override val errorColor: Color = Color.White,

    override val nonSelectedBubbleColor: Color = Color(0xFFE6E6E6),
    override val selectedBubbleColor: Color = Color(0xFFBAB6A8),
    override val emptyBubbleColor: Color = Color(0xFFA89D77),

    override val notesColor: Color = Color.White,
    override val nonSelectedNumberColor: Color = Color(0xFFA1A1A1),
    override val selectedNumberColor: Color = backgroundColor,

    override val nonSelectedKeywordBackgroundColor: Color = backgroundColor,
    override val nonSelectedKeywordNumberColor: Color = nonSelectedNumberColor,
    override val selectedKeywordBackgroundColor: Color = thickLineColor,
    override val selectedKeywordNumberColor: Color = backgroundColor
) : SudokuBoardColors

//class SudokuBoardColorsTheme1Impl (
//    override val foregroundColor: Color = Color(0xFFF9F9F9),
//    override val thickLineColor: Color = Color(0xFFA79D79),
//    override val thinLineColor: Color = Color(0xFFE6E6E6),
//    override val altForegroundColor: Color = Color.White,
//    override val errorColor: Color = Color.White,
//    override val highlightColor: Color = Color(0xFFBAB6a8),
//    override val notesColor: Color = Color.White
//) : SudokuBoardColors

/* Array */
//["a89d77","f9f9f9","bab6a8","e1dacb","aaa387","e5e5e5"]