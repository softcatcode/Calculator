package com.softcat.domain.interfaces

import com.softcat.domain.entities.BracketType

interface ExpressionBuilderInterface {
    fun addBracket(type: BracketType): ExpressionBuilderInterface

    fun addFunction(name: String): ExpressionBuilderInterface

    fun addDigit(d: Char): ExpressionBuilderInterface

    fun addOperation(op: Char): ExpressionBuilderInterface

    fun backspace(): ExpressionBuilderInterface

    fun clear(): ExpressionBuilderInterface

    fun addAbs(): ExpressionBuilderInterface

    fun addConstant(value: String): ExpressionBuilderInterface

    fun addPoint(): ExpressionBuilderInterface

    fun setExpression(expr: String): ExpressionBuilderInterface

    fun get(): String
}