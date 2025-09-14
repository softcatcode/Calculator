package com.codingeveryday.calcapp

import com.codingeveryday.calcapp.data.core.implementations.ConstantProvider
import com.codingeveryday.calcapp.data.core.implementations.MathImplementation
import com.codingeveryday.calcapp.data.implementations.TranslationImplementation
import com.codingeveryday.calcapp.domain.entities.Number
import junit.framework.TestCase.assertEquals
import org.junit.Test

class TransformTest {

    private val calculator = MathImplementation(ConstantProvider())
    private val transformer = TranslationImplementation(calculator)

    @Test
    fun transformBinToDec() {
        val a = Number("1010.10", 2)

        val r = transformer.transformNS(a, 10)

        assertEquals(r.toString(), "10.5")
    }

    @Test
    fun transformHexToDec() {
        val a = Number("F0.1", 16)

        val r = transformer.transformNS(a, 10)

        assertEquals(r.toString(), "240.0625")
    }

    @Test
    fun transformBinToHex() {
        val a = Number("1010.1", 2)

        val r = transformer.transformNS(a, 16)

        assertEquals(r.toString(), "A.8")
    }

    @Test
    fun transformInvalidNumber() {
        val a = Number("0", 10).apply { base = -1 }

        val r = transformer.transformNS(a, 16)

        assertEquals(r, a)
    }

    @Test
    fun transformDecToDec() {
        val a = Number("182833", 10)

        val r = transformer.transformNS(a, 10)

        assertEquals(r.toString(), "182833")
    }

    @Test
    fun transformIntegerDecToBin() {
        val a = Number("2491", 10)

        val r = transformer.transformNS(a, 2)

        assertEquals(r.toString(), "100110111011")
    }
}