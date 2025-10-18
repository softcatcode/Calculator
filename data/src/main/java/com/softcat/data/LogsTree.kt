package com.softcat.data

import android.annotation.SuppressLint
import android.util.Log
import timber.log.Timber
import java.io.File
import java.util.Calendar

class LogsTree(
    internalStoragePath: String,
): Timber.Tree() {

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val logTag: String = tag ?: defaultTag
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
        val dir = File(logsStorage)
        if (!dir.exists())
            dir.mkdir()
        val file = File(logsStorage, fileName)
        if (!file.exists()) {
            if (!file.createNewFile()) {
                return
            }
        }
        file.appendText("[$tag] ${msg.replace('\n', ' ')}\n")
    }

    val logsStorage = "$internalStoragePath/$LOG_DIR_NAME"
    private val defaultTag = LogsTree::class.simpleName.toString()

    companion object {
        const val LOG_DIR_NAME = "log_storage"
    }
}