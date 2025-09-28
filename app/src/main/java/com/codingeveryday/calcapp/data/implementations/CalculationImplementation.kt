package com.codingeveryday.calcapp.data.implementations

import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.BinaryOperation
import com.codingeveryday.calcapp.domain.entities.Expression
import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.domain.entities.UnaryOperation
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.data.core.interfaces.MathInterface
import com.codingeveryday.calcapp.data.core.interfaces.ParseExpressionInterface
import timber.log.Timber
import javax.inject.Inject

class CalculationImplementation @Inject constructor(
    private val calculator: MathInterface,
    private val parser: ParseExpressionInterface
): CalculationInterface {

    private var angleUnit = AngleUnit.Radians

    private fun applyOperation(id: Int, first: Number, second: Number? = null): Number {
        Timber.i("${this::class.simpleName}.applyOperation($id, $first, $second")
        return when (id) {
            Expression.SIN_ID -> calculator.sin(first, angleUnit = angleUnit)
            Expression.COS_ID -> calculator.cos(first, angleUnit = angleUnit)
            Expression.TAN_ID -> calculator.tan(first, angleUnit = angleUnit)
            Expression.CTG_ID -> calculator.ctg(first, angleUnit = angleUnit)
            Expression.FAC_ID -> calculator.fac(first)
            Expression.ABS_ID -> calculator.abs(first)
            Expression.INT_ID -> calculator.intPart(first)
            Expression.FR_ID -> calculator.fractionPart(first)
            Expression.SQRT_ID -> calculator.sqrt(first)
            Expression.SUM_ID -> calculator.sum(first, second!!)
            Expression.SUB_ID -> calculator.sub(first, second!!)
            Expression.MUL_ID -> calculator.mul(first, second!!)
            Expression.DIV_ID -> calculator.div(first, second!!)
            Expression.MOD_ID -> calculator.mod(first, second!!)
            Expression.POW_ID -> calculator.pow(first, second!!)
            Expression.MINUS_ID -> calculator.minus(first)
            Expression.LOG_ID -> calculator.log(first, second!!)
            Expression.LN_ID -> calculator.ln(first)
            else -> throw Exception("Unknown operation: $id")
        }
    }

    private fun calculate(expr: Expression): Number {
        return when (expr) {
            is UnaryOperation -> applyOperation(expr.id, calculate(expr.operand))
            is BinaryOperation -> applyOperation(expr.id, calculate(expr.first), calculate(expr.second))
            else -> expr as Number
        }
    }

    override fun calculateValue(expr: String, base: Int, angleUnit: AngleUnit): String {
        Timber.i("${this::class.simpleName}.calculateValue($expr, $base, $angleUnit")
        val expression = parser.parseExpression(expr, base)
        this.angleUnit = angleUnit
        return calculate(expression).toString()
    }
}