package com.codingeveryday.calcapp.domain.interfaces;

import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.Expression

interface CalculationInterface {

    fun calculateValue(expr: String, base: Int, angleUnit: AngleUnit = AngleUnit.Radians): Pair<String, String>

    companion object {
        const val ABS = '|'
        const val POINT = '.'
        const val MUL = '×'
        const val DIV = '/'
        const val SUM = '+'
        const val SUB = '-'
        const val FAC = '!'
        const val POW = '^'
        const val SQRT = "√"
        const val SIN = "sin"
        const val COS = "cos"
        const val TG = "tg"
        const val CTG = "ctg"
        const val RAD = "rad"
        const val DEG = "deg"

        enum class BracketType { Round, Square, Curly, Triangle }

        fun openingBracket(type: BracketType) = when (type) {
            BracketType.Round -> '('
            BracketType.Curly -> '{'
            BracketType.Square -> '['
            BracketType.Triangle -> '<'
        }

        fun closingBracket(type: BracketType) = when (type) {
            BracketType.Round -> ')'
            BracketType.Curly -> '}'
            BracketType.Square -> ']'
            BracketType.Triangle -> '>'
        }

        fun processAbs(s: String): String {
            val sb = StringBuilder()
            for (i in s.indices) {
                if (s[i] != '|')
                    sb.append(s[i])
                else if (s[i - 1] in Expression.DIGITS + Expression.CLOSING_BRACKETS)
                    sb.append('>')
                else
                    sb.append('<')
            }
            return sb.toString()
        }
    }
}
