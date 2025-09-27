package com.codingeveryday.calcapp.domain.interfaces

import android.content.Context

interface ShareDataInterface {

    fun shareLogsArchive(context: Context)

    fun clearLogs()
}