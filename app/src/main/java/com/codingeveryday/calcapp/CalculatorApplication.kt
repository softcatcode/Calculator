package com.codingeveryday.calcapp

import android.app.Application
import com.codingeveryday.calcapp.di.DaggerApplicationComponent
import com.softcat.data.LogsTree.Companion.LOG_DIR_NAME
import java.io.File

class CalculatorApplication: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        INTERNAL_STORAGE = filesDir
    }

    fun getLogsStoragePath() = "$INTERNAL_STORAGE/$LOG_DIR_NAME"

    companion object {
        lateinit var INTERNAL_STORAGE: File
            private set
    }
}