package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.HistoryItem
import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import javax.inject.Inject

class AddHistoryItemUseCase @Inject constructor(private val repository: HistoryManagerInterface) {
    suspend operator fun invoke(item: HistoryItem) {
        repository.addItem(item)
    }
}