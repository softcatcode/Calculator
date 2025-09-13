package com.codingeveryday.calcapp.domain.interfaces

import com.codingeveryday.calcapp.domain.entities.AngleUnit

interface CalculationInterface {

    fun calculateValue(expr: String, base: Int, angleUnit: AngleUnit = AngleUnit.Radians): String

    companion object {
        const val ABS = '|'
        const val POINT = '.'
        const val MUL = '×'
        const val DIV = '/'
        const val SUM = '+'
        const val SUB = '-'
        const val FAC = '!'
        const val POW = '^'
        const val SQRT = '√'
        const val SIN = "sin"
        const val COS = "cos"
        const val TAN = "tg"
        const val CTG = "ctg"
        const val RAD = "rad"
        const val DEG = "deg"
        const val PI = 'π'
        const val LOG = "log₂"
        const val LN = "ln"
    }
}
