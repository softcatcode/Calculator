package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import timber.log.Timber
import javax.inject.Inject

class RemoveHistoryItemUseCase @Inject constructor(private val repository: HistoryManagerInterface) {
    suspend operator fun invoke(id: Int) {
        Timber.i("${this::class.simpleName}.invoke($id)")
        repository.removeItem(id)
    }
}