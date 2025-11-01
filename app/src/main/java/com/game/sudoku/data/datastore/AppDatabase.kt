package com.game.sudoku.data.datastore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.game.sudoku.data.datastore.converters.DurationConverter
import com.game.sudoku.data.datastore.converters.GameDifficultyConverter
import com.game.sudoku.data.datastore.converters.GameTypeConverter
import com.game.sudoku.data.datastore.converters.ZonedDateTimeConverter
import com.game.sudoku.data.datastore.dao.BoardDao
import com.game.sudoku.data.datastore.dao.FolderDao
import com.game.sudoku.data.datastore.dao.SavedGameDao
import com.game.sudoku.data.datastore.model.Folder
import com.game.sudoku.data.datastore.model.SavedGame
import com.game.sudoku.data.datastore.model.SudokuBoard
import com.game.sudoku.ui.core.qqwing.GameDifficulty

@Database(
    entities = [SavedGame::class, SudokuBoard::class, Folder::class],
    version = 2 ,
    autoMigrations = []
)
@TypeConverters(
    DurationConverter::class,
    GameDifficultyConverter::class,
    GameTypeConverter::class,
    ZonedDateTimeConverter::class
)
abstract class AppDatabase : RoomDatabase(){
//    abstract fun recordDao(): RecordDao
    abstract fun boardDao(): BoardDao
    abstract fun savedGameDao(): SavedGameDao
    abstract fun folderDao(): FolderDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context : Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "main_database"
                ).fallbackToDestructiveMigration(true)
                    .build()
            }

            return INSTANCE as AppDatabase
        }
    }
}
