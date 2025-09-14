package com.codingeveryday.calcapp.data.implementations

import androidx.lifecycle.MediatorLiveData
import com.codingeveryday.calcapp.data.database.HistoryItemDao
import com.codingeveryday.calcapp.data.mapper.HistoryItemMapper
import com.codingeveryday.calcapp.domain.entities.HistoryItem
import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import javax.inject.Inject

class HistoryManagerImplementation @Inject constructor(
    private val historyItemDao: HistoryItemDao,
    private val mapper: HistoryItemMapper
): HistoryManagerInterface {

    override fun getHistoryList() = MediatorLiveData<List<HistoryItem>>().apply {
        addSource(historyItemDao.getItemList()) {
            value = mapper.mapListDbModelToListEntity(it)
        }
    }

    override suspend fun addItem(hisItem: HistoryItem) {
        historyItemDao.addItem(mapper.mapHistoryItemToDbModel(hisItem))
    }

    override suspend fun removeItem(id: Int) {
        historyItemDao.deleteItem(id)
    }

    override suspend fun clearAll() {
        historyItemDao.clear()
    }
}