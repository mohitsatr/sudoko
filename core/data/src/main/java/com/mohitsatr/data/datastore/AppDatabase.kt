package com.mohitsatr.data.datastore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mohitsatr.data.datastore.converters.DurationConverter
import com.mohitsatr.data.datastore.converters.GameDifficultyConverter
import com.mohitsatr.data.datastore.converters.GameTypeConverter
import com.mohitsatr.data.datastore.converters.ZonedDateTimeConverter
import com.mohitsatr.data.datastore.dao.BoardDao
import com.mohitsatr.data.datastore.dao.FolderDao
import com.mohitsatr.data.datastore.model.FolderEntity
import com.mohitsatr.data.di.datastore.dao.SavedGameDao
import com.mohitsatr.data.di.datastore.model.SavedGameEntity
import com.mohitsatr.data.datastore.model.SudokuBoardEntity

@Database(
    entities = [SavedGameEntity::class, SudokuBoardEntity::class, FolderEntity::class],
    version = 4,
    autoMigrations = [],
    exportSchema = false
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

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context : Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "main_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
            }
            return INSTANCE as AppDatabase
        }
    }
}
