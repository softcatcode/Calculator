package com.softcat.domain.useCases

import com.softcat.domain.entities.HistoryItem
import com.softcat.domain.interfaces.HistoryManagerInterface
import timber.log.Timber
import javax.inject.Inject

class AddHistoryItemUseCase @Inject constructor(private val repository: HistoryManagerInterface) {
    suspend operator fun invoke(item: HistoryItem) {
        Timber.i("${this::class.simpleName}.invoke($item)")
        repository.addItem(item)
    }
}