package com.softcat.domain.interfaces

import android.content.Context

interface ShareDataInterface {

    fun shareLogsArchive(context: Context, logsStoragePath: String)

    fun clearLogs(logsStoragePath: String)
}