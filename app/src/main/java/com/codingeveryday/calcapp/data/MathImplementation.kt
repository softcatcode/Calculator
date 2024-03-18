package com.codingeveryday.calcapp.data

import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.Expression
import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.MathInterface
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class MathImplementation @Inject constructor(): MathInterface {

    private fun sum(a: MutableList<Byte>, b: MutableList<Byte>, base: Int): MutableList<Byte> {
        val n = max(a.size, b.size)
        var r: Byte = 0
        for (i in 0..n) {
            a[i].plus(b[i] + r)
            if (a[i] > base) {
                a[i].minus(base)
                ++r
            }
        }
        if (r > 0)
            a.add(r)
        return a
    }

    private fun sub(a: MutableList<Byte>, b: MutableList<Byte>, base: Int): MutableList<Byte> {
        for (i in a.indices) {
            a[i].minus(b[i])
            if (a[i] < 0) {
                a[i].plus(base)
                if (a[i + 1] == 0.toByte())
                    a[i - 1] = (Expression.DIGITS[base - 1] - '0').toByte()
                else
                    a[i + 1].minus(1)
            }
        }
        return a
    }

    private fun cmp(a: MutableList<Byte>, b: MutableList<Byte>): Int {
        var i = a.indexOfLast { it > 0 }
        val j = b.indexOfLast { it > 0 }
        if (i > j)
            return 1
        if (j > i)
            return -1
        if (i == -1)
            return 0
        while (i >= 0) {
            --i
            if (a[i] > b[i])
                return 1
            if (a[i] < b[i])
                return -1
        }
        return 0
    }

    override fun sum(a: Number, b: Number): Number {
        if (a.base != b.base)
            throw Exception("Numbers are in different number systems: ${a.base} and ${b.base}")
        val n = min(a.order, b.order)
        val firstOperand = MutableList<Byte>((a.order - n).toInt()) {0}.apply { addAll(a.digits) }
        val secondOperand = MutableList<Byte>((b.order - n).toInt()) {0}.apply { addAll(b.digits) }
        return if (a.sign == b.sign) {
            val result = sum(firstOperand, secondOperand, a.base)
            Number(result, n, a.sign, a.base)
        } else {
            val sign: Boolean
            val num: MutableList<Byte>
            if (cmp(firstOperand, secondOperand) >= 0) {
                num = sub(firstOperand, secondOperand, a.base)
                sign = a.sign
            } else {
                num = sub(secondOperand, firstOperand, a.base)
                sign = b.sign
            }
            Number(num, n, sign, a.base)
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