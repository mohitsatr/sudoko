package com.game.sudoku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.game.sudoku.core.PreferencesConstants
import com.game.sudoku.data.datastore.AppSettingsManager
import com.game.sudoku.data.datastore.ThemeSettingsManager
import com.game.sudoku.ui.components.NavigationBarComponent
import com.game.sudoku.ui.home.HomeViewModel
import com.game.sudoku.ui.theme.SudokuBoardColors.BoardColors
import com.game.sudoku.ui.theme.SudokuBoardColors.SudokuBoardColorsImpl
import com.game.sudoku.ui.theme.SudokuTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.GameScreenDestination
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

val LocalBoardColors = staticCompositionLocalOf { SudokuBoardColorsImpl() }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
//    @Inject
//    lateinit var settings: AppSettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
//        if (!BuildConfig.DEBUG) {
//            GlobalExceptionHandler.initialize(applicationContext, CrashActivity::class.java)
//        }
        setContent {
            val mainViewModel : MainActivityViewModel = hiltViewModel()
            val homeViewModel : HomeViewModel = hiltViewModel()

            val dynamicColors by mainViewModel.dc.collectAsStateWithLifecycle(isSystemInDarkTheme())
//            val darkTheme by mainViewModel.darkTheme.collectAsStateWithLifecycle(0)
//            val firstLaunch by mainViewModel.firstLaunch.collectAsStateWithLifecycle(false)
//            val amoledBlack = mainViewModel.amoledBlack.collectAsStateWithLifecycle(false)
//            val colorSeed = mainViewModel.colorSeed.collectAsStateWithLifecycle(initialValue = Color.Red)
//            val paletteStyle = mainViewModel.paletteStyle

            SudokuTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
//
                var bottomBarStack by rememberSaveable { mutableStateOf(false) }
//
                LaunchedEffect(navBackStackEntry) {
                    bottomBarStack = when(navBackStackEntry?.destination?.route) {
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
//
                val monetSudokuBoard = PreferencesConstants.DEFAULT_MONET_SUDOKU_BOARD
//
                val boardColors =
                        SudokuBoardColorsImpl(
                            boardBackgroundColor = BoardColors.boardBackgroundColor,
                            notesColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                            altForegroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            errorColor = BoardColors.errorColor,
                            nonSelectedHighlightColor = BoardColors.nonSelectedHighlightColor,
                            nonSelectedHighlightTextColor = BoardColors.nonSelectedHighlightTextColor,
                            selectedHighlightColor = BoardColors.selectedHighlightColor,
                            selectedHighlightTextColor = BoardColors.selectedHighlightTextColor,
                            thickLineColor = BoardColors.thickLineColor,
                            thinLineColor = BoardColors.thinLineColor
                        )
                CompositionLocalProvider(LocalBoardColors provides boardColors) {
                    Scaffold(
                        bottomBar = {
                            NavigationBarComponent(
                                navController = navController,
                                isVisible = bottomBarStack,
                                updateAvailable = false
                            )
                        },
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
    themeSettingsManager: ThemeSettingsManager,
    appSettingsManager: AppSettingsManager
) : ViewModel() {
    val dc = themeSettingsManager.dynamicColors
//    val darkTheme = themeSettingsManager.darkTheme
//    val firstLaunch = appSettingsManager.firstLaunch
//    val monetSudokuBoard = themeSettingsManager.monetSudokuBoard
//    val amoledBlack = themeSettingsManager.amoledBlack
//    val colorSeed = themeSettingsManager.themeColorSeed
//    val paletteStyle = Color.Unspecified
}
