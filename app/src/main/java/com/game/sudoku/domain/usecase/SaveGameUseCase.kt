package com.game.sudoku.domain.usecase

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.game.sudoku.data.datastore.model.SavedGame
import com.game.sudoku.data.datastore.model.SudokuBoardModel
import com.game.sudoku.domain.GameBoard
import com.game.sudoku.domain.repository.SavedGameRepository
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.time.Duration

class SaveGameUseCase @Inject constructor(
    val savedGameRepository: SavedGameRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(
        savedGame: SavedGame?,
        gameBoard: GameBoard,
        duration: Duration,
        boardEntity: SudokuBoardModel
    ) {
        if (savedGame != null) {
            savedGameRepository.update(
                savedGame.copy(
                    timer = java.time.Duration.ofSeconds(duration.inWholeSeconds),
                    savedBoard = gameBoard.toString(),
                    lastPlayed = ZonedDateTime.now()
                )
            )
            Log.d("saveGame", "Game updated: Game:${savedGame.uid} Board ${boardEntity.uid}")
        }
        else {
            val game = SavedGame(
                uid = boardEntity.uid,
                savedBoard = gameBoard.toString(),
                timer = java.time.Duration.ofSeconds(duration.inWholeSeconds),
                lastPlayed = ZonedDateTime.now(),
            )
            savedGameRepository.insert(game)
            Log.d("saveGame", "Game inserted: Game:${game.uid} Board ${boardEntity.uid}")
        }
    }
}
