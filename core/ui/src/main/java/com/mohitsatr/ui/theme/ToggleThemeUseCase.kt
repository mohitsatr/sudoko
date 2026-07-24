package com.mohitsatr.ui.theme

import com.mohitsatr.data.datastore.ThemeSettingsManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleThemeUseCase @Inject constructor(
    val themeSettingsManager: ThemeSettingsManager,
) {

    suspend operator fun invoke() {
        val currentIndex = themeSettingsManager.themeIndex.first()
        val nextIndex = if (currentIndex + 1 < 4) currentIndex + 1 else 0
        themeSettingsManager.setThemeIndex(nextIndex)
    }
}