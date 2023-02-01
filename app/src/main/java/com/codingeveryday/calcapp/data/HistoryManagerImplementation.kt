package com.codingeveryday.calcapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.codingeveryday.calcapp.domain.HistoryItem
import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import javax.inject.Inject

class HistoryManagerImplementation @Inject constructor(
    private val historyItemDao: HistoryItemDao
): HistoryManagerInterface {

    //private val historyItemDao = AppDataBase.getInstance(application).getHistoryItemDao()

    override fun getHistoryList(): LiveData<List<HistoryItem>> = MediatorLiveData<List<HistoryItem>>().apply {
        addSource(historyItemDao.getItemList()) {
            postValue(HistoryItemMapper.mapListDbModelToListEntity(it))
        }
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