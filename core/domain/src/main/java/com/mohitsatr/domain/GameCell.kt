package com.mohitsatr.domain

import io.github.ilikeyourhat.kudoku.model.Cell

// needs to be serialized
data class GameCell(
    val row: Int,
    val column: Int,
    var value: Int = 0,
    var error: Boolean = false,
    var locked: Boolean = false
) {
    internal val internalCell: Cell = Cell(row, column, value)

    fun isValidToUpdate() = row > -1 && column > -1 && !locked

    companion object {
        val unSelectedCell = GameCell(-1, -1, 0)
    }
}
