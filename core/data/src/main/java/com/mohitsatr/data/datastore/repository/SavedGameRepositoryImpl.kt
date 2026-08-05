package com.mohitsatr.data.datastore.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.mohitsatr.data.di.datastore.dao.SavedGameDao
import com.mohitsatr.data.di.datastore.model.SavedGameEntity
import com.mohitsatr.domain.repository.SavedGameModel
import com.mohitsatr.domain.repository.SavedGameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedGameRepositoryImpl(
    private val savedGameDao: SavedGameDao
): SavedGameRepository {
    override suspend fun get(boardUid: Long): SavedGameModel? = savedGameDao.get(boardUid)?.toDomain()

    override suspend fun insert(savedGameModel: SavedGameModel): Long = 
        savedGameDao.insert(savedGameModel.toEntity())

    override suspend fun update(savedGameModel: SavedGameModel) = savedGameDao.update(savedGameModel.toEntity())

    override fun getLast(limit: Int): Flow<List<SavedGameModel>> {
        return savedGameDao.getLast(limit).map { list->
            list.map { it.toDomain() }
        }
    }
}

fun SavedGameModel.toEntity(): SavedGameEntity = SavedGameEntity(
    timer = timer,
    lastPlayed = lastPlayed,
    uid = uid,
    initialBoard = initialBoard,
    difficulty = difficulty,
    solvedBoard = solvedBoard,
)

@RequiresApi(Build.VERSION_CODES.O)
fun SavedGameEntity.toDomain(): SavedGameModel = SavedGameModel(
    timer = timer,
    lastPlayed = lastPlayed,
    uid = uid,
    initialBoard = initialBoard,
    solvedBoard = solvedBoard,
    difficulty = difficulty
)
