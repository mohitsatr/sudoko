package com.mohitsatr.domain.usecase

import com.mohitsatr.domain.repository.SavedGameModel
import com.mohitsatr.domain.repository.SavedGameRepository
import javax.inject.Inject

class GetSavedGameUseCase @Inject constructor(
    val savedGameRepository: SavedGameRepository
) {
    operator fun invoke(boardUid: Long): SavedGameModel? = savedGameRepository.get(boardUid)
}
