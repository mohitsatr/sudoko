package com.mohitsatr.data.datastore.repository

import com.mohitsatr.data.di.datastore.dao.SavedGameDao
import com.mohitsatr.data.di.datastore.model.SavedGameEntity
import com.mohitsatr.domain.repository.SavedGameModel
import com.mohitsatr.domain.repository.SavedGameRepository
import com.mohitsatr.domain.repository.SudokuBoardModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedGameRepositoryImpl(
    private val savedGameDao: SavedGameDao
): SavedGameRepository {
    override fun get(boardUid: Long): SavedGameModel? = savedGameDao.get(boardUid)?.toDomain()

    override fun insert(savedGameModel: SavedGameModel) = savedGameDao.insert(savedGameModel.toEntity())

    override fun update(savedGameModel: SavedGameModel) = savedGameDao.update(savedGameModel.toEntity())

    override fun getLast(last: Int): Flow<Map<SavedGameModel, SudokuBoardModel>> {
        return savedGameDao.getLast(last).map {
            it.map { (key, value) -> key.toDomain() to value.toDomain() }.toMap()
        }
    }
}

fun SavedGameModel.toEntity(): SavedGameEntity = SavedGameEntity(
    savedBoard = savedBoard,
    timer = timer,
    lastPlayed = lastPlayed,
    uid = uid
)

fun SavedGameEntity.toDomain(): SavedGameModel = SavedGameModel(
    savedBoard = savedBoard,
    timer = timer,
    lastPlayed = lastPlayed,
    uid = uid
)
