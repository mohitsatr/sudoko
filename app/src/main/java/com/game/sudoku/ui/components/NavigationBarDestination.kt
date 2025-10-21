package com.game.sudoku.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.game.sudoku.R
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

sealed class NavigationBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    data object Home : NavigationBarDestination(
        HomeScreenDestination,
        icon = Icons.Rounded.Home,
        R.string.nav_bar_home
    )
}
