package com.game.sudoku


data class GameGeneratingState(
    val generationStatus: GenerationStatus = GenerationStatus.IDLE,
    val insertedBoardUid: Long? = null
)

enum class GenerationStatus {
    IDLE, GENERATING, SOLVING, READY
}
