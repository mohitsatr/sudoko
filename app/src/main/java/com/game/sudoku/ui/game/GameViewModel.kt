package com.game.sudoku.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.game.sudoku.ui.core.Cell
import com.game.sudoku.ui.core.qqwing.GameDifficulty
import com.game.sudoku.ui.data.core.PreferencesConstants
import com.game.sudoku.ui.data.datastore.AppSettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class GameViewModel @Inject constructor(
    private val appSettingsManager: AppSettingsManager,
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {  }
    }
    fun startTimer() {
        TODO("Not yet implemented")
    }

    fun pauseTimer() {
        TODO("Not yet implemented")
    }

    val cages: Any
    val notes: Any
    val solvedBoard: Any
    val gameBoard: Any
    val showSolution: Boolean
    val timeText by mutableStateOf("00:00")
    val timerEnabled = appSettingsManager.timerEnabled

    val mistakesCount by mutableStateOf(0)

    val mistakeLimit = appSettingsManager.mistakesLimit.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        PreferencesConstants.DEFAULT_MISTAKES_LIMIT
    )

    // mistake checking method
    val mistakesMethod = appSettingsManager.highlightMistakes.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        PreferencesConstants.DEFAULT_HIGHLIGHT_MISTAKES
    )
    val gameDifficulty by mutableStateOf(GameDifficulty.Unspecified)
    var giveUpDialog by mutableStateOf(false)
    var showMenu by mutableStateOf(false)

    var restartDialog by mutableStateOf(false)
    var curCell by mutableStateOf(Cell(-1, -1, 0))
    var gamePlaying by mutableStateOf(false)
    var endGame by mutableStateOf(false)
}
