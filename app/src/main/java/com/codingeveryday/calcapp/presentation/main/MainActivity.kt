package com.codingeveryday.calcapp.presentation.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.codingeveryday.calcapp.CalculatorApplication.Companion.INTERNAL_STORAGE
import com.codingeveryday.calcapp.R
import com.softcat.data.LogsTree
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!checkNotificationPermission()) {
            requestNotificationPermission()
        }
        Timber.plant(LogsTree(INTERNAL_STORAGE.path))
    }

    private fun checkNotificationPermission() = ActivityCompat.checkSelfPermission(
        applicationContext,
        android.Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestNotificationPermission() {
        Timber.i("Notification permission is requested.")
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            NOTIFICATION_PERMISSION_RC
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.i("Permission result (code $requestCode): ($permissions) -> ($grantResults)")
        when (requestCode) {
            NOTIFICATION_PERMISSION_RC -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(
                        this,
                        getString(R.string.notification_permission_warning),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_RC = 1
    }
}