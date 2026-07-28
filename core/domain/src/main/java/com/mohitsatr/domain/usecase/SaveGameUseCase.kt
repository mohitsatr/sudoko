package com.mohitsatr.domain.usecase

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.mohitsatr.domain.GameBoard
import com.mohitsatr.domain.repository.SavedGameModel
import com.mohitsatr.domain.repository.SavedGameRepository
import com.mohitsatr.domain.repository.SudokuBoardModel
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.time.Duration

const val TAG = "SaveGameUseCase"

class SaveGameUseCase @Inject constructor(
    val savedGameRepository: SavedGameRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(
        savedGameModel: SavedGameModel?,
        gameBoard: GameBoard,
        duration: Duration,
        sudokuBoardModel: SudokuBoardModel
    ) {
        if (savedGameModel != null) {
            savedGameRepository.update(
                savedGameModel.copy(
                    timer = java.time.Duration.ofSeconds(duration.inWholeSeconds),
                    savedBoard = gameBoard.asString(),
                    lastPlayed = ZonedDateTime.now()
                )
            )
            Log.d(TAG, "Game updated: Game:${savedGameModel.uid} Board ${sudokuBoardModel.uid}")
        }
        else {
            val game = SavedGameModel(
                uid = sudokuBoardModel.uid,
                savedBoard = gameBoard.asString(),
                timer = java.time.Duration.ofSeconds(duration.inWholeSeconds),
                lastPlayed = ZonedDateTime.now(),
            )
            savedGameRepository.insert(game)
            Log.d(TAG, "Game inserted: Game:${game.uid} Board ${sudokuBoardModel.uid}")
        }
    }
}
