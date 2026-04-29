package com.game.sudoku.domain.usecase

import com.game.sudoku.data.datastore.model.SavedGame
import com.game.sudoku.domain.repository.SavedGameRepository
import javax.inject.Inject

class GetSavedGameUseCase @Inject constructor(
    val savedGameRepository: SavedGameRepository
) {

    operator fun invoke(boardUid: Long): SavedGame = savedGameRepository.get(boardUid)
}
