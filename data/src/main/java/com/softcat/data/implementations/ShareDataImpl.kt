package com.softcat.data.implementations

import android.content.Context
import android.content.Intent
import android.os.StrictMode
import androidx.core.content.FileProvider
import com.softcat.domain.interfaces.ShareDataInterface
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

class ShareDataImpl @Inject constructor(
    private val fileProviderAuthority: String
): ShareDataInterface {

    companion object {
        private const val SEND_INTENT_TITLE = "Send logs report."
        private const val SEND_LOGS_EMAIL = "d5.machilskiy@gmail.com"
        private const val SEND_LOGS_TITLE = "CalculatorLogReport"
        private const val ZIPPED_FILE_NAME = "logs.zip"
    }

    private fun createZipArchive(inputFiles: List<File>, logsStoragePath: String) {
        val fileOutputStream = FileOutputStream("$logsStoragePath/$ZIPPED_FILE_NAME")
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

    private fun createSendIntent(context: Context, logsStoragePath: String) = Intent().apply {
        action = Intent.ACTION_SEND
        type = "application/zip"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(SEND_LOGS_EMAIL))
        putExtra(Intent.EXTRA_SUBJECT, SEND_LOGS_TITLE)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val uri = FileProvider.getUriForFile(
            context,
            fileProviderAuthority,
            File("$logsStoragePath/$ZIPPED_FILE_NAME")
        )
        putExtra(Intent.EXTRA_STREAM, uri)
    }

    private fun getLogsFiles(logsStoragePath: String): List<File> {
        val dir = File(logsStoragePath)
        return dir.listFiles()?.filter {
            it.name.contains("logs")
        } ?: emptyList()
    }

    override fun shareLogsArchive(context: Context, logsStoragePath: String) {
        createZipArchive(getLogsFiles(logsStoragePath), logsStoragePath)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        context.startActivity(
            Intent.createChooser(
                createSendIntent(context, logsStoragePath),
                SEND_INTENT_TITLE
            )
        )
    }

    override fun clearLogs(logsStoragePath: String) {
        getLogsFiles(logsStoragePath).forEach {
            it.delete()
        }
    }
}