package com.mohitsatr.domain.usecase

import com.mohitsatr.domain.repository.BoardRepository
import com.mohitsatr.domain.repository.SudokuBoardModel
import jakarta.inject.Inject

class GetBoardUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    operator fun invoke(gameUid: Long): SudokuBoardModel = boardRepository.get(gameUid)
}
