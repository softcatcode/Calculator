package com.codingeveryday.calcapp.domain.interfaces;

import com.codingeveryday.calcapp.domain.entities.AngleUnit

interface CalculationInterface {

    fun calculateValue(expr: String, base: Int, angleUnit: AngleUnit = AngleUnit.Radians): Pair<String, String>

    companion object {
        const val SQRT = "sqrt"
        const val SIN = "sin"
        const val COS = "cos"
        const val TG = "tg"
        const val CTG = "ctg"
        const val RAD = "rad"
        const val DEG = "deg"
    }
}
