package com.mohitsatr.domain.usecase

import com.mohitsatr.domain.repository.SavedGameModel
import com.mohitsatr.domain.repository.SavedGameRepository
import javax.inject.Inject

class GetSavedGameUseCase @Inject constructor(
    val savedGameRepository: SavedGameRepository
) {
    suspend operator fun invoke(boardUid: Long): SavedGameModel? = savedGameRepository.get(boardUid)
}
