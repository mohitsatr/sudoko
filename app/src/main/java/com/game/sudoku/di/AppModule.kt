package com.game.sudoku.di

import android.content.Context
import com.game.sudoku.ui.data.datastore.AppSettingsManager
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    fun providesAppSettingsManager(@ApplicationContext context: Context) {
        AppSettingsManager(context)
    }
}