package com.codingeveryday.calcapp.data

import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.Number
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
                    a[i - 1] = (base - 1).toByte()
                else
                    a[i + 1].minus(1)
            }
        }
        while (a.size > 1 && a.last() == 0.toByte())
            a.removeLast()
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
        val firstOperand = MutableList<Byte>((a.order - n)) {0}.apply { addAll(a.digits) }
        val secondOperand = MutableList<Byte>((b.order - n)) {0}.apply { addAll(b.digits) }
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
        var result = mutableListOf<Byte>()
        for (i in a.indices) {
            val num = MutableList<Byte>(i) {0}
            for (digit in b) {
                r += a[i] * digit
                num.add((r % base).toByte())
                r.div(base)
            }
            if (r > 0)
                num.add(r.toByte())
            while (result.size < num.size)
                result.add(0)
            result = sum(result, num, base)
        }
        return result
    }

    override fun mul(a: Number, b: Number): Number {
        if (a.base != b.base)
            throw Exception("Numbers are in different number systems: ${a.base} and ${b.base}")
        val sign = a.sign == b.sign
        val result = mul(a.digits, b.digits, a.base)
        return Number(result, a.order + b.order, sign, a.base)
    }

    private fun isZero(a: Number) = Number(a.digits, a.order, a.sign, a.base).let{
        it.order != 0 && it.sign && it.digits.size == 1 && it.digits[0] == 0.toByte()
    }

    private fun afterMaxTimesSub(a: MutableList<Byte>, b: MutableList<Byte>, base: Int): MutableList<Byte> {
        var n = a.size - b.size
        if (n < 0)
            return mutableListOf(0)
        val delta = MutableList<Byte>(n) {0}.apply { add(1) }
        val result = mutableListOf<Byte>(0)
        while (n >= 0) {
            while (cmp(a, b) >= 0) {
                sub(a, b, base)
                sum(result, delta, base)
            }
            if (n > 0)
                b.removeFirst()
            delta.removeFirst()
            --n
        }
        return result
    }

    override fun div(a: Number, b: Number): Number {
        if (a.base != b.base)
            throw Exception("Numbers are in different number systems: ${a.base} and ${b.base}")
        if (isZero(b))
            throw RuntimeException("Division by 0 occurred")

        val n = min(a.order, b.order)
        val orderA = a.order - n
        val orderB = b.order - n
        var first = MutableList<Byte>(orderA) {0}.apply { addAll(a.digits) }
        val second = MutableList<Byte>(orderB) {0}.apply { addAll(b.digits) }

        val result = afterMaxTimesSub(first, second, a.base)
        while (first.indexOfFirst { it > 0 }  != -1) {
            first.add(0, 0)
            var nextDigit = 0
            while (cmp(first, second) >= 0) {
                first = sub(first, second, a.base)
                ++nextDigit
            }
            result.add(0, nextDigit.toByte())
        }

        return Number(result, 0, a.sign == b.sign, 0)
    }

    override fun mod(a: Number, b: Number): Number {
        val num = intPart(div(a, b))
        return sub(a, mul(num, b))
    }

    override fun pow(a: Number, b: Number): Number {
        if (a.base != b.base)
            throw Exception("Numbers are in different number systems: ${a.base} and ${b.base}")
        if (b.order != 0)
            throw Exception("Attempt to calculate a float power")
        if (b.digits.indexOfFirst { it > 0 } == -1)
            return Number("1", a.base)
        return if (b.digits.last() % 2 == 0)
            pow(a, sum(b, Number("-1", a.base)))
        else {
            val two = if (a.base == 2) "10" else "2"
            pow(a, div(b, Number(two, a.base)))
        }
    }

    override fun sin(a: Number, angleUnit: AngleUnit): Number {
        var numerator = Number(a.digits, a.order, a.sign, a.base)
        var denominator = Number("1", a.base)
        val numeratorMulRatio = mul(a, a).apply { sign = false }
        var i = Number(if (a.base == 2) "10" else "2", a.base)
        var j = Number(if (a.base == 2) "11" else "3", a.base)

        var prevResult = Number("0", a.base)
        var result = sum(prevResult, div(numerator, denominator))
        val eps = epsValue(a.base)

        while (cmp(sub(result, prevResult).apply { sign = true }, eps) >= 0) {
            numerator = mul(numerator, numeratorMulRatio)
            denominator = mul(mul(denominator, i), j)
            i = j
            j = sum(j, Number("1", a.base))

            prevResult = result
            result = sum(result, div(numerator, denominator))
        }
        return result
    }

    override fun cos(a: Number, angleUnit: AngleUnit): Number {
        TODO("Not yet implemented")
    }

    override fun tan(a: Number, angleUnit: AngleUnit) = div(sin(a, angleUnit), cos(a, angleUnit))

    override fun ctg(a: Number, angleUnit: AngleUnit) = div(Number("1", a.base), tan(a, angleUnit))

    override fun abs(a: Number) = Number(a.digits, a.order, true, a.base)

    override fun fractionPart(a: Number) = sub(a, intPart(a))

    override fun intPart(a: Number): Number {
        if (a.order > a.digits.size)
            return if (a.sign) Number("0", a.base) else Number("-1", a.base)
        val digits = a.digits.subList(0, a.digits.size - a.order)
        var num = Number(digits, 0, a.sign, a.base)
        if (!a.sign) {
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
        if (a.order < 0)
            throw Exception("Attempt to calculate factorial of a float $a")
        if (!a.sign)
            throw Exception("Attempt to calculate factorial of a negative $a")
        val value = MutableList<Byte>(a.order) {0}.apply { addAll(a.digits) }
        var result = mutableListOf<Byte>(0)
        while (value.size > 1 || value[0] != 0.toByte()) {
            result = mul(result, value, a.base)
            decrement(value, a.base)
        }
        return Number(result, a.order, true, a.base)
    }

    private fun cmp(a: Number, b: Number): Int {
        val n = min(a.order, b.order)
        val first = MutableList<Byte>(a.order - n) {0}.apply { addAll(a.digits) }
        val second = MutableList<Byte>(b.order - n) {0}.apply { addAll(b.digits) }
        return cmp(first, second)
    }

    override fun sqrt(a: Number): Number {
        if (a.sign)
            throw RuntimeException("attempt to calculate sqrt from a negative number")

        val two = if (a.base > 2) Number("2", a.base) else Number("10", 2)
        val order = if (a.order % 2 == 0) 0 else 1

        var l = Number("0", a.base)
        var r = Number(a.digits, order, a.sign, a.base)
        val eps = epsValue(a.base)
        while (cmp(sub(r, l), eps) >= 0) {
            val num = div(sum(l, r), two)
            if (cmp(mul(num, num), a) > 0)
                r = num
            else
                l = num
        }
        var result = div(sum(l, r), two)
        if (a.order % 2 == -1)
            result = div(result, Number("10", a.base))

        result.order += a.order / 2
        return result
    }

    private fun epsValue(base: Int) = Number("0.000001", base)
}