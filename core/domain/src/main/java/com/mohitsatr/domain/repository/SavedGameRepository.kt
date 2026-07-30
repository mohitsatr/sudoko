package com.mohitsatr.domain.repository

import kotlinx.coroutines.flow.Flow

interface SavedGameRepository {

    suspend fun get(boardUid: Long): SavedGameModel?

    suspend fun insert(savedGameModel: SavedGameModel): Long

    suspend fun update(savedGameModel: SavedGameModel)

    fun getLast(limit: Int): Flow<List<SavedGameModel>>
}
