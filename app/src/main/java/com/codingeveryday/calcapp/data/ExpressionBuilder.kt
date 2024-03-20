package com.codingeveryday.calcapp.data

import com.codingeveryday.calcapp.domain.entities.Expression
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.isOperation
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import javax.inject.Inject

class ExpressionBuilder @Inject constructor(): ExpressionBuilderInterface {

    private var builder = StringBuilder()
    override fun addBracket(br: Char) {
        if (builder.isEmpty() || isOperation(builder.last()))
            builder.append('(')
        else {
            val brSeq = correctBracketSequence()
            if (brSeq && builder.last() in Expression.DIGITS)
                builder.append("${Expression.operation[Expression.MUL_ID]}(")
            else if (!brSeq && builder.last() in Expression.CLOSING_BRACKETS + Expression.DIGITS)
                builder.append(')')
        }
    }

    private fun correctBracketSequence(): Boolean {

    }

    override fun addFunction(name: String) {
        if (builder.isEmpty() || isOperation(builder.last()))
            builder.append("$name(")
        else if (builder.last() in Expression.CLOSING_BRACKETS + Expression.DIGITS)
            builder.append("${Expression.operation[Expression.MUL_ID]}$name(")
    }

    override fun addDigit(d: Char) {
        if (builder.isNotEmpty() && builder[builder.length - 1] in Expression.CLOSING_BRACKETS)
            builder.append(Expression.operation[Expression.MUL_ID])
        builder.append(d)
    }

    override fun addOperation(op: Char) {
        if (builder.isEmpty()) {
            builder.append(op)
            return
        }
        if (builder[builder.length - 1] in Expression.OPERATIONS)
            return
        if (builder[builder.length - 1] in Expression.OPENING_BRACKETS)
            return
        builder.append(op)
    }

    override fun backspace() {
        if (builder.isEmpty())
            return
        if (builder[builder.length - 1] in 'a'..'z') {
            do {
                builder.deleteAt(builder.length - 1)
            } while (builder.isNotEmpty() && builder[builder.length - 1] in 'a'..'z')
        } else
            builder.deleteCharAt(builder.length - 1)
    }

    override fun clear() {
        builder.clear()
    }

    override fun addAbs() {
        if (builder.isNotEmpty() && builder[builder.length - 1] in Expression.CLOSING_BRACKETS)
            builder.append(Expression.operation[Expression.MUL_ID])
        builder.append('|')
    }

    override fun addConstant(value: String) {
        if (builder.isNotEmpty() && builder[builder.length - 1] !in Expression.OPENING_BRACKETS)
            builder.append(Expression.operation[Expression.MUL_ID])
        builder.append(value)
    }

    override fun addPoint() {
        if (builder.isEmpty() || builder[builder.length - 1] !in Expression.DIGITS)
            addDigit('0')
        builder.append(Expression.POINT)
    }

    override fun get(): String {
        return builder.toString()
    }

    override fun setExpression(expr: String) {
        builder = StringBuilder(expr)
    }
}