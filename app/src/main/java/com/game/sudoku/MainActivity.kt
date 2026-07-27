package com.game.sudoku

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.game.sudoku.ui.home.HomeScreen
import com.mohitsatr.game.ui.game.GameScreen
import com.mohitsatr.ui.SudokuBoardColors.DarkBlueThemeSudokuColorsImpl
import com.mohitsatr.ui.SudokuBoardColors.DarkGreenThemeSudokuColorsImpl
import com.mohitsatr.ui.SudokuBoardColors.LightRedThemeSudokuColorsImpl
import com.mohitsatr.ui.SudokuBoardColors.LightThemeSudokuColorsImpl
import com.mohitsatr.ui.SudokuBoardColors.LocalBoardColors
import com.mohitsatr.ui.theme.SudokuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            // Update the uiState
//            lifecycleScope.launch {
//                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    combine(
//                        isSystemInDarkTheme(),
//                        viewModel().uiState,
//                    ) { systemDark, uiState ->
//                        ThemeSettings(
//                            darkTheme = uiState.shouldUseDarkTheme(systemDark),
//                            androidTheme = uiState.shouldUseAndroidTheme,
//                            disableDynamicTheming = uiState.shouldDisableDynamicTheming,
//                        )
//                    }
//                        .onEach { themeSettings = it }
//                        .map { it.darkTheme }
//                        .distinctUntilChanged()
//                        .collect { darkTheme ->
//                            trace("niaEdgeToEdge") {
//                                // Turn off the decor fitting system windows, which allows us to handle insets,
//                                // including IME animations, and go edge-to-edge.
//                                // This is the same parameters as the default enableEdgeToEdge call, but we manually
//                                // resolve whether or not to show dark theme using uiState, since it can be different
//                                // than the configuration's dark theme value based on the user preference.
//                                enableEdgeToEdge(
//                                    statusBarStyle = SystemBarStyle.auto(
//                                        lightScrim = android.graphics.Color.TRANSPARENT,
//                                        darkScrim = android.graphics.Color.TRANSPARENT,
//                                    ) { darkTheme },
//                                    navigationBarStyle = SystemBarStyle.auto(
//                                        lightScrim = lightScrim,
//                                        darkScrim = darkScrim,
//                                    ) { darkTheme },
//                                )
//                            }
//                        }
//                }
//            }

            val view = LocalView.current

            val gameTheme = when (1) {
                1 -> DarkBlueThemeSudokuColorsImpl()
                2 -> LightRedThemeSudokuColorsImpl()
                3 -> DarkGreenThemeSudokuColorsImpl()
                else -> LightThemeSudokuColorsImpl()
            }

            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    val controller = WindowCompat.getInsetsController(window, window.decorView)

//                    val isLightBackground = themeIndex.value == 0 || themeIndex.value == 2
//                    controller.isAppearanceLightStatusBars = isLightBackground
//                    controller.isAppearanceLightNavigationBars = isLightBackground
                }
            }

            CompositionLocalProvider(LocalBoardColors provides gameTheme) {
                SudokuTheme {
                    val backStack = remember { mutableStateListOf<SudokuNavKey>(SudokuNavKey.Home) }

                    NavDisplay(
                        backStack = backStack,
                        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                        entryProvider = entryProvider {
                            entry<SudokuNavKey.Home> {
                                HomeScreen(
                                    onNavigateToGame = { uid, playedBefore ->
                                        backStack.add(SudokuNavKey.Game(uid, playedBefore))
                                    }
                                )
                            }
                            entry<SudokuNavKey.Game> { key ->
                                GameScreen(
                                    gameUid = key.gameUid,
                                    playedBefore = key.playedBefore,
                                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
