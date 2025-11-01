package com.game.sudoku.data.datastore.converters

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.game.sudoku.ui.core.qqwing.GameDifficulty
import io.github.ilikeyourhat.kudoku.rating.Difficulty

class GameDifficultyConverter {
    @TypeConverter
    fun fromDifficulty(gameDifficulty: Difficulty) : Int {
        return when (gameDifficulty) {
            Difficulty.EASY -> 1
            Difficulty.MEDIUM -> 2
            Difficulty.HARD -> 3
            else -> 1
        }
    }

    @TypeConverter
    fun toDifficulty(value: Int): Difficulty {
        return when (value) {
            1 -> Difficulty.EASY
            2 -> Difficulty.MEDIUM
            3 -> Difficulty.HARD
            else -> Difficulty.UNSOLVABLE
        }
    }
}
