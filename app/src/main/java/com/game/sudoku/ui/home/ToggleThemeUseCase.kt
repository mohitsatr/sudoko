package com.game.sudoku.ui.home

import com.game.sudoku.data.datastore.ThemeSettingsManager
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

class ToggleThemeUseCase @Inject constructor(
    val themeSettingsManager: ThemeSettingsManager,
) {

    suspend operator fun invoke() {
        val currentIndex = themeSettingsManager.themeIndex.first()
        val nextIndex = if (currentIndex + 1 < 3) currentIndex + 1 else 0
        themeSettingsManager.setThemeIndex(nextIndex)
    }
}

