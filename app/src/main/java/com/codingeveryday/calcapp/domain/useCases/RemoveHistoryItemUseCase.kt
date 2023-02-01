package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import javax.inject.Inject

class RemoveHistoryItemUseCase @Inject constructor(private val repository: HistoryManagerInterface) {
    suspend operator fun invoke(id: Int) {
        repository.removeItem(id)
    }
}