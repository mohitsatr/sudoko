package com.game.sudoku.data.datastore.model

//@Serializable
//@Entity(
//    tableName = "board",
//    foreignKeys = [
//        ForeignKey(
//            onDelete = ForeignKey.CASCADE,
//            entity = Folder::class,
//            parentColumns = arrayOf("uid"),
//            childColumns = arrayOf("folder_id")
//        )
//    ]
//)
//data class SudokuBoard(
//    @PrimaryKey(autoGenerate = true) val uid: Long,
//    @ColumnInfo(name = "initial_board") val initialBoard: String,
//    @ColumnInfo("solved_board") val solvedBoard: String,
//    @ColumnInfo("difficulty") val difficulty: GameDifficulty,
//    @ColumnInfo("type") val type: GameType,
//    @ColumnInfo(name = "folder_id", defaultValue = "null") val folderId: Long? = null,
//    @ColumnInfo(name = "killer_cages", defaultValue = "null") val killerCages: String? = null
//)
