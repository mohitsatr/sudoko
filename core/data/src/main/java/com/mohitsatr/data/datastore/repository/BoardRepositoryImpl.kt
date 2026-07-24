package com.mohitsatr.data.datastore.repository

import com.mohitsatr.data.datastore.dao.BoardDao
import com.mohitsatr.data.datastore.model.SudokuBoardEntity
import com.mohitsatr.domain.repository.BoardRepository
import com.mohitsatr.domain.repository.SudokuBoardModel

class BoardRepositoryImpl(
    private val boardDao: BoardDao
) : BoardRepository {
    override fun get(gameUid: Long): SudokuBoardModel = boardDao.get(gameUid).toDomain()
    override fun insert(sudokuBoardModel: SudokuBoardModel): Long = boardDao.insert(sudokuBoardModel.toEntity())
    override fun update(sudokuBoardModel: SudokuBoardModel) = boardDao.update(sudokuBoardModel.toEntity())
}

fun SudokuBoardEntity.toDomain(): SudokuBoardModel = SudokuBoardModel(
    uid = uid,
    difficulty = difficulty,
    initialBoard = initialBoard,
    solvedBoard = solvedBoard
)

fun SudokuBoardModel.toEntity(): SudokuBoardEntity = SudokuBoardEntity(
    uid = uid,
    difficulty = difficulty,
    initialBoard = initialBoard,
    solvedBoard = solvedBoard
)
