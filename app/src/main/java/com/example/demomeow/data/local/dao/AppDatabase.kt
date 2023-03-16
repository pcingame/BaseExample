package com.example.demomeow.data.local.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.demomeow.data.entity.ExampleEntity
//import com.example.demomeow.data.local.dao.AppDatabase.Companion.DATABASE_VERSION

//@Database(
//    entities = [
//        ExampleEntity::class,
//    ], version = DATABASE_VERSION, exportSchema = false
//)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun exampleDao(): ExampleDao
//
//    companion object {
//        const val DATABASE_VERSION = 1
//        private const val DB_NAME = "example_db"
//
//        fun build(context: Context): AppDatabase =
//            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
//                .fallbackToDestructiveMigration()
//                .build()
//    }
//}
