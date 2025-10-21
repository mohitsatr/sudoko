package com.game.sudoku.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.game.sudoku.core.PreferencesConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map

//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.booleanPreferencesKey
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.intPreferencesKey
//import androidx.datastore.preferences.preferencesDataStore
//import com.game.sudoku.core.PreferencesConstants
//import dagger.hilt.android.qualifiers.ApplicationContext
//import jakarta.inject.Inject
//import kotlinx.coroutines.flow.map
//import jakarta.inject.Singleton
//
class AppSettingsManager @Inject constructor(@ApplicationContext context: Context) {
//
    private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val dataStore = context.createDataStore

    private val firstLaunchKey = booleanPreferencesKey("first_launch")

    suspend fun setFirstLaunch(value: Boolean) {
        dataStore.edit { settings ->
            settings[firstLaunchKey] = value
        }
    }

    val firstLaunch = dataStore.data.map { preferences ->
        preferences[firstLaunchKey] ?: true
    }

    // highlight mistakes
    private val highlightMistakesKey = intPreferencesKey("mistakes_highlight")

    private val mistakesLimitKey = booleanPreferencesKey("mistakes_limit")

    private val timerKey = booleanPreferencesKey("timer")

//     count and show remaining uses for numbers
    private val remainingUseKey = booleanPreferencesKey("remaining_use")
//
    private val highlightIdenticalKey = booleanPreferencesKey("highlight_identical")
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

    suspend fun setRemainingUse(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[remainingUseKey] = enabled
        }
    }

    val remainingUse = dataStore.data.map { preferences ->
        preferences[remainingUseKey] ?: true
    }


    val highlightIdentical = dataStore.data.map { preferences ->
        preferences[highlightIdenticalKey] ?: true
    }

    val mistakesLimit = dataStore.data.map { preferences ->
        preferences[mistakesLimitKey] ?: PreferencesConstants.DEFAULT_MISTAKES_LIMIT
    }
}
