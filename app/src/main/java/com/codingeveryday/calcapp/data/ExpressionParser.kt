@file:Suppress("NAME_SHADOWING")

package com.codingeveryday.calcapp.data

import com.codingeveryday.calcapp.domain.entities.BinaryOperation
import com.codingeveryday.calcapp.domain.entities.Expression
import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.domain.entities.UnaryOperation
import com.codingeveryday.calcapp.domain.interfaces.ParseExpressionInterface
import java.util.Stack
import javax.inject.Inject

class ExpressionParser @Inject constructor(): ParseExpressionInterface {

    private data class ParseObject(
        var expr: Expression? = null,
        var operationId: Int? = null
    )

    private val isNumberPiece: (Char) -> Boolean = { c -> c in Expression.DIGITS || c == '.' || c == ',' }
    val isFuncPiece: (Char) -> Boolean = { c -> c in 'a'..'z' }
    private val isOperation: (Char) -> Boolean = { c -> c in Expression.OPERATIONS }
    private val callbackList = listOf(isNumberPiece, isOperation, isFuncPiece)
    private val bracketSequenceException = Exception("Illegal bracket sequence")

    private val takeNextElem: (String, Int) -> String = { expr, i ->
        var result = ""
        for (callback in callbackList) {
            if (callback(expr[i])) {
                var j = i + 1
                while (j < expr.length && callback(expr[j]))
                    ++j
                result = expr.substring(i, j)
                break
            }
        }
        result
    }

    private fun withoutSpaces(str: String): String {
        val sb = StringBuilder()
        for (c in str)
            if (c != ' ')
                sb.append(c)
        return sb.toString()
    }

    private fun fetchSubExpr(expr: String, startIndex: Int): String {
        if (expr[startIndex] !in Expression.OPENING_BRACKETS)
            return ""
        val stack = Stack<Char>()
        stack.add(expr[startIndex])
        var i = startIndex + 1
        while (i < expr.length) {
            val br = expr[i++]
            if (br in Expression.OPENING_BRACKETS)
                stack.add(br)
            else if (br in Expression.CLOSING_BRACKETS) {
                if (stack.empty() || !Expression.matchingBrackets(stack.pop(), br))
                    throw bracketSequenceException
            }
        }
        if (stack.empty())
            return expr.substring(startIndex + 1, i - startIndex - 1)
        throw bracketSequenceException
    }

    private fun getParseObjects(expr: String, base: Int): MutableList<ParseObject> {
        val list = mutableListOf<ParseObject>()
        var i = 0
        val expr = withoutSpaces(expr)
        while (i < expr.length) {
            if (expr[i] in Expression.OPENING_BRACKETS) {
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
                if (elem == "")
                    i = expr.length
                else if (isNumberPiece(elem[0]))
                    list.add( ParseObject(Number(elem, base)) )
                else if (isFuncPiece(elem[0]))
                    list.add( ParseObject(operationId = Expression.funcId[elem]!!) )
                else if (isOperation(elem[0]))
                    list.add( ParseObject(operationId = Expression.operationId[ elem[0] ]) )
                i += elem.length
            }
        }
        return list
    }

    private fun parseUnaryOperations(list: MutableList<ParseObject>) {
        var i = 1
        while (i < list.size) {
            if (list[i].operationId in Expression.unaryOperations) {
                list[i] = ParseObject(UnaryOperation(list[i + 1].expr!!, list[i].operationId!!), null)
                list.removeAt(i + 1)
            }
            ++i
        }
    }

    private fun parseBinaryOperations(list: MutableList<ParseObject>, operations: List<Int>) {
        var i = list.size - 2
        while (i >= 1) {
            if (list[i].operationId in operations) {
                list[i] = ParseObject(
                    BinaryOperation(list[i - 1].expr!!, list[i + 1].expr!!, list[i].operationId!!),
                    null
                )
                list.removeAt(i - 1)
                list.removeAt(i)
            }
            --i
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