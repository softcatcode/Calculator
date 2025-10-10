package com.softcat.domain.useCases

import android.content.Context
import com.softcat.domain.interfaces.ShareDataInterface
import timber.log.Timber
import javax.inject.Inject

class ShareLogsUseCase @Inject constructor(
    private val repository: ShareDataInterface
) {
    operator fun invoke(context: Context, logsStoragePath: String) {
        Timber.i("${this::class.java}.invoke()")
        repository.shareLogsArchive(context, logsStoragePath)
    }
}