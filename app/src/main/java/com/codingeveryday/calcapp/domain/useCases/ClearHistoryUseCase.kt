package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import javax.inject.Inject

class ClearHistoryUseCase @Inject constructor(private val repository: HistoryManagerInterface) {
    suspend operator fun invoke() {
        repository.clearAll()
    }
}