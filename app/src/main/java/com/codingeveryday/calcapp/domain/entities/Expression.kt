package com.codingeveryday.calcapp.domain.entities

import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.SUM
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.SUB
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.MUL
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.DIV
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.FAC
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.PI
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.POW
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.SQRT

open class Expression {
    companion object {
        const val SUM_ID = 1
        const val SUB_ID = 2
        const val DIV_ID = 3
        const val MUL_ID = 4
        const val SIN_ID = 5
        const val COS_ID = 6
        const val TAN_ID = 7
        const val CTG_ID = 8
        const val MOD_ID = 9
        const val LOG_ID = 10
        const val FAC_ID = 11
        const val POW_ID = 12
        const val ABS_ID = 13
        const val INT_ID = 14
        const val FR_ID = 15
        const val SQRT_ID = 16

        const val OPERATIONS = "$SUM$SUB$MUL$DIV$FAC$POW$SQRT"
        const val CONSTANTS = "$PI"
        const val OPENING_BRACKETS = "([{<"
        const val CLOSING_BRACKETS = ")]}>"
        const val DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWYZ"

        val operationId = mapOf(
            SUM to SUM_ID,
            SUB to SUB_ID,
            MUL to MUL_ID,
            DIV to DIV_ID,
            FAC to FAC_ID,
            POW to POW_ID,
            SQRT to SQRT_ID
        )

        val operation = mapOf(
            SUM_ID to SUM,
            SUB_ID to SUB,
            MUL_ID to MUL,
            DIV_ID to DIV,
            FAC_ID to FAC,
            POW_ID to POW,
            SQRT_ID to SQRT
        )

        val funcId = mapOf(
            "sin" to SIN_ID,
            "cos" to COS_ID,
            "tg" to TAN_ID,
            "tan" to TAN_ID,
            "ctg" to CTG_ID,
            "log" to LOG_ID
        )

        val unaryOperations = listOf(COS_ID, SIN_ID, TAN_ID, CTG_ID, FAC_ID, SQRT_ID, LOG_ID)
        val binaryOperations = listOf(SUM_ID, SUB_ID, POW_ID, MUL_ID, DIV_ID, MOD_ID)

        fun isOperation(c: Char) = operationId.keys.contains(c)

        fun matchingBrackets(a: Char, b: Char) =
            OPENING_BRACKETS.indexOf(a) == CLOSING_BRACKETS.indexOf(b)

        fun digit(d: Char) = DIGITS.indexOf(d)
    }
}

data class UnaryOperation(
    val operand: Expression,
    val id: Int
): Expression()

data class BinaryOperation(
    val first: Expression,
    val second: Expression,
    val id: Int
): Expression()