package com.codingeveryday.calcapp.data

import com.codingeveryday.calcapp.domain.entities.Expression
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.CLOSING_BRACKETS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.CONSTANTS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.DIGITS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.OPENING_BRACKETS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.OPERATIONS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.matchingBrackets
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.operation
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.POINT
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.BracketType
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.closingBracket
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.openingBracket
import java.util.Stack
import javax.inject.Inject

class ExpressionBuilder @Inject constructor(): ExpressionBuilderInterface {

    private var builder = StringBuilder()
    override fun addBracket(bracketType: BracketType): ExpressionBuilder {
        val open = openingBracket(bracketType)
        val close = closingBracket(bracketType)
        if (builder.isEmpty() || builder.last() in OPERATIONS + OPENING_BRACKETS)
            builder.append(open)
        else if (builder.last() in DIGITS + CLOSING_BRACKETS + CONSTANTS) {
            if (correctBracketSequence())
                builder.append("${CalculationInterface.MUL}$open")
            else
                builder.append(close)
        }
        return this
    }

    private fun correctBracketSequence(): Boolean {
        val stack = Stack<Char>()
        for (c in builder) {
            if (c in OPENING_BRACKETS)
                stack.add(c)
            else if (c in CLOSING_BRACKETS) {
                if (stack.empty() || !matchingBrackets(stack.pop(), c))
                    return false
            }
        }
        return stack.empty()
    }

    override fun addFunction(name: String): ExpressionBuilder {
        if (builder.isEmpty() || builder.last() in OPERATIONS || builder.last() in OPENING_BRACKETS)
            builder.append("$name(")
        else if (builder.last() in CLOSING_BRACKETS + DIGITS + CONSTANTS)
            builder.append("${CalculationInterface.MUL}$name(")
        return this
    }

    override fun addDigit(d: Char): ExpressionBuilder {
        if (builder.isNotEmpty() && builder.last() in CLOSING_BRACKETS + CONSTANTS)
            builder.append(operation[Expression.MUL_ID])
        builder.append(d)
        return this
    }

    override fun addOperation(op: Char): ExpressionBuilder {
        if (builder.isEmpty()) {
            if (op == CalculationInterface.SUB)
                builder.append(op)
            return this
        }
        if (
            builder.last() in CLOSING_BRACKETS + DIGITS + CONSTANTS ||
            builder.last() in OPENING_BRACKETS && op == CalculationInterface.SUB
        ) {
            builder.append(op)
        }
        return this
    }

    override fun backspace(): ExpressionBuilder {
        if (builder.isEmpty())
            return this
        if (builder.last() in 'a'..'z') {
            do {
                builder.deleteAt(builder.lastIndex)
            } while (builder.isNotEmpty() && builder.last() in 'a'..'z')
        } else
            builder.deleteCharAt(builder.lastIndex)
        return this
    }

    override fun clear(): ExpressionBuilder {
        builder.clear()
        return this
    }

    override fun addAbs(): ExpressionBuilder {
        addBracket(BracketType.Triangle)
        return this
    }

    override fun addConstant(value: String): ExpressionBuilder {
        if (builder.isNotEmpty() && builder.last() in CLOSING_BRACKETS + DIGITS)
            builder.append(operation[Expression.MUL_ID])
        builder.append(value)
        return this
    }

    override fun addPoint(): ExpressionBuilder {
        var i = builder.lastIndex
        while (i >= 0 && builder[i] in DIGITS)
            --i
        if (i >= 0 && (builder[i] == POINT || builder.last() in CONSTANTS))
            builder.append("${CalculationInterface.MUL}")
        if (builder.isEmpty() || builder.last() !in DIGITS)
            addDigit('0')
        builder.append(POINT)
        return this
    }

    override fun get() = builder.toString()

    override fun setExpression(expr: String): ExpressionBuilder {
        builder = StringBuilder(expr)
        return this
    }
}