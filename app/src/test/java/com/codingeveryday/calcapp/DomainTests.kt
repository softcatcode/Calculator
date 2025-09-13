package com.codingeveryday.calcapp

import androidx.lifecycle.MutableLiveData
import com.codingeveryday.calcapp.TestDataGenerator.getRandomHistoryItem
import com.codingeveryday.calcapp.TestDataGenerator.getRandomBase
import com.codingeveryday.calcapp.TestDataGenerator.getRandomExpr
import com.codingeveryday.calcapp.TestDataGenerator.getRandomHistoryItemList
import com.codingeveryday.calcapp.TestDataGenerator.getRandomNumber
import com.codingeveryday.calcapp.TestDataGenerator.getRandomString
import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.BracketType
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.matchingBrackets
import com.codingeveryday.calcapp.domain.entities.bracketType
import com.codingeveryday.calcapp.domain.entities.closingBracket
import com.codingeveryday.calcapp.domain.entities.openingBracket
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.ConstantInterpreterInterface
import com.codingeveryday.calcapp.domain.interfaces.ConstantProviderInterface
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import com.codingeveryday.calcapp.domain.interfaces.MathInterface
import com.codingeveryday.calcapp.domain.interfaces.ParseExpressionInterface
import com.codingeveryday.calcapp.domain.interfaces.TranslationInterface
import com.codingeveryday.calcapp.domain.useCases.AddHistoryItemUseCase
import com.codingeveryday.calcapp.domain.useCases.CalculateUseCase
import com.codingeveryday.calcapp.domain.useCases.ClearHistoryUseCase
import com.codingeveryday.calcapp.domain.useCases.GetHistoryListUseCase
import com.codingeveryday.calcapp.domain.useCases.NumberSystemTranslationUseCase
import com.codingeveryday.calcapp.domain.useCases.RemoveHistoryItemUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.random.Random
import com.codingeveryday.calcapp.domain.entities.Number

class DomainTests {

    private val calculationInterface = mock(CalculationInterface::class.java)
    private val constantInterpreterInterface = mock(ConstantInterpreterInterface::class.java)
    private val constantProviderInterface = mock(ConstantProviderInterface::class.java)
    private val expressionBuilderInterface = mock(ExpressionBuilderInterface::class.java)
    private val historyManagerInterface = mock(HistoryManagerInterface::class.java)
    private val mathInterface = mock(MathInterface::class.java)
    private val parseExpressionInterface = mock(ParseExpressionInterface::class.java)
    private val translationInterface = mock(TranslationInterface::class.java)

    @Before
    fun resetMocks() {
        reset(calculationInterface)
        reset(constantInterpreterInterface)
        reset(constantProviderInterface)
        reset(expressionBuilderInterface)
        reset(historyManagerInterface)
        reset(mathInterface)
        reset(parseExpressionInterface)
        reset(translationInterface)
    }

    @Test
    fun bracketManipulations() {
        // Checking the openingBracket function.
        assert(openingBracket(BracketType.Triangle) == '<')
        assert(openingBracket(BracketType.Round) == '(')
        assert(openingBracket(BracketType.Square) == '[')
        assert(openingBracket(BracketType.Curly) == '{')

        // Checking the closingBracket function.
        assert(closingBracket(BracketType.Triangle) == '>')
        assert(closingBracket(BracketType.Round) == ')')
        assert(closingBracket(BracketType.Square) == ']')
        assert(closingBracket(BracketType.Curly) == '}')

        // Checking the bracketType function.
        BracketType.entries.forEach {
            val opening = openingBracket(it)
            val closing = closingBracket(it)

            assert(it == bracketType(opening))
            assert(it == bracketType(closing))
        }
        assert(bracketType('a' + Random.nextInt(0, 26)) == null)

        // Checking the matchingBrackets function.
        assert(matchingBrackets('<', '>'))
        assert(matchingBrackets('(', ')'))
        assert(matchingBrackets('{', '}'))
        assert(matchingBrackets('[', ']'))

        assert(!matchingBrackets('[', '>'))
        assert(!matchingBrackets('[', '}'))
        assert(!matchingBrackets('[', ')'))
        assert(!matchingBrackets('<', ']'))
        assert(!matchingBrackets('<', '}'))
        assert(!matchingBrackets('<', ')'))
        assert(!matchingBrackets('(', '>'))
        assert(!matchingBrackets('(', '}'))
        assert(!matchingBrackets('(', ']'))
    }

    @Test
    fun addHistoryItem(): Unit = runBlocking {
        val addHistoryItemUseCase = AddHistoryItemUseCase(historyManagerInterface)
        val historyItem = getRandomHistoryItem()

        addHistoryItemUseCase(historyItem)

        verify(historyManagerInterface, times(1))
            .addItem(historyItem)
    }

    @Test
    fun calculate() {
        val calculateUseCase = CalculateUseCase(calculationInterface)
        val expr = getRandomExpr()
        val base = getRandomBase()
        val answer = getRandomString(10)
        doReturn(answer)
            .whenever(calculationInterface)
            .calculateValue(anyString(), anyInt(), any())

        val result = calculateUseCase(expr, base)

        verify(calculationInterface, times(1))
            .calculateValue(expr, base, AngleUnit.Radians)
        assert(result == answer)
    }

    @Test
    fun clearHistory(): Unit = runBlocking {
        val clearHistoryUseCase = ClearHistoryUseCase(historyManagerInterface)

        clearHistoryUseCase()

        verify(historyManagerInterface, times(1))
            .clearAll()
    }

    @Test
    fun getHistoryList(): Unit = runBlocking {
        val getHistoryListUseCase = GetHistoryListUseCase(historyManagerInterface)
        val items = MutableLiveData(getRandomHistoryItemList())
        doReturn(items)
            .whenever(historyManagerInterface)
            .getHistoryList()

        val result = getHistoryListUseCase().value

        verify(historyManagerInterface, times(1))
            .getHistoryList()
        assert(result == items.value)
    }

    @Test
    fun translateNumberSystem() {
        val numberSystemTranslationUseCase = NumberSystemTranslationUseCase(translationInterface)
        val number = getRandomNumber()
        val base = getRandomBase()
        val answer = getRandomNumber()
        doReturn(answer).whenever(translationInterface).transformNS(any(), anyInt())

        val result = numberSystemTranslationUseCase(number, base)

        verify(translationInterface, times(1))
            .transformNS(number, base)
        assert(result.digits == answer.digits)
        assert(result.order == answer.order)
        assert(result.sign == answer.sign)
        assert(result.base == answer.base)
    }

    @Test
    fun removeHistoryItem(): Unit = runBlocking {
        val removeHistoryItemUseCase = RemoveHistoryItemUseCase(historyManagerInterface)
        val id = Random.nextInt(10, 10000)

        removeHistoryItemUseCase(id)

        verify(historyManagerInterface, times(1))
            .removeItem(id)
    }

    @Test
    fun correctNumberToString() {
        assert(Number("0", 10).toString() == "0")
        assert(Number("-000.00000", 10).toString() == "0")
        assert(Number("-000.0100", 10).toString() == "-0.01")
        assert(Number("1030F", 16).toString() == "1030F")
        assert(Number("-1", 10).toString() == "-1")
        assert(Number("0.1000", 10).toString() == "0.1")
        assert(Number("-0.10", 10).toString() == "-0.1")
        assert(Number("5", 10).toString() == "5")
        assert(Number("-005.6218730", 10).toString() == "-5.621873")
        assert(Number("150800000000", 16).toString() == "150800000000")
    }

    @Test
    fun invalidNumberConstructing() {
        val testData = listOf(
            "0" to -1,
            "1.0." to 3,
            ".1." to 10,
            "24" to 4,
            "4863.92" to -2,
            "48.63.92" to 10,
            "FFF" to 0,
        )
        testData.forEach {
            try {
                Number(it.first, it.second)
                assert(false)
            } catch (_: Exception) { }
        }
    }

    @Test
    fun copyNumberTest() {
        val n1 = Number("5738.0275", 10)
        val n2 = Number("-0.0011348", 10)
        val n3 = Number("-1.001101", 2)
        val n4 = Number("F.091101A", 16)
        val n5 = Number("-F40B", 16)

        assert(n1.toString() == n1.copy().toString())
        assert(n2.toString() == n2.copy().toString())
        assert(n3.toString() == n3.copy().toString())
        assert(n4.toString() == n4.copy().toString())
        assert(n5.toString() == n5.copy().toString())
    }
}