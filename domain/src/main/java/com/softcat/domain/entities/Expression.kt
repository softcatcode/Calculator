package com.softcat.domain.entities

import com.softcat.domain.interfaces.CalculationInterface.Companion.COS
import com.softcat.domain.interfaces.CalculationInterface.Companion.CTG
import com.softcat.domain.interfaces.CalculationInterface.Companion.DIV
import com.softcat.domain.interfaces.CalculationInterface.Companion.FAC
import com.softcat.domain.interfaces.CalculationInterface.Companion.LN
import com.softcat.domain.interfaces.CalculationInterface.Companion.LOG
import com.softcat.domain.interfaces.CalculationInterface.Companion.MUL
import com.softcat.domain.interfaces.CalculationInterface.Companion.POW
import com.softcat.domain.interfaces.CalculationInterface.Companion.SQRT
import com.softcat.domain.interfaces.CalculationInterface.Companion.SUB
import com.softcat.domain.interfaces.CalculationInterface.Companion.SUM
import com.softcat.domain.interfaces.CalculationInterface.Companion.TAN
import com.softcat.domain.interfaces.CalculationInterface.Companion.PI
import com.softcat.domain.interfaces.CalculationInterface.Companion.SIN

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
        const val LN_ID = 11
        const val FAC_ID = 12
        const val POW_ID = 13
        const val ABS_ID = 14
        const val INT_ID = 15
        const val FR_ID = 16
        const val SQRT_ID = 17
        const val MINUS_ID = 18

        const val OPERATIONS = "$SUM$SUB$MUL$DIV$FAC$POW$SQRT"
        const val CONSTANTS = "$PI"
        const val OPENING_BRACKETS = "([{<"
        const val CLOSING_BRACKETS = ")]}>"
        const val FUNC_LETTERS = "abcdefghijklmnopqrstuvwxyz₂"
        const val DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWYZ"

        val operation = mapOf(
            SUM_ID to SUM,
            SUB_ID to SUB,
            MUL_ID to MUL,
            DIV_ID to DIV,
            FAC_ID to FAC,
            POW_ID to POW,
            SQRT_ID to SQRT,
            MINUS_ID to SUB
        )

        val unaryOperationId = mapOf(
            SUB to MINUS_ID,
            SQRT to SQRT_ID,
            FAC to FAC_ID,
        )

        val binaryOperationId = mapOf(
            SUM to SUM_ID,
            SUB to SUB_ID,
            MUL to MUL_ID,
            DIV to DIV_ID,
            POW to POW_ID,
        )

        val funcId = mapOf(
            SIN to SIN_ID,
            COS to COS_ID,
            TAN to TAN_ID,
            CTG to CTG_ID,
            LOG to LOG_ID,
            LN to LN_ID
        )

        fun matchingBrackets(a: Char, b: Char) =
            OPENING_BRACKETS.indexOf(a) == CLOSING_BRACKETS.indexOf(b)

        fun postfixUnary(id: Int?) = (id == FAC_ID)
        fun prefixUnary(id: Int?) =
            (id in listOf(SIN_ID, COS_ID, TAN_ID, CTG_ID, MINUS_ID, SQRT_ID, LOG_ID, LN_ID))
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