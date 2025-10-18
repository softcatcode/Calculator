package com.softcat.data.implementations

import com.softcat.domain.entities.BracketType
import com.softcat.domain.entities.Expression
import com.softcat.domain.entities.Expression.Companion.CLOSING_BRACKETS
import com.softcat.domain.entities.Expression.Companion.CONSTANTS
import com.softcat.domain.entities.Expression.Companion.DIGITS
import com.softcat.domain.entities.Expression.Companion.FUNC_LETTERS
import com.softcat.domain.entities.Expression.Companion.OPENING_BRACKETS
import com.softcat.domain.entities.Expression.Companion.OPERATIONS
import com.softcat.domain.entities.Expression.Companion.binaryOperationId
import com.softcat.domain.entities.Expression.Companion.matchingBrackets
import com.softcat.domain.entities.Expression.Companion.operation
import com.softcat.domain.entities.Expression.Companion.postfixUnary
import com.softcat.domain.entities.Expression.Companion.unaryOperationId
import com.softcat.domain.entities.bracketType
import com.softcat.domain.entities.closingBracket
import com.softcat.domain.entities.openingBracket
import com.softcat.domain.interfaces.CalculationInterface
import com.softcat.domain.interfaces.CalculationInterface.Companion.FAC
import com.softcat.domain.interfaces.CalculationInterface.Companion.MUL
import com.softcat.domain.interfaces.CalculationInterface.Companion.POINT
import com.softcat.domain.interfaces.ExpressionBuilderInterface
import java.util.Stack
import javax.inject.Inject
import kotlin.text.iterator

class ExpressionBuilder @Inject constructor(): ExpressionBuilderInterface {

    private var builder = StringBuilder()
    override fun addBracket(type: BracketType): ExpressionBuilder {
        val open = openingBracket(type)
        val close = closingBracket(type)
        if (builder.isEmpty()) {
            builder.append(open)
            return this
        }
        val last = builder.last()
        if (last == POINT)
            return this
        if (last in FUNC_LETTERS + OPENING_BRACKETS) {
            builder.append(open)
            return this
        }
        if (last in OPERATIONS) {
            val id = binaryOperationId[last] ?: unaryOperationId[last]
            if (!postfixUnary(id)) {
                builder.append(open)
                return this
            }
        }
        if (correctBracketSequence()) {
            builder.append("$MUL$open")
            return this
        }
        var balance = 0
        var i = builder.lastIndex
        while (balance >= 0) {
            if (builder[i] in CLOSING_BRACKETS)
                ++balance
            else if (builder[i] in OPENING_BRACKETS)
                --balance
            --i
        }
        if (bracketType(builder[i + 1]) != type)
            builder.append("$MUL$open")
        else
            builder.append(close)
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
            builder.append("${MUL}$name(")
        return this
    }

    override fun addDigit(d: Char): ExpressionBuilder {
        if (d !in DIGITS)
            return this
        if (builder.isNotEmpty() && builder.last() in CLOSING_BRACKETS + CONSTANTS)
            builder.append(operation[Expression.MUL_ID])
        builder.append(d)
        return this
    }

    override fun addOperation(op: Char): ExpressionBuilder {
        if (op !in OPERATIONS)
            return this
        if (builder.isEmpty()) {
            if (op == CalculationInterface.SUB)
                builder.append(op)
            return this
        }
        if (op == FAC) {
            if (builder.last() in CLOSING_BRACKETS + DIGITS + CONSTANTS)
                builder.append(op)
            return this
        }
        if (
            builder.last() in CLOSING_BRACKETS + DIGITS + CONSTANTS + FAC ||
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
        if (builder.isNotEmpty() && builder.last() == POINT)
            return this
        var i = builder.lastIndex
        while (i >= 0 && builder[i] in DIGITS)
            --i
        if (i >= 0 && (builder[i] == POINT || builder.last() in CONSTANTS))
            builder.append(MUL)
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