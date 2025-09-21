package com.codingeveryday.calcapp.presentation

import android.annotation.SuppressLint
import android.util.Log
import com.codingeveryday.calcapp.CalculatorApplication.Companion.INTERNAL_STORAGE
import timber.log.Timber
import java.io.File
import java.util.Calendar

object LogsTree: Timber.Tree() {

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val logTag: String = tag ?: DEFAULT_TAG
        val msg = if (t == null)
            message
        else if (message.isNotBlank())
            "$message: ${t.message}"
        else
            return

        when (priority) {
            Log.DEBUG -> Log.i(logTag, msg)
            Log.INFO -> Log.i(logTag, msg)
            Log.ERROR -> Log.e(logTag, msg)
            Log.WARN -> Log.w(logTag, msg)
            Log.ASSERT -> Log.wtf(logTag, msg)
        }
        dumpToFile(logTag, msg)
    }

    private fun getLogFileName(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "logs-%02d.%02d.%d".format(day, month, year)
    }

    private fun dumpToFile(tag: String, msg: String) {
        val fileName = getLogFileName()
        val file = File(INTERNAL_STORAGE, fileName)
        if (!file.exists()) {
            if (!file.createNewFile()) {
                return
            }
        }
        file.appendText("[$tag] ${msg.replace('\n', ' ')}\n")
    }

    private val DEFAULT_TAG = LogsTree::class.simpleName.toString()
}