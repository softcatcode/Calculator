package com.codingeveryday.calcapp.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.codingeveryday.calcapp.CalculatorApplication
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.HistoryItem
import com.codingeveryday.calcapp.domain.interfaces.HistoryItemMapper
import com.codingeveryday.calcapp.domain.useCases.CalculateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class CalcService: Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val component by lazy {
        (application as CalculatorApplication).component
    }
    private var success = true

    @Inject
    lateinit var calcUseCase: CalculateUseCase
    @Inject
    lateinit var historyDao: HistoryItemDao

    var mapper = HistoryItemMapperImpl()

    override fun onCreate() {
        component.inject(this@CalcService)
        super.onCreate()
        running = true
        createNotificationChannel()
        val notification = createNotification(
            getString(R.string.calc_run_title),
            getString(R.string.calc_run_notification_text),
            R.drawable.calc_running,
            true
        )
        startForeground(
            CALC_RUN_NOTIFICATION_ID,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val initExpr = intent?.getStringExtra(EXPR_KEY) ?: ""
        val base = intent?.getIntExtra(BASE_KEY, 0) ?: 0
        val angleUnit = intent?.getParcelableExtra(ANGLE_UNIT_KEY) ?: AngleUnit.Radians

        coroutineScope.launch {
            try {
                val result = calcUseCase(initExpr, base, angleUnit)
                historyDao.addItem(
                    mapper.mapHistoryItemToDbModel(
                        HistoryItem(initExpr, result)
                    )
                )
                success = true
                stopSelf()
            } catch(_: Exception) {
                success = false
            }
        }
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(title: String, content: String, iconId: Int, swipeAble: Boolean) =
        NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(iconId)
        .setOngoing(swipeAble)
        .build()

    override fun onDestroy() {
        coroutineScope.cancel()
        val message = if (success) "" else getString(R.string.calc_error_message)
        val iconId = if (success) R.drawable.calc_over else R.drawable.cross_btn
        val notification = createNotification(
            getString(R.string.calc_over_title),
            message,
            iconId,
            false
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(CALC_OVER_NOTIFICATION_ID, notification)
        running = false
        super.onDestroy()
    }

    companion object {

        private const val CHANNEL_NAME = "calculation condition notifying"
        private const val CHANNEL_ID = "calc_progress_info"
        private const val CALC_RUN_NOTIFICATION_ID = 100
        private const val CALC_OVER_NOTIFICATION_ID = 200
        private const val EXPR_KEY = "expression"
        private const val BASE_KEY = "base"
        private const val ANGLE_UNIT_KEY = "angle_unit"
        var running = false
            private set

        fun newIntent(context: Context, expr: String, base: Int, angleUnit: AngleUnit) =
            Intent(context, CalcService::class.java).apply {
                putExtra(EXPR_KEY, expr)
                putExtra(BASE_KEY, base)
                putExtra(ANGLE_UNIT_KEY, angleUnit)
            }
    }

}