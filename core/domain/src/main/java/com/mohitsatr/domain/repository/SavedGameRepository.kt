package com.mohitsatr.domain.repository

import kotlinx.coroutines.flow.Flow

interface SavedGameRepository {

    fun get(boardUid: Long): SavedGameModel?

    fun insert(savedGameModel: SavedGameModel)

    fun update(savedGameModel: SavedGameModel)

    fun getLast(last: Int): Flow<Map<SavedGameModel, SudokuBoardModel>>
}
