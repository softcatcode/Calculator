package com.codingeveryday.calcapp

import android.app.Application
import com.codingeveryday.calcapp.di.DaggerApplicationComponent
import java.io.File

class CalculatorApplication: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        INTERNAL_STORAGE = filesDir
    }

    companion object {
        lateinit var INTERNAL_STORAGE: File
            private set
    }
}