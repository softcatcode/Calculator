@file:Suppress("NAME_SHADOWING")

package com.codingeveryday.calcapp.data

import com.codingeveryday.calcapp.domain.entities.BinaryOperation
import com.codingeveryday.calcapp.domain.entities.Expression
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.CLOSING_BRACKETS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.CONSTANTS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.DIGITS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.OPENING_BRACKETS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.OPERATIONS
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.postfixUnary
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.prefixUnary
import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.domain.entities.UnaryOperation
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.ConstantInterpreterInterface
import com.codingeveryday.calcapp.domain.interfaces.ParseExpressionInterface
import java.util.Stack
import javax.inject.Inject

class ExpressionParser @Inject constructor(
    private val constantInterpreter: ConstantInterpreterInterface
): ParseExpressionInterface {

    private data class ParseObject(
        var expr: Expression? = null,
        var operationId: Int? = null
    )

    private enum class ParseElemType {
        Number, UnaryOperation, BinaryOperation, Function, Constant, Undefined
    }

    private val isNumberPiece: (Char) -> Boolean = { c -> c in DIGITS || c == '.' || c == ',' }
    private val isFuncPiece: (Char) -> Boolean = { c -> c in 'a'..'z' }
    private val isOperation: (Char) -> Boolean = { c -> c in OPERATIONS }
    private val isConstant: (Char) -> Boolean = { c -> c in CONSTANTS }
    private val bracketSequenceException = Exception("Illegal bracket sequence")

    private fun takeNextElem(expr: String, i: Int): Pair<String, ParseElemType> {
        var result = ""
        var resType = ParseElemType.Undefined
        if (isOperation(expr[i])) {
            val type = if (expr[i] == CalculationInterface.SUB) {
                if (i == 0 || expr[i - 1] in OPENING_BRACKETS)
                    ParseElemType.UnaryOperation
                else
                    ParseElemType.BinaryOperation
            } else if (expr[i] in Expression.unaryOperationId)
                ParseElemType.UnaryOperation
            else
                ParseElemType.BinaryOperation
            return "${expr[i]}" to type
        }
        val callbackList = listOf(isNumberPiece, isFuncPiece, isConstant)
        for (callback in callbackList) {
            if (callback(expr[i])) {
                resType = when (callback) {
                    isNumberPiece -> ParseElemType.Number
                    isFuncPiece -> ParseElemType.Function
                    isConstant -> ParseElemType.Constant
                    else -> ParseElemType.Undefined
                }
                var j = i + 1
                while (j < expr.length && callback(expr[j]))
                    ++j
                result += expr.substring(i, j)
                break
            }
        }
        return result to resType
    }

    private fun withoutSpaces(str: String): String {
        val sb = StringBuilder()
        for (c in str)
            if (c != ' ')
                sb.append(c)
        return sb.toString()
    }

    private fun fetchSubExpr(expr: String, startIndex: Int): String {
        if (expr[startIndex] !in OPENING_BRACKETS)
            return ""
        val stack = Stack<Char>()
        stack.add(expr[startIndex])
        var i = startIndex + 1
        while (i < expr.length && stack.isNotEmpty()) {
            val br = expr[i++]
            if (br in OPENING_BRACKETS)
                stack.add(br)
            else if (br in CLOSING_BRACKETS) {
                if (stack.empty() || !Expression.matchingBrackets(stack.pop(), br))
                    throw bracketSequenceException
            }
        }
        if (stack.empty())
            return expr.substring(startIndex + 1, i - 1)
        throw bracketSequenceException
    }

    private fun getParseObjects(expr: String, base: Int): MutableList<ParseObject> {
        val list = mutableListOf<ParseObject>()
        var i = 0
        val expr = withoutSpaces(expr)
        while (i < expr.length) {
            if (expr[i] in OPENING_BRACKETS) {
                val subExpr = fetchSubExpr(expr, i)
                val elem = when (expr[i]) {
                    '(' -> ParseObject(parseExpression(subExpr, base))
                    '[' -> ParseObject(UnaryOperation(parseExpression(subExpr, base), Expression.INT_ID))
                    '{' -> ParseObject(UnaryOperation(parseExpression(subExpr, base), Expression.FR_ID))
                    '<' -> ParseObject(UnaryOperation(parseExpression(subExpr, base), Expression.ABS_ID))
                    else -> throw bracketSequenceException
                }
                list.add(elem)
                i += subExpr.length + 2
            } else {
                val elem = takeNextElem(expr, i)
                val token = elem.first
                val type = elem.second
                when (type) {
                    ParseElemType.Number ->
                        list.add( ParseObject(expr = Number(token, base)) )
                    ParseElemType.Constant ->
                        list.add( ParseObject(expr = constantInterpreter.decode(token[0], base)) )
                    ParseElemType.UnaryOperation ->
                        list.add( ParseObject(operationId = Expression.unaryOperationId[token[0]]) )
                    ParseElemType.BinaryOperation ->
                        list.add( ParseObject(operationId = Expression.binaryOperationId[token[0]]) )
                    ParseElemType.Function ->
                        list.add( ParseObject(operationId = Expression.funcId[token]) )
                    else -> { i = expr.length }
                }
                i += token.length
            }
        }
        return list
    }

    private fun parseUnaryOperations(list: MutableList<ParseObject>) {
        var i = 0
        while (i < list.size) {
            val id = list[i].operationId
            if (prefixUnary(id)) {
                list[i] = ParseObject(expr = UnaryOperation(list[i + 1].expr!!, list[i].operationId!!))
                list.removeAt(i + 1)
            } else if (postfixUnary(id)) {
                list[i] = ParseObject(expr = UnaryOperation(list[i - 1].expr!!, list[i].operationId!!))
                list.removeAt(i - 1)
            }
            ++i
        }
    }

    private fun parseBinaryOperations(list: MutableList<ParseObject>, operations: List<Int>) {
        var i = 1
        val lastIndex = list.size - 2
        while (i <= lastIndex) {
            if (list[i].operationId in operations) {
                list[i] = ParseObject(
                    BinaryOperation(list[i - 1].expr!!, list[i + 1].expr!!, list[i].operationId!!),
                    null
                )
                list.removeAt(i - 1)
                list.removeAt(i)
            }
            ++i
        }
    }

    override fun parseExpression(expr: String, base: Int): Expression {
        val list = getParseObjects(expr, base)
        parseUnaryOperations(list)
        with(Expression) {
            parseBinaryOperations(list, listOf(POW_ID))
            parseBinaryOperations(list, listOf(DIV_ID, MUL_ID, MOD_ID))
            parseBinaryOperations(list, listOf(SUM_ID, SUB_ID))
        }
        return list[0].expr!!
    }
}