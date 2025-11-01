package com.game.sudoku.domain.usecase

import com.game.sudoku.domain.repository.BoardRepository
import jakarta.inject.Inject

class GetBoardUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(uid: Long) = boardRepository.get(uid)
}
