package com.codingeveryday.calcapp.domain.useCases

import android.content.Context
import com.codingeveryday.calcapp.domain.interfaces.ShareDataInterface
import timber.log.Timber
import javax.inject.Inject

class ShareLogsUseCase @Inject constructor(
    private val repository: ShareDataInterface
) {
    operator fun invoke(context: Context) {
        Timber.i("${this::class.java}.invoke()")
        repository.shareLogsArchive(context)
    }
}