package com.codingeveryday.calcapp.domain.interfaces

import com.codingeveryday.calcapp.domain.entities.HistoryItem

interface HistoryManagerInterface {
    fun getHistoryList(): List<HistoryItem>

    suspend fun addItem(hisItem: HistoryItem)

    suspend fun removeItem(id: Int)

    suspend fun clearAll()
}