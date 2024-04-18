package com.codingeveryday.calcapp

import android.app.Application
import com.codingeveryday.calcapp.data.ExpressionBuilder
import com.codingeveryday.calcapp.di.DaggerApplicationComponent

class CalculatorApplication: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this, ExpressionBuilder())
    }
}