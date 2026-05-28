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
interface SudokuColors {
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
    val selectedButtonTextColor: Color
    val nonSelectedButtonTextColor: Color
    val selectedButtonBackground: Color
    val nonSelectedButtonBackground: Color

    val notesColor: Color
}

class LightThemeSudokuColorsImpl (
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
    override val selectedNumberColor: Color = Color(0xFFF9F9F9),

    override val nonSelectedKeywordBackgroundColor: Color = Color(0xFFF9F9F9),
    override val nonSelectedKeywordNumberColor: Color = Color(0xFFA1A1A1),
    override val selectedKeywordBackgroundColor: Color = Color(0xFFA89D79),
    override val selectedKeywordNumberColor: Color = Color(0xFFF9F9F9),

    override val selectedButtonBackground: Color = Color(0xFFA89D79),
    override val nonSelectedButtonBackground: Color = Color(0xFFF9F9F9),
    override val selectedButtonTextColor: Color = Color(0xFFF9F9F9),
    override val nonSelectedButtonTextColor: Color = Color(0xFF7b7b7b),
    ) : SudokuColors

class DarkBlueThemeSudokuColorsImpl(
    override val backgroundColor: Color = Color(0xFF151C22),
    override val altForegroundColor: Color = Color.Red,
    override val thickLineColor: Color = Color(0xFF60C3E9),
    override val thinLineColor: Color = Color(0xFF21303A),
    override val errorColor: Color = Color.Red,

    override val nonSelectedBubbleColor: Color = Color(0xFF406B7C),
    override val selectedBubbleColor: Color = Color(0xFF406B7C),

    override val emptyBubbleColor: Color = Color.Red,

    override val nonSelectedNumberColor: Color = Color(0xFF62ADCA),
    override val selectedNumberColor: Color = Color(0xFF151C22),

    override val nonSelectedKeywordBackgroundColor: Color = Color(0xFF151C22),
    override val nonSelectedKeywordNumberColor: Color = Color(0xFFBACEDD),
    override val selectedKeywordBackgroundColor: Color = Color(0xFF42BEF2),
    override val selectedKeywordNumberColor: Color = Color(0xFF151C22),


    override val selectedButtonTextColor: Color = Color(0xFF151C22),
    override val nonSelectedButtonTextColor: Color= Color(0XFFBBCFDE),
    override val selectedButtonBackground: Color = Color(0XFF42BEF2),
    override val nonSelectedButtonBackground: Color = Color(0xFF151C22),
    override val notesColor: Color = Color.Red
) : SudokuColors

class LightRedThemeSudokuColorsImpl(
    override val backgroundColor: Color = Color(0xFFFFFFFF),
    override val altForegroundColor: Color = Color(0xFFB5555E),
    override val thickLineColor: Color = Color(0xFFB5555E),
    override val thinLineColor: Color = Color(0xFFE4E4E4),

    override val errorColor: Color = Color(0xFFE64353),
    override val nonSelectedBubbleColor: Color = Color(0xFFDAB5B8),
    override val selectedBubbleColor: Color = Color(0xFFDAB5B8),
    override val emptyBubbleColor: Color = Color(0xFFB5555E),

    override val nonSelectedNumberColor: Color = Color(0xFFA2686E),
    override val selectedNumberColor: Color = Color(0xFFFFFFFF),

    override val nonSelectedKeywordBackgroundColor: Color = Color(0xFFFFFFFF),
    override val nonSelectedKeywordNumberColor: Color = Color(0xFF727272),
    override val selectedKeywordBackgroundColor: Color = Color(0xFFBB636C),
    override val selectedKeywordNumberColor: Color = Color(0xFFFFFFFF),

    override val selectedButtonTextColor: Color = Color(0xFFFFFFFF),
    override val nonSelectedButtonTextColor: Color = Color(0xFF727272),
    override val selectedButtonBackground: Color = Color(0xFFBB636C),
    override val nonSelectedButtonBackground: Color = Color(0xFFFFFFFF),
    override val notesColor: Color = Color(0xFF727272)
) : SudokuColors
