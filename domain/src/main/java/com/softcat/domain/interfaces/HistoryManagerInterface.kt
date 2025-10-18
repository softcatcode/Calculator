package com.softcat.domain.interfaces

import androidx.lifecycle.LiveData
import com.softcat.domain.entities.HistoryItem

interface HistoryManagerInterface {
    fun getHistoryList(): LiveData<List<HistoryItem>>

    suspend fun addItem(hisItem: HistoryItem)

    suspend fun removeItem(id: Int)

    suspend fun clearAll()
}