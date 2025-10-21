package com.game.sudoku.data.datastore

import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.map

//
//import android.content.Context
//import android.os.Build
//import android.os.Build.VERSION.SDK_INT
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.toArgb
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.booleanPreferencesKey
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.intPreferencesKey
//import androidx.datastore.preferences.preferencesDataStore
//import com.game.sudoku.core.PreferencesConstants
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.flow.map
//import jakarta.inject.Inject
//import jakarta.inject.Singleton
//
@Singleton
class ThemeSettingsManager @Inject constructor(@ApplicationContext context : Context) {
    private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_theme")
    private val dataStore = context.createDataStore
//
    private val dynamicColorsKey = booleanPreferencesKey("dynamic_colors")
//    private val darkThemeKey = intPreferencesKey("dark_theme")
//    private val amoledBlackKey = intPreferencesKey("amoled_black")
//
//
//    val amoledBlack = dataStore.data.map { preferences ->
//        preferences[amoledBlackKey] ?: PreferencesConstants.DEFAULT_AMOLED_BLACK
//    }
//
//    // seed color for the custom dynamic color scheme
//    private val themeSeedColorKey = intPreferencesKey("theme_seed_color")
//
//    // palette style for the custom dynamic color scheme
//    private val paletteStyleKey = intPreferencesKey("palette_style")
////
////    val themePaletteStyle = dataStore.data.map { prefs ->
////        val index = prefs[paletteStyleKey] ?: 0
////        if (index in paletteStyles.indices) {
////            paletteStyles[index].first
////        } else {
////            paletteStyles.first().first
////        }
////    }
//
//    val themeColorSeed = dataStore.data.map { preferences ->
//        Color(preferences[themeSeedColorKey] ?: Color.Green.toArgb())
//    }
//
//    // colorful sudoku board with dynamic theme colors
//    private val monetSudokuBoardKey = booleanPreferencesKey("monet_sudoku_board")
//
    val dynamicColors = dataStore.data.map { preferences ->
        preferences[dynamicColorsKey] ?: (SDK_INT >= Build.VERSION_CODES.S)
    }
//
//    suspend fun setDarkTheme(value : Int) {
//        dataStore.edit { setting ->
//            setting[darkThemeKey] = value
//        }
//    }
////
////    suspend fun setAmoledBlack(value : Int) {
////        dataStore.edit { setting ->
////            setting[amoledBlackKey] = value
////        }
////    }
//
//    // 0 - system default, 1 - off, 2 - on
//    val darkTheme = dataStore.data.map { preferences ->
//        preferences[darkThemeKey] ?: PreferencesConstants.DEFAULT_DARK_THEME
//    }
//
//    suspend fun setMonetSudokuBoard(enabled: Boolean) {
//        dataStore.edit { settings ->
//            settings[monetSudokuBoardKey] = enabled
//        }
//    }
//
//    val monetSudokuBoard = dataStore.data.map { preferences ->
//        preferences[monetSudokuBoardKey] ?: PreferencesConstants.DEFAULT_MONET_SUDOKU_BOARD
//    }
////
////    companion object {
////        val paletteStyles = listOf(
////            PaletteStyle.TonalSpot to 0,
////            PaletteStyle.Neutral to 1,
////            PaletteStyle.Vibrant to 2,
////            PaletteStyle.Expressive to 3,
////            PaletteStyle.Rainbow to 4,
////            PaletteStyle.FruitSalad to 5,
////            PaletteStyle.Monochrome to 6,
////            PaletteStyle.Fidelity to 7,
////            PaletteStyle.Content to 8,
////        )
////
////        fun getPaletteStyle(index: Int) =
////            if (index in paletteStyles.indices) paletteStyles[index].first else paletteStyles[0].first
////
////        fun getPaletteIndex(paletteStyle: PaletteStyle, default: Int = 0) =
////            paletteStyles.find { it.first == paletteStyle }?.second
////                ?: default
////    }
}
