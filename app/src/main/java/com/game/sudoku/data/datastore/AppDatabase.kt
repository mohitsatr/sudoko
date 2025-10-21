package com.game.sudoku.data.datastore

//abstract class AppDatabase : RoomDatabase(){
//    abstract fun recordDao(): RecordDao
//    abstract fun boardDao(): BoardDao
//    abstract fun savedGameDao(): SavedGameDao
//    abstract fun folderDao(): FolderDao
//
//
//    companion object {
//        private var INSTANCE: AppDatabase? = null
//
//        fun getInstance(context : Context): AppDatabase {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder(
//                    context,
//                    AppDatabase::class.java,
//                    "main_database"
//                ).build()
//            }
//
//            return INSTANCE as AppDatabase
//        }
//    }
//}
