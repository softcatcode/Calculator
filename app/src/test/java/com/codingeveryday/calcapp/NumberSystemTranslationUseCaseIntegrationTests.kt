package com.codingeveryday.calcapp

import com.codingeveryday.calcapp.data.core.implementations.ConstantProvider
import com.codingeveryday.calcapp.data.core.implementations.MathImplementation
import com.codingeveryday.calcapp.data.implementations.TranslationImplementation
import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.domain.useCases.NumberSystemTranslationUseCase
import junit.framework.TestCase.assertEquals
import org.junit.Test

class NumberSystemTranslationUseCaseIntegrationTests {

    private val constantProvider = ConstantProvider()
    private val mathImpl = MathImplementation(constantProvider)
    private val translator = TranslationImplementation(mathImpl)

    private val translateUseCase = NumberSystemTranslationUseCase(translator)

    data class TestCase(
        val number: String,
        val srcBase: Int,
        val dstBase: Int,
        val answer: String
    )

    private val cases = listOf(
        TestCase("579240", 10, 2, "10001101011010101000"),
        TestCase("256", 10, 8, "400"),
        TestCase("47296", 12, 6, "2013510"),
        TestCase("99", 10, 3, "10200"),
        TestCase("FF4A02B", 16, 10, "267690027"),
    )

    @Test
    fun translateNumber1() {
        with (cases[0]) {
            val a = Number(number, srcBase)
            val result = translateUseCase(a, dstBase)
            assertEquals(result.toString(), answer)
        }
    }

    @Test
    fun translateNumber2() {
        with (cases[1]) {
            val a = Number(number, srcBase)
            val result = translateUseCase(a, dstBase)
            assertEquals(result.toString(), answer)
        }
    }

    @Test
    fun translateNumber3() {
        with (cases[2]) {
            val a = Number(number, srcBase)
            val result = translateUseCase(a, dstBase)
            assertEquals(result.toString(), answer)
        }
    }

    @Test
    fun translateNumber4() {
        with (cases[3]) {
            val a = Number(number, srcBase)
            val result = translateUseCase(a, dstBase)
            assertEquals(result.toString(), answer)
        }
    }

    @Test
    fun translateNumber5() {
        with (cases[4]) {
            val a = Number(number, srcBase)
            val result = translateUseCase(a, dstBase)
            assertEquals(result.toString(), answer)
        }
    }
}