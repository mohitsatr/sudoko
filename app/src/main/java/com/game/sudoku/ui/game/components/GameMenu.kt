package com.game.sudoku.ui.game.components

import androidx.compose.runtime.Composable

@Composable
fun GameMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onGiveUpClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onExportClicked: () -> Unit
) {}
