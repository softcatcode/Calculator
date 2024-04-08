package com.codingeveryday.calcapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = HistoryItemDbModel.tableName)
class HistoryItemDbModel(
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