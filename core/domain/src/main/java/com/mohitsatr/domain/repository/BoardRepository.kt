package com.mohitsatr.domain.repository

interface BoardRepository {

    fun get(gameUid: Long): SudokuBoardModel

    fun insert(sudokuBoardModel: SudokuBoardModel): Long

    fun update(sudokuBoardModel: SudokuBoardModel)
}
