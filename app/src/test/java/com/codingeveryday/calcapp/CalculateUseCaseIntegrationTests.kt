package com.codingeveryday.calcapp

import com.softcat.data.core.implementations.ConstantInterpreter
import com.softcat.data.core.implementations.ConstantProvider
import com.softcat.data.core.implementations.ExpressionParser
import com.softcat.data.core.implementations.MathImplementation
import com.softcat.data.implementations.CalculationImplementation
import com.softcat.domain.useCases.CalculateUseCase
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CalculateUseCaseIntegrationTests {

    private val constantProvider = ConstantProvider()
    private val mathImpl = MathImplementation(constantProvider)
    private val calculator = CalculationImplementation(
        mathImpl,
        ExpressionParser(ConstantInterpreter(constantProvider))
    )
    private val calculateUseCase = CalculateUseCase(calculator)

    private val expressions = listOf(
        "23+5/2" to "25.5",
        "sin(π)-π×cos(π/2)" to "0",
        "1+√(10!/70)" to "721",
        "<<1/3+(-4/3)>-<4×ln(1)-2>>" to "1",
        "0.5×(1/2+1/2-12/3)" to "-1.5",
        "0.625 + 0.375 - 0.1×sin(3×π/2)" to "1.1",
        "400 / 10^3 - 5! + ln(1)" to "-119.6",
        //"sin(57129)^2 + cos(57129)^2" to "1",
        //"sin(0.6×π) - 2 × sin(0.3×π) × cos(0.3×π)" to "0",
    )

    @Test
    fun calculateExpression1() {
        val (expr, answer) = expressions[0]
        val result = calculateUseCase(expr, 10)
        assertEquals(result, answer)
    }

    @Test
    fun calculateExpression2() {
        val (expr, answer) = expressions[1]
        val result = calculateUseCase(expr, 10)
        assertEquals(result, answer)
    }

    @Test
    fun calculateExpression3() {
        val (expr, answer) = expressions[2]
        val result = calculateUseCase(expr, 10)
        assertEquals(result, answer)
    }

    @Test
    fun calculateExpression4() {
        val (expr, answer) = expressions[3]
        val result = calculateUseCase(expr, 10)
        assertEquals(result, answer)
    }

    @Test
    fun calculateExpression5() {
        val (expr, answer) = expressions[4]
        val result = calculateUseCase(expr, 10)
        assertEquals(result, answer)
    }

    @Test
    fun calculateExpression6() {
        val (expr, answer) = expressions[5]
        val result = calculateUseCase(expr, 10)
        assertEquals(result, answer)
    }

    @Test
    fun calculateExpression7() {
        val (expr, answer) = expressions[6]
        val result = calculateUseCase(expr, 10)
        assertEquals(result, answer)
    }
}