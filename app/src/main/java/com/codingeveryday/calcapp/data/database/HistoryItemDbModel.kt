package com.codingeveryday.calcapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = HistoryItemDbModel.tableName)
data class HistoryItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val expr: String,
    val base: Int?,
    val result: String
) {
    companion object {
        const val tableName = "historyLog"
    }
}