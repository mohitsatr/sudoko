package com.game.sudoku.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import javax.inject.Singleton

@Singleton
class AppSettingsManager(context: Context) {

    private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val dataStore = context.createDataStore

    private val firstLaunchKey = booleanPreferencesKey("first_launch")

    suspend fun setFirstLaunch(value: Boolean) {
        dataStore.edit { settings ->
            settings[firstLaunchKey] = value
        }
    }
}