package com.mohitsatr.domain

// needs to be serialized
data class Cell(
    val row: Int,
    val column: Int,
    var value: Int = 0,
    var error: Boolean = false,
    var locked: Boolean = false
)