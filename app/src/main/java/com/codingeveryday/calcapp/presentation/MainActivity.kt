package com.codingeveryday.calcapp.presentation

import android.app.Dialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.codingeveryday.calcapp.R
import kotlinx.android.synthetic.main.fragment_to_number_system.*
import kotlinx.android.synthetic.main.keyboard_dialog.*

class MainActivity : AppCompatActivity() {

    private lateinit var _numberDialog: Dialog
    val numberDialog: Dialog
        get() = _numberDialog

    private var _latestDialogInput: String? = null
    val latestDialogInput: String
        get() = _latestDialogInput ?: ""

    private val stringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*val numberDialogBinding = layoutInflater.inflate(R.layout.keyboard_dialog, null)
        _numberDialog = Dialog(this)
        _numberDialog.setContentView(numberDialogBinding)
        _numberDialog.setCancelable(false)
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val side = min(width, height)
        _numberDialog.window?.setLayout(side, side)
        setDialogListeners()*/
    }

    private fun setDialogListeners() {
        _numberDialog.setOnShowListener {
            _numberDialog.findViewById<TextView>(R.id.numberField).setText(stringBuilder.toString())
            cancelBtn.setOnClickListener {
                _numberDialog.cancel()
            }
            approveNumberButton.setOnClickListener {
                _latestDialogInput = dialogNumberField.text.toString()
                stringBuilder.clear()
                with (stringBuilder) {
                    zero.setOnClickListener { append('0'); numberField.setText(toString()) }
                    one.setOnClickListener { append('1'); numberField.setText(toString()) }
                    two.setOnClickListener { stringBuilder.append('2'); numberField.setText(stringBuilder.toString()) }
                    three.setOnClickListener { stringBuilder.append('3'); numberField.setText(stringBuilder.toString()) }
                    four.setOnClickListener { stringBuilder.append('4'); numberField.setText(stringBuilder.toString()) }
                    five.setOnClickListener { stringBuilder.append('5'); numberField.setText(stringBuilder.toString()) }
                    six.setOnClickListener { stringBuilder.append('6'); numberField.setText(stringBuilder.toString()) }
                    seven.setOnClickListener { stringBuilder.append('7'); numberField.setText(stringBuilder.toString()) }
                    eight.setOnClickListener { stringBuilder.append('8'); numberField.setText(stringBuilder.toString()) }
                    nine.setOnClickListener { stringBuilder.append('9'); numberField.setText(stringBuilder.toString()) }
                    a.setOnClickListener { stringBuilder.append('a'); numberField.setText(stringBuilder.toString()) }
                    b.setOnClickListener { stringBuilder.append('b'); numberField.setText(stringBuilder.toString()) }
                    c.setOnClickListener { stringBuilder.append('c'); numberField.setText(stringBuilder.toString()) }
                    d.setOnClickListener { stringBuilder.append('d'); numberField.setText(stringBuilder.toString()) }
                    e.setOnClickListener { stringBuilder.append('e'); numberField.setText(stringBuilder.toString()) }
                    f.setOnClickListener { stringBuilder.append('f'); numberField.setText(stringBuilder.toString()) }
                    g.setOnClickListener { stringBuilder.append('g'); numberField.setText(stringBuilder.toString()) }
                    h.setOnClickListener { stringBuilder.append('h'); numberField.setText(stringBuilder.toString()) }
                    i.setOnClickListener { stringBuilder.append('i'); numberField.setText(stringBuilder.toString()) }
                    j.setOnClickListener { stringBuilder.append('j'); numberField.setText(stringBuilder.toString()) }
                    k.setOnClickListener { stringBuilder.append('k'); numberField.setText(stringBuilder.toString()) }
                    l.setOnClickListener { stringBuilder.append('l'); numberField.setText(stringBuilder.toString()) }
                    m.setOnClickListener { stringBuilder.append('m'); numberField.setText(stringBuilder.toString()) }
                    n.setOnClickListener { stringBuilder.append('n'); numberField.setText(stringBuilder.toString()) }
                    o.setOnClickListener { stringBuilder.append('o'); numberField.setText(stringBuilder.toString()) }
                    p.setOnClickListener { stringBuilder.append('p'); numberField.setText(stringBuilder.toString()) }
                    q.setOnClickListener { stringBuilder.append('q'); numberField.setText(stringBuilder.toString()) }
                    r.setOnClickListener { stringBuilder.append('r'); numberField.setText(stringBuilder.toString()) }
                    s.setOnClickListener { stringBuilder.append('s'); numberField.setText(stringBuilder.toString()) }
                    t.setOnClickListener { stringBuilder.append('t'); numberField.setText(stringBuilder.toString()) }
                    u.setOnClickListener { stringBuilder.append('u'); numberField.setText(stringBuilder.toString()) }
                    v.setOnClickListener { stringBuilder.append('v'); numberField.setText(stringBuilder.toString()) }
                    w.setOnClickListener { stringBuilder.append('w'); numberField.setText(stringBuilder.toString()) }
                    x.setOnClickListener { stringBuilder.append('x'); numberField.setText(stringBuilder.toString()) }
                    y.setOnClickListener { stringBuilder.append('y'); numberField.setText(stringBuilder.toString()) }
                    z.setOnClickListener { stringBuilder.append('z'); numberField.setText(stringBuilder.toString()) }
                }
            }
        }
    }

    fun checkNotificationPermission() = ActivityCompat.checkSelfPermission(
        applicationContext,
        android.Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    fun requestNotificationPermission() {
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
        when (requestCode) {
            NOTIFICATION_PERMISSION_RC -> {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
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
        private const val NOTIFICATION_PERMISSION_RC = 2
    }
}