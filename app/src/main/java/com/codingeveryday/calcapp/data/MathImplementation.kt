package com.codingeveryday.calcapp.data

import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.MathInterface
import javax.inject.Inject
import kotlin.math.max

class MathImplementation @Inject constructor(): MathInterface {

    private fun formatForSum(a: Number, intLen: Int, floatLen: Int): MutableList<Byte> {
        val digits = mutableListOf<Byte>()
        for (i in a.int.size until intLen)
            digits.add(0)
        digits.addAll(a.int)
        digits.addAll(a.float)
        for (i in a.float.size until floatLen)
            digits.add(0)
        return digits
    }

    private fun sum(a: MutableList<Byte>, b: MutableList<Byte>, base: Int): MutableList<Byte> {
        val digits = mutableListOf<Byte>()
        var r = 0
        for (i in a.size - 1 downTo 0) {
            r = a[i] + b[i]
            digits.add((r % base).toByte())
            r.div(base)
        }
        if (r > 0)
            digits.add(r.toByte())
        digits.reverse()
        return digits
    }

    private fun sub(a: MutableList<Byte>, b: MutableList<Byte>, base: Int): MutableList<Byte> {
        val result = mutableListOf<Byte>()
        var r = 0
        for (i in a.size - 1 downTo 1) {
            r += a[i] - b[i]
            if (r < 0) {
                if (a[i - 1] == 0.toByte())
                    a[i - 1] = 9.toByte()
                else
                    a[i - 1].minus(1)
            }
            result.add((r % base).toByte())
            r.div(base)
        }
        while (result.size > 1 && result[result.size - 1] == 0.toByte())
            result.removeAt(result.size - 1)
        result.reverse()
        return result
    }

    private fun cmpFormattedValues(a: MutableList<Byte>, b: MutableList<Byte>): Int {
        val n = a.size
        for (i in 0 until n)
            if (a[i] != b[i])
                return if (a[i] > b[i]) 1 else -1
        return 0
    }

    override fun sum(a: Number, b: Number): Number {
        if (a.base != b.base)
            throw Exception("Numbers are in different number systems: ${a.base} and ${b.base}")
        val floatSize = max(a.float.size, b.float.size)
        val intSize = max(a.int.size, b.int.size)
        val firstOperand = formatForSum(a, intSize, floatSize)
        val secondOperand = formatForSum(b, intSize, floatSize)
        if (a.sign == b.sign) {
            val num = sum(firstOperand, secondOperand, a.base)
            return Number(
                num.subList(0, num.size - floatSize),
                num.subList(num.size - floatSize, num.size),
                a.sign,
                a.base
            )
        } else {
            val comp = cmpFormattedValues(firstOperand, secondOperand)
            val sign = if (a.sign == b.sign || comp >= 0) a.sign else b.sign
            val num = if (comp >= 0)
                sub(firstOperand, secondOperand, a.base)
            else
                sub(secondOperand, firstOperand, a.base)
            return Number(
                num.subList(0, num.size - floatSize),
                num.subList(num.size - floatSize, num.size),
                sign,
                a.base
            )
        }
    }

    override fun sub(a: Number, b: Number): Number {
        b.sign = !b.sign
        val result = sum(a, b)
        b.sign = !b.sign
        return result
    }

    private fun mul(a: MutableList<Byte>, b: MutableList<Byte>, base: Int): MutableList<Byte> {
        var r = 0
        val result = mutableListOf<Byte>()
        for ((extraZeroCount, i) in (a.size - 1 downTo 0).withIndex()) {
            val num = mutableListOf<Byte>()
            for (k in 0 until extraZeroCount)
                num.add(0)
            for (j in b.size - 1 downTo 0) {
                r += a[i] * b[j]
                num.add((r % base).toByte())
                r.div(base)
            }
            if (r > 0)
                num.add(r.toByte())
            var ptr = 0
            while (ptr < num.size) {
                if (ptr == result.size)
                    result.add(0)
                result[ptr].plus(num[ptr])
                if (result[result.size - 1] >= base) {
                    val n = result[result.size - 1]
                    result[result.size - 1].mod(base)
                    result.add((n / base).toByte())
                }
                ++ptr
            }
        }
        result.reverse()
        return result
    }

    override fun mul(a: Number, b: Number): Number {
        if (a.base != b.base)
            throw Exception("Numbers are in different number systems: ${a.base} and ${b.base}")
        val n = a.float.size + b.float.size
        val firstOperand = mutableListOf<Byte>().apply {
            addAll(a.int)
            addAll(a.float)
        }
        val secondOperand = mutableListOf<Byte>().apply {
            addAll(b.int)
            addAll(b.float)
        }
        val sign = a.sign == b.sign
        val result = mul(firstOperand, secondOperand, a.base)
        return Number(
            result.subList(0, result.size - n),
            result.subList(result.size - n, result.size),
            sign,
            a.base
        )
    }

    override fun div(a: Number, b: Number): Number {
        TODO("Not yet implemented")
    }

    override fun mod(a: Number, b: Number): Number {
        TODO("Not yet implemented")
    }

    override fun pow(a: Number, b: Number): Number {
        if (a.base != b.base)
            throw Exception("Numbers are in different number systems: ${a.base} and ${b.base}")
        if (b.float.size != 0)
            throw Exception("Attempt to calculate a float power")
        if (b.int[0] == 0.toByte())
            return Number("1", a.base)
        if (b.int[1] == 1.toByte())
            return a
        return if (b.int[b.int.size - 1] % 2 == 0)
            pow(a, sum(b, Number("-1", a.base)))
        else {
            val two = if (a.base == 2) "10" else "2"
            pow(a, div(b, Number(two, a.base)))
        }
    }

    override fun sin(a: Number, angleUnit: AngleUnit): Number {
        TODO("Not yet implemented")
    }

    override fun cos(a: Number, angleUnit: AngleUnit): Number {
        TODO("Not yet implemented")
    }

    override fun tan(a: Number, angleUnit: AngleUnit): Number {
        TODO("Not yet implemented")
    }

    override fun ctg(a: Number, angleUnit: AngleUnit): Number {
        TODO("Not yet implemented")
    }

    override fun abs(a: Number): Number {
        return Number(a.int, a.float, true, a.base)
    }

    override fun fractionPart(a: Number): Number {
        var num = Number(a.int, mutableListOf(), a.sign, a.base)
        if (!a.sign) {
            num = sub(num, Number("1", num.base))
            num.sign = false
        }
        return sub(a, num)
    }

    override fun intPart(a: Number): Number {
        var num = Number(a.int, mutableListOf(), a.sign, a.base)
        if (!a.sign && a.float.size > 0) {
            num = sub(num, Number("1", num.base))
            num.sign = false
        }
        return num
    }

    private fun decrement(a: MutableList<Byte>, base: Int) {
        var i = a.size - 1
        val zero = 0.toByte()
        while (i >= 0 && a[i] == zero)
            --i
        a[i].minus(1)
        if (a[i] == 0.toByte())
            a.removeAt(i)
        else
            ++i
        val maxDigit = (base - 1).toByte()
        while (i < a.size) {
            a[i] = maxDigit
            ++i
        }
    }

    override fun fac(a: Number): Number {
        if (a.float.size > 0)
            throw Exception("Attempt to calculate factorial of a float $a")
        if (!a.sign)
            throw Exception("Attempt to calculate factorial of a negative $a")
        val value = mutableListOf<Byte>().apply{ addAll(a.int) }
        var result = Number("1", a.base)
        while (value.size > 1 || value[0] != 0.toByte()) {
            result = mul(result, Number(value, mutableListOf(), true, a.base))
            decrement(value, a.base)
        }
        return result
    }

    override fun sqrt(a: Number): Number {
        TODO("Not yet implemented")
    }
}