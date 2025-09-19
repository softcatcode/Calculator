package com.codingeveryday.calcapp

import com.codingeveryday.calcapp.data.core.implementations.ConstantInterpreter
import com.codingeveryday.calcapp.data.core.implementations.ConstantProvider
import com.codingeveryday.calcapp.data.core.implementations.ExpressionParser
import com.codingeveryday.calcapp.data.core.implementations.MathImplementation
import com.codingeveryday.calcapp.data.implementations.CalculationImplementation
import org.junit.Assert
import org.junit.Test

class CalculationInstrumentationTest {

    private val constantProvider = ConstantProvider()
    private val calculator = CalculationImplementation(
        MathImplementation(constantProvider),
        ExpressionParser(ConstantInterpreter(constantProvider))
    )


    @Test
    fun calculateSimpleExpr() {
        val expr = "((23+2)/2-0.5)×3!"
        val base = 10

        val result = calculator.calculateValue(expr, base)

        Assert.assertEquals(result, "72")
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