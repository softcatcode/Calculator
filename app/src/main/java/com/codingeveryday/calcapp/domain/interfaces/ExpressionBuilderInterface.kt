package com.codingeveryday.calcapp.domain.interfaces

interface ExpressionBuilderInterface {
    fun addBracket(br: Char)

    fun addFunction(name: String)

    fun addDigit(d: Char)

    fun addOperation(op: Char)

    fun backspace()

    fun clear()

    fun addAbs()

    fun addConstant(value: String)

    fun addPoint()

    fun get(): String

    fun setExpression(expr: String)

    companion object {
        const val PI = "П"
    }
}