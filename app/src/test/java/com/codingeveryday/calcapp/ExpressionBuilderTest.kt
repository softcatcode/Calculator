package com.codingeveryday.calcapp

import com.codingeveryday.calcapp.TestDataGenerator.getRandomExpr
import com.codingeveryday.calcapp.data.implementations.ExpressionBuilder
import com.codingeveryday.calcapp.domain.entities.BracketType
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.PI
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import org.junit.Before
import org.junit.Test

class ExpressionBuilderTest {

    private lateinit var builder: ExpressionBuilderInterface

    @Before
    fun initBuilder() {
       builder = ExpressionBuilder()
    }

    @Test
    fun setSimpleExpression() {
        val expr = getRandomExpr()

        builder.setExpression(expr)
        val result = builder.get()

        assert(result == expr)
    }

    @Test
    fun setEmptyExpression() {
        val result = builder.get()

        assert(result == "")
    }

    @Test
    fun addPointToValidNumber() {
        builder.setExpression("17D53")

        builder.addPoint()

        assert(builder.get() == "17D53.")
    }

    @Test
    fun addPointToFloatNumber() {
        builder.setExpression("01.3")

        builder.addPoint()

        assert(builder.get() == "01.3×0.")
    }

    @Test
    fun addPointToExpr() {
        builder.setExpression("01.3-")

        builder.addPoint()

        assert(builder.get() == "01.3-0.")
    }

    @Test
    fun addPointTwoTimes() {
        builder.setExpression("1")

        builder.addPoint()
        builder.addPoint()

        assert(builder.get() == "1.")
    }

    @Test
    fun addPointToEmptyString() {
        builder.addPoint()

        assert(builder.get() == "0.")
    }

    @Test
    fun addPiConstantToEmptyExpr() {
        builder.addConstant(PI.toString())

        assert(builder.get() == PI.toString())
    }

    @Test
    fun addConstantAfterBracket() {
        builder.setExpression("3+(2-1)")

        builder.addConstant(PI.toString())

        assert(builder.get() == "3+(2-1)×$PI")
    }

    @Test
    fun addConstantAfterNumber() {
        builder.setExpression("3.23")

        builder.addConstant(PI.toString())

        assert(builder.get() == "3.23×$PI")
    }

    @Test
    fun addAbsAfterNumber() {
        builder.setExpression("3.23")

        builder.addAbs()

        assert(builder.get() == "3.23×<")
    }

    @Test
    fun addAbsAfterNestedAbs() {
        val expr = "<<1/3+(-4/3)>-<4×ln(1)-2>>"
        builder.setExpression(expr)

        builder.addAbs()

        assert(builder.get() == "$expr×<")
    }

    @Test
    fun closeAbsTest() {
        val expr = "<<1/3+(-4/3)>-<4×ln(1)-2>"
        builder.setExpression(expr)

        builder.addAbs()

        assert(builder.get() == "$expr>")
    }

    @Test
    fun addAbsToEmptyExpr() {
        builder.addAbs()
        val r1 = builder.get()
        builder.addAbs()
        val r2 = builder.get()

        assert(r1 == "<")
        assert(r2 == "<<")
    }

    @Test
    fun clearEmptyExpr() {
        builder.clear()
        builder.clear()

        assert(builder.get().isEmpty())
    }

    @Test
    fun clearSimpleExpr() {
        builder.setExpression("1 + 2 / (2 - ln(3))")

        builder.clear()

        assert(builder.get().isEmpty())
    }

    @Test
    fun backspaceFunction() {
        builder.setExpression("4-sin")

        builder.backspace()

        assert(builder.get() == "4-")
    }

    @Test
    fun backspaceEmptyExpr() {
        builder.backspace()
        builder.backspace()

        assert(builder.get().isEmpty())
    }

    @Test
    fun backspaceNumber() {
        builder.setExpression("4A0")

        builder.backspace()

        assert(builder.get() == "4A")
    }

    @Test
    fun backspaceOperation() {
        builder.setExpression("1+")

        builder.backspace()

        assert(builder.get() == "1")
    }

    @Test
    fun backspaceBracket() {
        builder.setExpression("(1+3)")

        builder.backspace()

        assert(builder.get() == "(1+3")
    }

    @Test
    fun backspaceThreeTimes() {
        builder.setExpression("1-sin(3")

        builder.backspace()
        builder.backspace()
        builder.backspace()

        assert(builder.get() == "1-")
    }

    @Test
    fun addOperationToEmptyExpr() {
        builder.addOperation('+')

        assert(builder.get().isEmpty())
    }

    @Test
    fun addInvalidOperation() {
        builder.setExpression("35")

        builder.addOperation('#')
        builder.addOperation('#')

        assert(builder.get() == "35")
    }

    @Test
    fun addOperationAfterNumber() {
        builder.setExpression("35")

        builder.addOperation('/')

        assert(builder.get() == "35/")
    }

    @Test
    fun addOperationAfterOpeningBracket() {
        builder.setExpression("35-(")

        builder.addOperation('+')

        assert(builder.get() == "35-(")
    }

    @Test
    fun addOperationAfterClosingBracket() {
        builder.setExpression("(35)")

        builder.addOperation('+')

        assert(builder.get() == "(35)+")
    }

    @Test
    fun addOperationTwoTimes() {
        builder.setExpression("1")

        builder.addOperation('+')
        builder.addOperation('-')

        assert(builder.get() == "1+")
    }

    @Test
    fun addOperationAfterConstant() {
        builder.setExpression(PI.toString())

        builder.addOperation('+')

        assert(builder.get() == "$PI+")
    }

    @Test
    fun addOperationAfterFunction() {
        builder.setExpression("ln")

        builder.addOperation('+')

        assert(builder.get() == "ln")
    }

    @Test
    fun addInvalidDigit() {
        builder.setExpression("020")

        builder.addDigit('f')
        builder.addDigit('+')

        assert(builder.get() == "020")
    }

    @Test
    fun addDigitToNumber() {
        builder.setExpression("592")

        builder.addDigit('F')
        builder.addDigit('0')

        assert(builder.get() == "592F0")
    }

    @Test
    fun addDigitToConstant() {
        builder.setExpression("592-$PI")

        builder.addDigit('3')
        builder.addDigit('1')

        assert(builder.get() == "592-$PI×31")
    }

    @Test
    fun addDigitAfterOpeningBracket() {
        builder.setExpression("592-(")

        builder.addDigit('B')

        assert(builder.get() == "592-(B")
    }

    @Test
    fun addDigitAfterClosingBracket() {
        builder.setExpression("(592-1)")

        builder.addDigit('B')

        assert(builder.get() == "(592-1)×B")
    }

    @Test
    fun addDigitAfterFunction() {
        builder.setExpression("sin")

        builder.addDigit('3')

        assert(builder.get() == "sin3")
    }

    @Test
    fun addDigitToEmptyExpr() {
        builder.addDigit('0')

        assert(builder.get() == "0")
    }

    @Test
    fun addFunctionToEmptyExpr() {
        builder.addFunction("sin")

        assert(builder.get() == "sin" || builder.get() == "sin(")
    }

    @Test
    fun addFunctionAfterOpeningBracket() {
        builder.setExpression("2+(")

        builder.addFunction("cos")

        assert(builder.get() == "2+(cos(" || builder.get() == "2+(cos")
    }

    @Test
    fun addFunctionAfterClosingBracket() {
        builder.setExpression("2/(3-2)")

        builder.addFunction("sin")

        assert(builder.get() == "2/(3-2)×sin(" || builder.get() == "2/(3-2)×sin")
    }

    @Test
    fun addFunctionAfterNumber() {
        builder.setExpression("23856N")

        builder.addFunction("ln")

        assert(builder.get() == "23856N×ln(" || builder.get() == "23856N×ln")
    }

    @Test
    fun addFunctionAfterFunction() {
        builder.setExpression("sin")

        builder.addFunction("ln")

        assert(builder.get() == "sin")
    }

    @Test
    fun addBracketsToEmptyExpr() {
        builder.addBracket(BracketType.Round)
        builder.addBracket(BracketType.Curly)
        builder.addBracket(BracketType.Square)
        builder.addBracket(BracketType.Round)

        assert(builder.get() == "({[(")
    }

    @Test
    fun addBracketToNumber() {
        builder.setExpression("2621")

        builder.addBracket(BracketType.Round)

        assert(builder.get() == "2621×(")
    }

    @Test
    fun addBracketToOpeningBracket() {
        builder.setExpression("2621×(")

        builder.addBracket(BracketType.Round)
        builder.addBracket(BracketType.Round)

        assert(builder.get() == "2621×(((")
    }

    @Test
    fun closeAndOpenBracket() {
        builder.setExpression("(2621-1")

        builder.addBracket(BracketType.Round)
        builder.addBracket(BracketType.Round)

        assert(builder.get() == "(2621-1)×(")
    }

    @Test
    fun openBracketAfterOtherTypeBracket() {
        builder.setExpression("[2")

        builder.addBracket(BracketType.Round)
        builder.addBracket(BracketType.Square)

        assert(builder.get() == "[2×([")
    }

    @Test
    fun openBracketAfterOperation() {
        builder.setExpression("[2+")

        builder.addBracket(BracketType.Round)

        assert(builder.get() == "[2+(")
    }

    @Test
    fun openBracketAfterFunction() {
        builder.setExpression("8-sin")

        builder.addBracket(BracketType.Round)

        assert(builder.get() == "8-sin(")
    }

    @Test
    fun openBracketAfterPoint() {
        builder.setExpression("8.")

        builder.addBracket(BracketType.Round)

        assert(builder.get() == "8.")
    }
}