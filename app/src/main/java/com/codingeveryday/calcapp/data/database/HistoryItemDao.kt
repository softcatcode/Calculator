package com.codingeveryday.calcapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryItemDao {
    @Query("SELECT * FROM ${HistoryItemDbModel.tableName} WHERE id = :id LIMIT 1")
    suspend fun getItem(id: Int): HistoryItemDbModel

    @Query("DELETE FROM ${HistoryItemDbModel.tableName} WHERE id = :id")
    suspend fun deleteItem(id: Int)

    @Query("SELECT * FROM ${HistoryItemDbModel.tableName}")
    fun getItemList(): LiveData<List<HistoryItemDbModel>>

    @Query("DELETE FROM ${HistoryItemDbModel.tableName} WHERE 1")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(historyItem: HistoryItemDbModel)
}