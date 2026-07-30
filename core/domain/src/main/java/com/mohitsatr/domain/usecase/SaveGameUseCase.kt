package com.mohitsatr.domain.usecase

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.mohitsatr.domain.GameBoard
import com.mohitsatr.domain.repository.SavedGameModel
import com.mohitsatr.domain.repository.SavedGameRepository
import io.github.ilikeyourhat.kudoku.rating.Difficulty
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.time.Duration

const val TAG = "SaveGameUseCase"

class SaveGameUseCase @Inject constructor(
    val savedGameRepository: SavedGameRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(
        savedGameModel: SavedGameModel? = null,
        uid: Long,
        inGameBoard: GameBoard,
        solvedBoard: GameBoard,
        duration: Duration,
        gameDifficulty: Difficulty
    ) {
        if (savedGameModel != null) {
            savedGameRepository.update(
                savedGameModel.copy(
                    initialBoard = inGameBoard.asString(),
                    timer = java.time.Duration.ofSeconds(duration.inWholeSeconds),
                    lastPlayed = ZonedDateTime.now()
                )
            )
            Log.d(TAG, "Game with ID: $uid Updated")
        }
        else {
            val curGame = SavedGameModel(
                uid = uid,
                timer = java.time.Duration.ofSeconds(duration.inWholeSeconds),
                lastPlayed = ZonedDateTime.now(),
                initialBoard = inGameBoard.asString(),
                solvedBoard = solvedBoard.asString(),
                difficulty = gameDifficulty,
            )
            savedGameRepository.insert(curGame)
            Log.d(TAG, "New Game with $uid Saved")
        }
    }
}
