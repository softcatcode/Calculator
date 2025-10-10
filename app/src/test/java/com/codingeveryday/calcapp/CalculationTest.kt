package com.codingeveryday.calcapp

import com.softcat.data.core.implementations.ExpressionParser
import com.softcat.data.core.interfaces.MathInterface
import com.softcat.data.implementations.CalculationImplementation
import com.softcat.domain.entities.BinaryOperation
import com.softcat.domain.entities.Expression
import com.softcat.domain.entities.UnaryOperation
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.random.Random
import com.softcat.domain.entities.Number

class CalculationTest {


    private val calculator = mock(MathInterface::class.java)
    private val parser = mock(ExpressionParser::class.java)
    private val calculationImpl = CalculationImplementation(calculator, parser)

    @Before
    fun resetMocks() {
        reset(calculator)
        reset(parser)
    }

    @Test
    fun calculateSimpleExpr() {
        val expr = "((23+2)/2-0.5)×3!"
        val base = 10
        doReturn(
            BinaryOperation(
                BinaryOperation(
                    BinaryOperation(
                        BinaryOperation(
                            Number("23", base),
                            Number("2", base),
                            Expression.SUM_ID
                        ),
                        Number("2", base),
                        Expression.DIV_ID
                    ),
                    Number("0.5", base),
                    Expression.SUB_ID
                ),
                UnaryOperation(
                    Number("3", base),
                    Expression.FAC_ID
                ),
                Expression.MUL_ID
            )
        ).whenever(parser)
            .parseExpression(anyString(), anyInt())
        doReturn(Number("25", base))
            .whenever(calculator)
            .sum(any(), any())
        doReturn(Number("12.5", base))
            .whenever(calculator)
            .div(any(), any())
        doReturn(Number("12", base))
            .whenever(calculator)
            .sub(any(), any())
        doReturn(Number("72", base))
            .whenever(calculator)
            .mul(any(), any())
        doReturn(Number("6", base))
            .whenever(calculator)
            .fac(any())

        val result = calculationImpl.calculateValue(expr, base)

        assert(result == Number("72", base).toString())
        verify(parser, times(1))
            .parseExpression(expr, base)
        verify(calculator, times(1))
            .sum(any(), any())
        verify(calculator, times(1))
            .sub(any(), any())
        verify(calculator, times(1))
            .mul(any(), any())
        verify(calculator, times(1))
            .div(any(), any())
        verify(calculator, times(1))
            .fac(any())
        verify(calculator, times(0))
            .mod(any(), any())
        verify(calculator, times(0))
            .pow(any(), any())
        verify(calculator, times(0))
            .sin(any(), any())
        verify(calculator, times(0))
            .cos(any(), any())
        verify(calculator, times(0))
            .tan(any(), any())
        verify(calculator, times(0))
            .ctg(any(), any())
        verify(calculator, times(0))
            .abs(any())
        verify(calculator, times(0))
            .log(any(), any())
        verify(calculator, times(0))
            .ln(any())
        verify(calculator, times(0))
            .sqrt(any())
        verify(calculator, times(0))
            .minus(any())
        verify(calculator, times(0))
            .intPart(any())
        verify(calculator, times(0))
            .fractionPart(any())
    }

    @Test
    fun throwExceptionOnUnknownOperation() {
        val expr = "((23+2)/2-0.5)×3!"
        val base = 10
        doReturn(
            BinaryOperation(
                Number("1", base),
                Number("1", base),
                -Random.nextInt(1000, 10000)
            )
        ).whenever(parser)
            .parseExpression(anyString(), anyInt())

        try {
            calculationImpl.calculateValue(expr, base)
            assert(false)
        } catch (_: Exception) {}

        verify(parser, times(1))
            .parseExpression(expr, base)
        verify(calculator, times(0))
            .sum(any(), any())
        verify(calculator, times(0))
            .sub(any(), any())
        verify(calculator, times(0))
            .mul(any(), any())
        verify(calculator, times(0))
            .div(any(), any())
        verify(calculator, times(0))
            .mod(any(), any())
        verify(calculator, times(0))
            .pow(any(), any())
        verify(calculator, times(0))
            .sin(any(), any())
        verify(calculator, times(0))
            .cos(any(), any())
        verify(calculator, times(0))
            .tan(any(), any())
        verify(calculator, times(0))
            .ctg(any(), any())
        verify(calculator, times(0))
            .abs(any())
        verify(calculator, times(0))
            .log(any(), any())
        verify(calculator, times(0))
            .ln(any())
        verify(calculator, times(0))
            .sqrt(any())
        verify(calculator, times(0))
            .minus(any())
        verify(calculator, times(0))
            .intPart(any())
        verify(calculator, times(0))
            .fractionPart(any())
        verify(calculator, times(0))
            .fac(any())
    }
}