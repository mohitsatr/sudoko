package com.game.sudoku.data.datastore.converters

import androidx.room.TypeConverter
import io.github.ilikeyourhat.kudoku.model.SudokuType
import io.github.ilikeyourhat.kudoku.type.Classic6x6
import io.github.ilikeyourhat.kudoku.type.Classic9x9

class GameTypeConverter {

    @TypeConverter
    fun fromGameType(gameType: SudokuType): Int {
        return when (gameType) {
            Classic9x9 -> 1
            Classic6x6 -> 2
            else -> 1
        }
    }

    @TypeConverter
    fun toGameType(gameType: Int): SudokuType {
        return when (gameType) {
            1 -> Classic9x9
            2 -> Classic6x6
            else -> Classic9x9
        }
    }
}
