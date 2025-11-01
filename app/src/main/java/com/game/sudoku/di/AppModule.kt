package com.game.sudoku.di

import android.app.Application
import android.content.Context
import com.game.sudoku.data.datastore.AppDatabase
import com.game.sudoku.data.datastore.AppSettingsManager
import com.game.sudoku.data.datastore.ThemeSettingsManager
import com.game.sudoku.data.datastore.dao.BoardDao
import com.game.sudoku.data.datastore.dao.SavedGameDao
import com.game.sudoku.domain.repository.BoardRepository
import com.game.sudoku.domain.repository.BoardRepositoryImpl
import com.game.sudoku.domain.repository.SavedGameRepository
import com.game.sudoku.domain.repository.SavedGameRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesAppSettingsManager(@ApplicationContext context: Context) : AppSettingsManager {
        return AppSettingsManager(context)
    }

    @Provides
    @Singleton
    fun providesThemeSettingsManager(@ApplicationContext context: Context) =
        ThemeSettingsManager(context)

    @Singleton
    @Provides
    fun provideSavedGameRepository(savedGameDao: SavedGameDao): SavedGameRepository =
        SavedGameRepositoryImpl(savedGameDao)

    @Singleton
    @Provides
    fun provideSavedGameDao(appDatabase: AppDatabase): SavedGameDao = appDatabase.savedGameDao()

    @Singleton
    @Provides
    fun provideBoardRepository(boardDao: BoardDao): BoardRepository = BoardRepositoryImpl(boardDao)

    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase = AppDatabase.getInstance(context = app)

    @Singleton
    @Provides
    fun provideBoardDao(appDatabase: AppDatabase): BoardDao = appDatabase.boardDao()
}
