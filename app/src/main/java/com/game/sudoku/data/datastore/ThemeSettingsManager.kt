package com.game.sudoku.data.datastore

import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.game.sudoku.ui.data.core.PreferencesConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map

class ThemeSettingsManager @Inject constructor(@ApplicationContext context : Context){
    private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_theme")
    private val dataStore = context.createDataStore

    private val dynamicColorsKey = booleanPreferencesKey("dynamic_colors")
    private val darkThemeKey = intPreferencesKey("dark_theme")

    val dynamicColors = dataStore.data.map { preferences ->
        preferences[dynamicColorsKey] ?: (SDK_INT >= Build.VERSION_CODES.S)
    }

    suspend fun setDarkTheme(value : Int) {
        dataStore.edit { setting ->
            setting[darkThemeKey] = value
        }
    }

    // 0 - system default, 1 - off, 2 - on
    val darkTheme = dataStore.data.map { preferences ->
        preferences[darkThemeKey] ?: PreferencesConstants.DEFAULT_DARK_THEME
    }

}
