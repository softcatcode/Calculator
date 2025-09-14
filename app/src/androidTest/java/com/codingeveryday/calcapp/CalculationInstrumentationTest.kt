package com.codingeveryday.calcapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class CalculationInstrumentationTest {

    private val application = InstrumentationRegistry
        .getInstrumentation()
        .targetContext
        .applicationContext as CalculatorApplication

    private val component = DaggerTestingComponent.factory().create(application)
    private val calculator = component.getCalculationImplementation()


    @Test
    fun calculateSimpleExpr() {
        val expr = "((23+2)/2-0.5)×3!"
        val base = 10

        val result = calculator.calculateValue(expr, base)

        assertEquals(result, "72")
    }

    @Test
    fun calculateEmptyExpr() {
        val expr = ""
        val base = 10

        try {
            calculator.calculateValue(expr, base)
            assert(false)
        } catch(_: Exception) {}
    }
}