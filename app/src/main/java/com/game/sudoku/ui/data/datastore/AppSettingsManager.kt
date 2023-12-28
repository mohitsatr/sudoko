package com.game.sudoku.ui.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.game.sudoku.ui.data.core.PreferencesConstants
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class AppSettingsManager(context: Context) {
    private val Context.createDataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "settings")
    private val dataStore = context.createDataStore

    // highlight mistakes
    private val highlightMistakesKey = intPreferencesKey("mistakes_highlight")

    private val mistakesLimitKey = booleanPreferencesKey("mistakes_limit")

    private val timerKey = booleanPreferencesKey("timer")

    suspend fun setHighlightMistakes(value: Int) {
        dataStore.edit { settings ->
            settings[highlightMistakesKey] = value
        }
    }

    val highlightMistakes = dataStore.data.map { preferences ->
        preferences[highlightMistakesKey] ?: PreferencesConstants.DEFAULT_HIGHLIGHT_MISTAKES
    }

    val timerEnabled = dataStore.data.map { preferences ->
        preferences[timerKey] ?: PreferencesConstants.DEFAULT_SHOW_TIMER
    }
    suspend fun setMistakeLimit(enabled: Boolean) {
        dataStore.edit { setting ->
            setting[mistakesLimitKey] = enabled
        }
    }

    val mistakesLimit = dataStore.data.map { preferences ->
        preferences[mistakesLimitKey] ?:PreferencesConstants.DEFAULT_MISTAKES_LIMIT
    }
}
