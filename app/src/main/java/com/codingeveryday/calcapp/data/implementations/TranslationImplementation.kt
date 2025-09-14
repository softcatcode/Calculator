package com.codingeveryday.calcapp.data.implementations

import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.data.core.interfaces.MathInterface
import com.codingeveryday.calcapp.domain.interfaces.TranslationInterface
import javax.inject.Inject
import kotlin.math.abs

class TranslationImplementation @Inject constructor(
    private val math: MathInterface
): TranslationInterface {

    private fun translateDecInt(a: Int, base: Int): Number {
        val digits = mutableListOf<Byte>()
        var value = abs(a)
        while (value > 0) {
            digits.add((value % base).toByte())
            value /= base
        }
        if (digits.isEmpty())
            digits.add(0)
        return Number(digits, 0, a >= 0, base)
    }

    private fun decIntVal(number: Number, base: Int): Int {
        val digits = MutableList<Byte>(number.order) {0}.apply { addAll(number.digits) }
        var w = 1
        var result = 0
        for (d in digits) {
            result += w * d.toInt()
            w *= base
        }
        return result
    }

    private fun translateIntPart(digits: List<Byte>, fromBase: Int, toBase: Int, sign: Boolean): Number {
        var result = Number("0", toBase)
        var weight = Number("1", toBase)
        val sourceBase = translateDecInt(fromBase, toBase)

        for (digitValue in digits) {
            val nextDigit = translateDecInt(digitValue.toInt(), toBase)
            val nextValue = math.mul(weight, nextDigit)
            result = math.sum(result, nextValue)
            weight = math.mul(weight, sourceBase)
        }
        result.sign = sign
        return result
    }

    private fun transformFractionPart(a: Number, toBase: Int): Number {
        val isZero: (Number) -> Boolean = {
            it.digits.size == 1 && it.digits[0] == 0.toByte()
        }
        val result = mutableListOf<Byte>()
        var num = a.copy().apply { sign = true }
        val mulRatio = translateDecInt(toBase, a.base)
        var limit = 100
        while (!isZero(num) && limit > 0) {
            num = math.mul(num, mulRatio)
            val digit = math.intPart(num)
            result.add(decIntVal(digit, digit.base).toByte())
            num = math.sub(num, digit)
            --limit
        }
        result.reverse()
        return Number(result, -result.size, a.sign, toBase)
    }

    override fun transformNS(a: Number, toBase: Int): Number {
        val int = math.intPart(a)
        val digits = MutableList<Byte>(int.order) {0}.apply { addAll(int.digits) }
        val intPartResult = translateIntPart(digits, a.base, toBase, a.sign)

        val fraction = math.fractionPart(a)
        val fractionPartResult = transformFractionPart(fraction, toBase)

        return math.sum(intPartResult, fractionPartResult)
    }
}