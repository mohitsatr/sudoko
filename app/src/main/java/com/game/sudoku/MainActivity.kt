package com.game.sudoku

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.game.sudoku.core.PreferencesConstants
import com.game.sudoku.data.datastore.AppSettingsManager
import com.game.sudoku.data.datastore.ThemeSettingsManager
import com.game.sudoku.ui.home.HomeViewModel
import com.game.sudoku.ui.theme.SudokuBoardColors.DarkBlueThemeSudokuColorsImpl
import com.game.sudoku.ui.theme.SudokuBoardColors.LightRedThemeSudokuColorsImpl
import com.game.sudoku.ui.theme.SudokuBoardColors.LightThemeSudokuColorsImpl
import com.game.sudoku.ui.theme.SudokuBoardColors.SudokuColors
import com.game.sudoku.ui.theme.SudokuLightTheme
import com.game.sudoku.ui.theme.SudokuTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.GameScreenDestination
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

val LocalBoardColors = staticCompositionLocalOf<SudokuColors> { LightThemeSudokuColorsImpl() }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val mainViewModel: MainActivityViewModel = hiltViewModel()
            val themeIndex = mainViewModel.themeIndex.collectAsStateWithLifecycle()
            val view = LocalView.current

            val window = (view.context as Activity).window
            val controller = WindowInsetsControllerCompat(window, view)
            controller.isAppearanceLightStatusBars = themeIndex.value != 1
            controller.isAppearanceLightNavigationBars = themeIndex.value != 1

            val gameTheme = when (themeIndex.value) {
                1 -> {
                    DarkBlueThemeSudokuColorsImpl()
                }
                2 -> {
                    LightRedThemeSudokuColorsImpl()
                }
                else -> LightThemeSudokuColorsImpl()
            }

            CompositionLocalProvider(LocalBoardColors provides gameTheme) {
                SudokuTheme {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    var bottomBarStack by rememberSaveable { mutableStateOf(false) }

                    LaunchedEffect(navBackStackEntry) {
                        bottomBarStack = when (navBackStackEntry?.destination?.route) {
                            GameScreenDestination.route -> true
                            else -> false
                        }
                    }

                    LaunchedEffect(true) {
                        if (true) {
                            navController.navigate(
                                route = HomeScreenDestination.route,
                                navOptions = navOptions {
                                    popUpTo(HomeScreenDestination.route) {
                                        inclusive = true
                                    }
                                }
                            )
                        }
                    }

                    Scaffold(
                        bottomBar = {},
                        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
                    ) { paddingValues ->
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            navController = navController,
                            modifier = Modifier.padding(paddingValues)
                        )
                    }
                }
            }
        }
    }
}

@HiltViewModel
class MainActivityViewModel
@Inject constructor(
    val themeSettingsManager: ThemeSettingsManager,
    appSettingsManager: AppSettingsManager,
) : ViewModel() {
    val dc = themeSettingsManager.dynamicColors

    val themeIndex = themeSettingsManager.themeIndex.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

}
