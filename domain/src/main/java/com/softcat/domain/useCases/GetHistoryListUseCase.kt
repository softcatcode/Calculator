package com.softcat.domain.useCases

import androidx.lifecycle.LiveData
import com.softcat.domain.entities.HistoryItem
import com.softcat.domain.interfaces.HistoryManagerInterface
import timber.log.Timber
import javax.inject.Inject

class GetHistoryListUseCase @Inject constructor(private val repository: HistoryManagerInterface) {
    operator fun invoke(): LiveData<List<HistoryItem>> {
        Timber.i("${this::class.simpleName}.invoke()")
        return repository.getHistoryList()
    }
}