package com.codingeveryday.calcapp.domain.useCases

import androidx.lifecycle.LiveData
import com.codingeveryday.calcapp.domain.HistoryItem
import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import javax.inject.Inject

class GetHistoryListUseCase @Inject constructor(private val repository: HistoryManagerInterface) {
    operator fun invoke(): LiveData<List<HistoryItem>> {
        return repository.getHistoryList()
    }
}