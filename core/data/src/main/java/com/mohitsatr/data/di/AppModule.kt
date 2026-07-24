package com.mohitsatr.data.di

import android.app.Application
import android.content.Context
import com.mohitsatr.data.datastore.AppDatabase
import com.mohitsatr.data.datastore.ThemeSettingsManager
import com.mohitsatr.data.datastore.dao.BoardDao
import com.mohitsatr.data.datastore.repository.BoardRepositoryImpl
import com.mohitsatr.data.datastore.repository.SavedGameRepositoryImpl
import com.mohitsatr.data.di.datastore.dao.SavedGameDao
import com.mohitsatr.domain.repository.BoardRepository
import com.mohitsatr.domain.repository.SavedGameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
//
//    @Provides
//    @Singleton
//    fun providesAppSettingsManager(@ApplicationContext context: Context) : AppSettingsManager {
//        return AppSettingsManager(context)
//    }
//
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
//
    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase = AppDatabase.getInstance(context = app)

    @Singleton
    @Provides
    fun provideBoardDao(appDatabase: AppDatabase): BoardDao = appDatabase.boardDao()
}
