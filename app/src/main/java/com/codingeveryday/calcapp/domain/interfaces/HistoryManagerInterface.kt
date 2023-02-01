package com.codingeveryday.calcapp.domain.interfaces

import androidx.lifecycle.LiveData
import com.codingeveryday.calcapp.domain.HistoryItem

interface HistoryManagerInterface {
    fun getHistoryList(): LiveData<List<HistoryItem>>

    suspend fun addItem(hisItem: HistoryItem)

    suspend fun removeItem(id: Int)

    suspend fun clearAll()
}