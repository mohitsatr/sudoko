package com.game.sudoku.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.ilikeyourhat.kudoku.generating.SudokuGenerator
import io.github.ilikeyourhat.kudoku.generating.defaultGenerator
import io.github.ilikeyourhat.kudoku.model.Sudoku
import io.github.ilikeyourhat.kudoku.solving.SudokuSolver
import io.github.ilikeyourhat.kudoku.solving.defaultSolver
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesSudokuGenerator(): SudokuGenerator {
        return Sudoku.defaultGenerator()
    }

    @Provides
    @Singleton
    fun providesSudokuSolver(): SudokuSolver {
        return Sudoku.defaultSolver()
    }
}

//    @Provides
//    @Singleton
//    fun providesThemeSettingsManager(@ApplicationContext context: Context) =
//        ThemeSettingsManager(context)

//    @Singleton
//    @Provides
//    fun provideSavedGameRepository(savedGameDao: SavedGameDao): SavedGameRepository =
//        SavedGameRepositoryImpl(savedGameDao)
//
//    @Singleton
//    @Provides
//    fun provideSavedGameDao(appDatabase: AppDatabase): SavedGameDao = appDatabase.savedGameDao()
//
//    @Singleton
//    @Provides
//    fun provideBoardRepository(boardDao: BoardDao): BoardRepository = BoardRepositoryImpl(boardDao)
//
//    @Singleton
//    @Provides
//    fun provideAppDatabase(app: Application): AppDatabase = AppDatabase.getInstance(context = app)
//
//    @Singleton
//    @Provides
//    fun provideBoardDao(appDatabase: AppDatabase): BoardDao = appDatabase.boardDao()
//}
