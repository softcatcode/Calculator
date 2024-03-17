package com.codingeveryday.calcapp.data

import com.codingeveryday.calcapp.domain.entities.HistoryItem
import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import javax.inject.Inject

class HistoryManagerImplementation @Inject constructor(
    private val historyItemDao: HistoryItemDao
): HistoryManagerInterface {

    override fun getHistoryList() = historyItemDao.getItemList().map {
        HistoryItem(expr = it.expr, result = it.result, id = it.id)
    }

    override suspend fun addItem(hisItem: HistoryItem) {
        historyItemDao.addItem(HistoryItemMapper.mapHistoryItemToDbModel(hisItem))
    }

    override suspend fun removeItem(id: Int) {
        historyItemDao.deleteItem(id)
    }

    override suspend fun clearAll() {
        historyItemDao.clear()
    }
}