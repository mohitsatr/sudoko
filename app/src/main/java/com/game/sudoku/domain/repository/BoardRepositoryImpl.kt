package com.game.sudoku.domain.repository
//
//import com.game.sudoku.data.datastore.dao.BoardDao
//import com.game.sudoku.data.datastore.model.SudokuBoard
//import com.game.sudoku.ui.core.qqwing.GameDifficulty
//import io.github.ilikeyourhat.kudoku.model.Sudoku
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class BoardRepositoryImpl @Inject constructor(
//    private val boardDao: BoardDao
//) : BoardRepository {
//    override fun getAll(): Flow<List<Sudoku>> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAll(difficulty: GameDifficulty): Flow<List<SudokuBoard>> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun get(uid: Long): SudokuBoard {
//        TODO("Not yet implemented")
//    }
//
//    override fun insert(boards: List<SudokuBoard>): List<Long> {
//        TODO("Not yet implemented")
//    }
//
//    override fun insert(board: SudokuBoard): Long {
//        TODO("Not yet implemented")
//    }
//}