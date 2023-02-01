package com.codingeveryday.calcapp.data

import android.app.Application
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {

    abstract fun getHistoryItemDao(): HistoryItemDao

    companion object {
        private const val DB_NAME = "calculation_history"
        private val LOCK = Any()
        private var INSTANCE: AppDataBase? = null

        fun getInstance(application: Application): AppDataBase {
            INSTANCE?.let { return it }
            synchronized(LOCK) {
                INSTANCE?.let { return it }
                val db = Room.databaseBuilder(
                    application,
                    AppDataBase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}