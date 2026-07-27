package com.mohitsatr.data.impl

import com.mohitsatr.data.datastore.ThemeSettingsManager
import com.mohitsatr.domain.repository.ThemeManager
import jakarta.inject.Inject

class DefaultThemeManager @Inject constructor(
    private val themeSettingsManager: ThemeSettingsManager
): ThemeManager {

}