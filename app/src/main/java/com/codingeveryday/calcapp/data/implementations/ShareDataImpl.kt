package com.codingeveryday.calcapp.data.implementations

import android.content.Context
import android.content.Intent
import android.os.StrictMode
import androidx.core.content.FileProvider
import com.codingeveryday.calcapp.BuildConfig
import com.codingeveryday.calcapp.domain.interfaces.ShareDataInterface
import com.codingeveryday.calcapp.presentation.LogsTree
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

class ShareDataImpl @Inject constructor(): ShareDataInterface {

    companion object {
        private const val SEND_INTENT_TITLE = "Send logs report."
        private val ZIPPED_LOGS_FILEPATH = "${LogsTree.LOGS_STORAGE}/logs.zip"
        private const val SEND_LOGS_EMAIL = "d5.machilskiy@gmail.com"
        private const val SEND_LOGS_TITLE = "CalculatorLogReport"
        private const val FILE_PROVIDER_AUTHORITY = "${BuildConfig.APPLICATION_ID}.provider"
    }

    private fun createZipArchive(inputFiles: List<File>) {
        val fileOutputStream = FileOutputStream(ZIPPED_LOGS_FILEPATH)
        val zipOutputStream = ZipOutputStream(fileOutputStream)

        inputFiles.filter { it.exists() }.forEach { inputFile ->
            val fileInputStream = FileInputStream(inputFile)
            val zipEntry = ZipEntry(inputFile.name)
            zipOutputStream.putNextEntry(zipEntry)
            val buf = ByteArray(1024 * 128)
            var bytesRead = fileInputStream.read(buf)
            while (bytesRead > 0) {
                zipOutputStream.write(buf, 0, bytesRead)
                bytesRead = fileInputStream.read(buf)
            }
            zipOutputStream.closeEntry()
            fileInputStream.close()
        }

        zipOutputStream.close()
        fileOutputStream.close()
    }

    private fun createSendIntent(context: Context) = Intent().apply {
        action = Intent.ACTION_SEND
        type = "application/zip"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(SEND_LOGS_EMAIL))
        putExtra(Intent.EXTRA_SUBJECT, SEND_LOGS_TITLE)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val uri = FileProvider.getUriForFile(
            context,
            FILE_PROVIDER_AUTHORITY,
            File(ZIPPED_LOGS_FILEPATH)
        )
        putExtra(Intent.EXTRA_STREAM, uri)
    }

    private fun getLogsFiles(): List<File> {
        val dir = File(LogsTree.LOGS_STORAGE)
        return dir.listFiles()?.filter {
            it.name.contains("logs")
        } ?: emptyList()
    }

    override fun shareLogsArchive(context: Context) {
        createZipArchive(getLogsFiles())
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        context.startActivity(
            Intent.createChooser(
                createSendIntent(context),
                SEND_INTENT_TITLE
            )
        )
    }

    override fun clearLogs() {
        getLogsFiles().forEach {
            it.delete()
        }
    }
}