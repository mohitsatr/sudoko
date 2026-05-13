package com.game.sudoku.domain.usecase

import com.game.sudoku.data.datastore.model.SudokuBoardModel
import com.game.sudoku.domain.repository.BoardRepository
import jakarta.inject.Inject

class GetBoardUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    operator fun invoke(gameUid: Long): SudokuBoardModel = boardRepository.get(gameUid)
}
