package com.game.sudoku.ui.theme

import androidx.annotation.FloatRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

object ColorUtils {

    fun Color.blend(
        color: Color,
        @FloatRange(from = 0.0, to = 1.0) fraction: Float = 0.2f
    ): Color = ColorUtils.blendARGB(this.toArgb(), color.toArgb(), fraction).toColor()

    fun Color.darken(
        @FloatRange(from = 0.0, to = 1.0) fraction: Float = 0.2f
    ): Color = blend(color = Color.Black, fraction = fraction)

    fun Color.lighten(
        @FloatRange(from = 0.0, to = 1.0) fraction: Float = 0.2f
    ): Color = blend(color = Color.White, fraction = fraction)

    fun Int.toColor(): Color = Color(color = this)

    @Composable
    fun Color.harmonizeWithPrimary(
        @FloatRange(
            from = 0.0,
            to = 1.0
        ) fraction: Float = 0.2f
    ): Color = blend(MaterialTheme.colorScheme.primary, fraction)

}

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

//val onSurface = Color()
val primary = Color(0xFFE6E6E6) // non-selected bubble
val onPrimary = Color(0xFFA1A1A1) // numbers on non-selected and keypad
val primaryContainer = Color(0xFFBAB6A8) // selected bubble
val OnPrimaryContainer = Color(0xFFF9F9F8) // white text when numbers are filled

//val secondary = Color()
//val onSecondary = Color()

//val teritary = Color()
//val onTertiary = Color()

val outline = Color(0xFFD8D8D8) // outline around keypad and lines making up cells

val surface = Color(0xFFF9F9F9) // background
//val onSurface = Color()


