package com.softcat.domain.useCases

import com.softcat.domain.interfaces.HistoryManagerInterface
import timber.log.Timber
import javax.inject.Inject

class ClearHistoryUseCase @Inject constructor(private val repository: HistoryManagerInterface) {
    suspend operator fun invoke() {
        Timber.i("${this::class.simpleName}.invoke()")
        repository.clearAll()
    }
}