package com.codingeveryday.calcapp.data

import com.codingeveryday.calcapp.domain.entities.Expression
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.CLOSING_BRACKETS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.DIGITS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.OPENING_BRACKETS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.OPERATIONS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.isOperation
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.matchingBrackets
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.operation
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.POINT
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.processAbs
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import java.util.Stack
import javax.inject.Inject

class ExpressionBuilder @Inject constructor(): ExpressionBuilderInterface {

    private var builder = StringBuilder()
    override fun addBracket(br: Char) {
        val s = processAbs(builder.toString())
        if (s.isEmpty() || isOperation(s.last()))
            builder.append('(')
        else if (s.last() in DIGITS + CLOSING_BRACKETS) {
            if (correctBracketSequence())
                builder.append("${CalculationInterface.MUL}(")
            else
                builder.append(')')
        }
    }

    private fun correctBracketSequence(): Boolean {
        val stack = Stack<Char>()
        val seq = processAbs(builder.toString())
        for (c in seq) {
            if (c in OPENING_BRACKETS)
                stack.add(c)
            else if (c in CLOSING_BRACKETS) {
                if (stack.empty() || !matchingBrackets(stack.pop(), c))
                    return false
            }
        }
        return stack.empty()
    }

    override fun addFunction(name: String) {
        val s = processAbs(builder.toString())
        if (s.isEmpty() || s.last() in OPERATIONS || s.last() in OPENING_BRACKETS)
            builder.append("$name(")
        else if (s.last() in CLOSING_BRACKETS + DIGITS)
            builder.append("${CalculationInterface.MUL}$name(")
    }

    override fun addDigit(d: Char) {
        val s = processAbs(builder.toString())
        if (s.isNotEmpty() && s.last() in CLOSING_BRACKETS)
            builder.append(operation[Expression.MUL_ID])
        builder.append(d)
    }

    override fun addOperation(op: Char) {
        val s = processAbs(builder.toString())
        if (s.isEmpty())
            return
        if (
            s.last() in CLOSING_BRACKETS + DIGITS ||
            s.last() in OPENING_BRACKETS && op == CalculationInterface.SUB
        ) {
            builder.append(op)
        }
    }

    override fun backspace() {
        if (builder.isEmpty())
            return
        if (builder.last() in 'a'..'z') {
            do {
                builder.deleteAt(builder.lastIndex)
            } while (builder.isNotEmpty() && builder.last() in 'a'..'z')
        } else
            builder.deleteCharAt(builder.lastIndex)
    }

    override fun clear() {
        builder.clear()
    }

    override fun addAbs() {
        addBracket('<')
        if (builder.isNotEmpty() && builder.last() == '<')
            builder[builder.lastIndex] = '|'
    }

    override fun addConstant(value: String) {
        val s = processAbs(builder.toString())
        if (s.isNotEmpty() && s.last() in CLOSING_BRACKETS + DIGITS)
            builder.append(operation[Expression.MUL_ID])
        builder.append(value)
    }

    override fun addPoint() {
        var i = builder.lastIndex
        while (i >= 0 && builder[i] in DIGITS)
            --i
        if (i >= 0 && builder[i] == POINT)
            builder.append("${CalculationInterface.MUL}")
        if (builder.isEmpty() || builder.last() !in DIGITS)
            addDigit('0')
        builder.append(POINT)
    }

    override fun get() = builder.toString()

    override fun setExpression(expr: String) {
        builder = StringBuilder(expr)
    }
}