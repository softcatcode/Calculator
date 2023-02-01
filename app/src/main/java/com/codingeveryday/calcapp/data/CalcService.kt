package com.codingeveryday.calcapp.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.codingeveryday.calcapp.CalculatorApplication
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.di.ApplicationComponent
import com.codingeveryday.calcapp.domain.HistoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class CalcService: Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val mainThreadScope = CoroutineScope(Dispatchers.Main)
    private var success = true
    private lateinit var component: ApplicationComponent

    @Inject
    lateinit var repository: CalculationImplementation
    @Inject
    lateinit var historyDao: HistoryItemDao

    override fun onCreate() {
        _running = true
        component = (application as CalculatorApplication).component
        component.inject(this@CalcService)
        super.onCreate()

        createNotificationChannel()
        val notification = createNotification(
            getString(R.string.calc_run_title),
            getString(R.string.calc_run_notification_text),
            R.drawable.calc_running,
            true
        )
        startForeground(CALC_RUN_NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val initExpr = intent?.getStringExtra(EXPR_LABEL)
        val base = intent?.getIntExtra(BASE_LABEL, 0)
        if (base == null || base == 0 || initExpr == null) {
            success = false
            stopSelf()
            return START_NOT_STICKY
        }
        repository.setExpr(initExpr!!)
        if (!repository.checkExpr(base!!)) {
            success = false
            stopSelf()
            return START_NOT_STICKY
        }
        coroutineScope.launch {
            repository.runCalcProcess(base)
            mainThreadScope.launch {
                repository.updateSolution()
                repository.updateExpr()
                val expr = repository.expr.value!!
                if (expr[0] in "×/") {
                    success = false
                    stopSelf()
                    return@launch
                }
                for (i in 1 until expr.length) {
                    if (expr[i] in "+-×/") {
                        success = false
                        stopSelf()
                        return@launch
                    }
                }
                coroutineScope.launch {
                    historyDao.addItem(
                        HistoryItemMapper.mapHistoryItemToDbModel(
                            HistoryItem(initExpr, repository.expr.value.toString())
                        )
                    )
                    stopSelf()
                }
            }
        }
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

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
        _running = false
        coroutineScope.cancel()
        mainThreadScope.cancel()
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
        super.onDestroy()
    }

    companion object {

        private const val CHANNEL_NAME = "calculation condition notifying"
        private const val CHANNEL_ID = "calc_progress_info"
        private const val CALC_RUN_NOTIFICATION_ID = 100
        private const val CALC_OVER_NOTIFICATION_ID = 200
        private const val EXPR_LABEL = "expression"
        private const val BASE_LABEL = "base"

        private var _running = false
        val running: Boolean
            get() = _running

        fun newIntent(context: Context, expr: String, base: Int) = Intent(context, CalcService::class.java).apply {
            putExtra(EXPR_LABEL, expr)
            putExtra(BASE_LABEL, base)
        }
    }

}