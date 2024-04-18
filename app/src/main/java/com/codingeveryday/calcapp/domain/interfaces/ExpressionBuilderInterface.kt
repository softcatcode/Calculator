package com.codingeveryday.calcapp.domain.interfaces

import com.codingeveryday.calcapp.data.implementations.ExpressionBuilder
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.BracketType

interface ExpressionBuilderInterface {
    fun addBracket(bracketType: BracketType): ExpressionBuilder

    fun addFunction(name: String): ExpressionBuilder

    fun addDigit(d: Char): ExpressionBuilder

    fun addOperation(op: Char): ExpressionBuilder

    fun backspace(): ExpressionBuilder

    fun clear(): ExpressionBuilder

    fun addAbs(): ExpressionBuilder

    fun addConstant(value: String): ExpressionBuilder

    fun addPoint(): ExpressionBuilder

    fun setExpression(expr: String): ExpressionBuilder

    fun get(): String
}