package com.softcat.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [HistoryItemDbModel::class], version = 2, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {

    abstract fun getHistoryItemDao(): HistoryItemDao

    companion object {
        private const val DB_NAME = "calculation_history"
        private val LOCK = Any()
        private var INSTANCE: AppDataBase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "alter table ${HistoryItemDbModel.tableName} add column base int;\n"
                        .plus("delete from ${HistoryItemDbModel.tableName} where 1")
                )
            }
        }

        fun getInstance(application: Application): AppDataBase {
            INSTANCE?.let { return it }
            synchronized(LOCK) {
                INSTANCE?.let { return it }
                val db = Room.databaseBuilder(
                    application,
                    AppDataBase::class.java,
                    DB_NAME
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = db
                return db
            }
        }
    }
}