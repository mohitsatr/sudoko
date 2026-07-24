package com.game.sudoku

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.mohitsatr.ui.SudokuBoardColors.DarkBlueThemeSudokuColorsImpl
import com.mohitsatr.ui.SudokuBoardColors.DarkGreenThemeSudokuColorsImpl
import com.mohitsatr.ui.SudokuBoardColors.LightRedThemeSudokuColorsImpl
import com.mohitsatr.ui.SudokuBoardColors.LightThemeSudokuColorsImpl
import com.mohitsatr.ui.SudokuBoardColors.LocalBoardColors
import com.mohitsatr.ui.SudokuBoardColors.SudokuColors
import com.mohitsatr.ui.theme.SudokuTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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

//            val mainViewModel: MainActivityViewModel = hiltViewModel()
//            val themeIndex = mainViewModel.themeIndex.collectAsStateWithLifecycle()
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
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    var bottomBarStack by rememberSaveable { mutableStateOf(false) }

//                    LaunchedEffect(navBackStackEntry) {
//                        bottomBarStack = when (navBackStackEntry?.destination?.route) {
//                            GameScreenDestination.route -> true
//                            else -> false
//                        }
//                    }

                    LaunchedEffect(true) {
                        if (true) {
                            navController.navigate(
                                route = "",
                                navOptions = navOptions {
//                                    popUpTo() {
//                                        inclusive = true
//                                    }
                                }
                            )
                        }
                    }
//                    DestinationsNavHost(
//                        navGraph = NavGraphs.root,
//                        navController = navController,
//                    )
                }
            }
        }
    }
}

//@HiltViewModel
//class MainActivityViewModel
//@Inject constructor(
//    val themeSettingsManager: ThemeSettingsManager,
//    appSettingsManager: AppSettingsManager,
//) : ViewModel() {
//
//
//    val themeIndex = themeSettingsManager.themeIndex.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = 0
//    )
//
//}

sealed interface HomeScreenUiState {
    data object Loading : HomeScreenUiState
    data class Success(val theme: Int) : HomeScreenUiState

}
