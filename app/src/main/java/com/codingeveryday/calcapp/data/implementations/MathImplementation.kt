package com.codingeveryday.calcapp.data.implementations

import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.domain.interfaces.ConstantProviderInterface
import com.codingeveryday.calcapp.domain.interfaces.ConstantProviderInterface.Companion.MAX_ACCURACY_ORDER
import com.codingeveryday.calcapp.domain.interfaces.MathInterface
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class MathImplementation @Inject constructor(
    private val constantProvider: ConstantProviderInterface
): MathInterface {

    private fun sum(a: MutableList<Byte>, b: MutableList<Byte>, base: Int): MutableList<Byte> {
        val n = max(a.size, b.size)
        var r: Byte = 0
        repeat (n - a.size) { a.add(0) }
        repeat (n - b.size) { b.add(0) }
        for (i in 0 until n) {
            a[i] = a[i].plus(b[i] + r).toByte()
            r = 0
            if (a[i] >= base) {
                a[i] = a[i].minus(base).toByte()
                ++r
            }
        }
        if (r > 0)
            a.add(r)
        while (b.size > 1 && b.last() == 0.toByte())
            b.removeLast()
        return a
    }

    private fun sub(a: MutableList<Byte>, b: MutableList<Byte>, base: Int): MutableList<Byte> {
        val n = max(a.size, b.size)
        repeat(n - b.size) { b.add(0) }
        for (i in a.indices) {
            a[i] = a[i].minus(b[i]).toByte()
            if (a[i] < 0) {
                a[i] = a[i].plus(base).toByte()
                var j = i + 1
                while (a[j] == 0.toByte()) {
                    a[j] = (base - 1).toByte()
                    ++j
                }
                a[j] = a[j].minus(1).toByte()
            }
        }
        while (a.size > 1 && a.last() == 0.toByte())
            a.removeLast()
        while (b.size > 1 && b.last() == 0.toByte())
            b.removeLast()
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
            if (a[i] > b[i])
                return 1
            if (a[i] < b[i])
                return -1
            --i
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
        var result = mutableListOf<Byte>(0)
        for (i in a.indices) {
            val num = MutableList<Byte>(i) {0}
            var r = 0
            for (digit in b) {
                r += a[i] * digit
                num.add((r % base).toByte())
                r = r.div(base)
            }
            if (r > 0)
                num.add(r.toByte())
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
        val divider = MutableList<Byte>(n) {0}.apply { addAll(b) }
        val result = mutableListOf<Byte>(0)
        while (n >= 0) {
            while (cmp(a, divider) >= 0) {
                sub(a, divider, base)
                sum(result, delta, base)
            }
            divider.removeFirst()
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
        var order = 0
        while (order > MAX_ACCURACY_ORDER && first.indexOfFirst { it > 0 }  != -1) {
            first.add(0, 0)
            var nextDigit = 0
            while (cmp(first, second) >= 0) {
                first = sub(first, second, a.base)
                ++nextDigit
            }
            result.add(0, nextDigit.toByte())
            --order
        }
        if (first.indexOfFirst { it > 0 }  != -1 && result.indexOfFirst { it > 0 } == -1)
            result.add(0, 1)

        return Number(result, order, a.sign == b.sign, a.base)
    }

    override fun mod(a: Number, b: Number): Number {
        val num = intPart(div(a, b))
        return sub(a, mul(num, b))
    }

    override fun pow(a: Number, b: Number): Number {
        if (a.base != b.base)
            throw Exception("Numbers are in different number systems: ${a.base} and ${b.base}")
        if (b.order < 0)
            throw Exception("Attempt to calculate a float power")
        if (b.digits.indexOfFirst { it > 0 } == -1)
            return Number("1", a.base)
        return if (b.digits[0] % 2 == 1) {
            val num = pow(a, sum(b, Number("-1", a.base)))
            mul(num, a)
        } else {
            val two = if (a.base == 2) "10" else "2"
            val num = pow(a, div(b, Number(two, a.base)))
            mul(num, num)
        }
    }

    private fun tailorRowSin(a: Number): Number {
        val one = Number("1", a.base)
        var numerator = Number(a.digits, a.order, a.sign, a.base)
        var denominator = one.copy()
        val numeratorMulRatio = mul(a, a).apply { sign = false }
        var i = Number(if (a.base == 2) "10" else "2", a.base)
        var j = sum(one, i)

        var prevResult = Number("0", a.base)
        var result = a.copy()
        val eps = constantProvider.epsValue(a.base)

        while (cmp(sub(result, prevResult).apply { sign = true }, eps) > 0) {
            numerator = mul(numerator, numeratorMulRatio)
            denominator = mul(mul(denominator, i), j)
            i = sum(j, one)
            j = sum(i, one)

            prevResult = result
            result = sum(result, div(numerator, denominator))
        }
        return round(result, 1)
    }

    private fun tailorRowCos(a: Number): Number {
        val one = Number("1", a.base)
        var numerator = one.copy()
        var denominator = one.copy()
        val numeratorMulRatio = mul(a, a).apply { sign = false }
        var i = one.copy()
        var j = Number(if (a.base == 2) "10" else "2", a.base)

        var prevResult = Number("0", a.base)
        var result = one.copy()
        val eps = constantProvider.epsValue(a.base)

        while (cmp(sub(result, prevResult).apply { sign = true }, eps) > 0) {
            numerator = mul(numerator, numeratorMulRatio)
            denominator = mul(mul(denominator, i), j)
            i = sum(j, one)
            j = sum(i, one)

            prevResult = result
            result = sum(result, div(numerator, denominator))
        }
        return round(result, 1)
    }

    private fun toRad(a: Number): Number {
        val two = if (a.base == 2) Number("10", a.base) else Number("2", a.base)
        val three = sum(two, Number("1", a.base))
        val num = mul(mul(three, three), two).apply { order++ }

        return div(mul(a, constantProvider.piValue(a.base)), num)
    }

    override fun sin(a: Number, angleUnit: AngleUnit): Number {
        val pi = constantProvider.piValue(a.base)
        val two = Number(if (a.base == 2) "10" else "2", a.base)
        val doublePi = mul(pi, two)
        val halfPi = div(pi, two)
        val x = mod(if (angleUnit == AngleUnit.Radians) a else toRad(a), doublePi)

        if (cmp(x, halfPi) == -1)
            return tailorRowSin(x)
        if (cmp(x, pi) == -1)
            return tailorRowCos(sub(x, halfPi))
        val num = mul(sum(two, Number("1", a.base)), halfPi)
        if (cmp(x, num) == -1)
            return tailorRowSin(sub(x, pi)).apply { sign = false }
        return tailorRowSin(sub(doublePi, x)).apply { sign = false }
    }

    override fun cos(a: Number, angleUnit: AngleUnit): Number {
        val pi = constantProvider.piValue(a.base)
        val two = Number(if (a.base == 2) "10" else "2", a.base)
        val doublePi = mul(pi, two)
        val halfPi = div(pi, two)
        val x = mod(if (angleUnit == AngleUnit.Radians) a else toRad(a), doublePi)

        if (cmp(x, halfPi) == -1)
            return tailorRowCos(x)
        if (cmp(x, pi) == -1)
            return tailorRowSin(sub(x, halfPi)).apply { sign = false }
        val num = mul(sum(two, Number("1", a.base)), halfPi)
        if (cmp(x, num) == -1)
            return tailorRowCos(sub(x, pi)).apply { sign = false }
        return tailorRowCos(sub(doublePi, x))
    }
    override fun tan(a: Number, angleUnit: AngleUnit) = div(sin(a, angleUnit), cos(a, angleUnit))

    override fun ctg(a: Number, angleUnit: AngleUnit) = div(cos(a, angleUnit), sin(a, angleUnit))

    override fun abs(a: Number) = Number(a.digits, a.order, true, a.base)

    override fun fractionPart(a: Number) = sub(a, intPart(a))

    override fun intPart(a: Number): Number {
        if (a.digits.size <= -a.order)
            return if (a.sign) Number("0", a.base) else Number("-1", a.base)
        val digits = if (a.order < 0)
            a.digits.subList(-a.order, a.digits.size)
        else
            MutableList<Byte>(a.order) {0}.apply { addAll(a.digits) }
        var num = Number(digits, 0, a.sign, a.base)
        if (!a.sign) {
            num = sub(num, Number("1", num.base))
            num.sign = false
        }
        return num
    }

    private fun decrement(a: MutableList<Byte>, base: Int) {
        var i = 0
        val zero = 0.toByte()
        while (i < a.size && a[i] == zero)
            ++i
        a[i] = a[i].minus(1).toByte()
        val maxDigit = (base - 1).toByte()
        --i
        while (i >= 0) {
            a[i] = maxDigit
            --i
        }
        if (a.last() == zero && a.size > 1)
            a.removeLast()
    }

    override fun fac(a: Number): Number {
        if (a.order < 0)
            throw Exception("Attempt to calculate factorial of a float $a")
        if (!a.sign)
            throw Exception("Attempt to calculate factorial of a negative $a")
        val value = MutableList<Byte>(a.order) {0}.apply { addAll(a.digits) }
        var result = mutableListOf<Byte>(1)
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

    private fun round(a: Number, digitsAfterPoint: Int = 0): Number {
        var x: Number = a.copy().apply { order += digitsAfterPoint }
        val eps = constantProvider.epsValue(a.base).apply { order += digitsAfterPoint }
        val fr = fractionPart(x)
        if (cmp(fr, eps) <= 0)
            x = sub(x, fr)
        val delta = sub(Number("1", a.base), fr)
        if (cmp(delta, eps) <= 0)
            x = sum(x, delta)
        x.order -= digitsAfterPoint
        return x
    }

    override fun sqrt(a: Number): Number {
        if (!a.sign)
            throw RuntimeException("attempt to calculate sqrt from a negative number")

        val two = if (a.base > 2) Number("2", a.base) else Number("10", 2)
        val argumentOrder = if (a.order % 2 == 0) 0 else 1
        val argument = Number(a.digits, argumentOrder, a.sign, a.base)

        var l = Number("0", a.base)
        var r = argument.copy()
        val eps = constantProvider.epsValue(a.base)
        while (cmp(sub(r, l), eps) > 0) {
            val num = div(sum(l, r), two)
            if (cmp(mul(num, num), argument) > 0)
                r = num
            else
                l = num
        }
        var result = div(sum(l, r), two)
        result = round(result)

        result.order += when (a.order % 2) {
            0 -> a.order / 2
            1 -> (a.order - 1) / 2
            else -> (a.order + 1) / 2
        }
        return result
    }

    override fun minus(a: Number) = Number(a.digits, a.order, !a.sign, a.base)

    override fun log(a: Number, b: Number): Number {
        TODO("Not yet implemented")
    }

    private fun tailorRowLnArgPlusOne(x: Number): Number {
        var numerator = x.copy()
        val numeratorMulRatio = x.copy().apply { sign = !sign }
        val one = Number("1", x.base)
        var denominator = one.copy()
        val eps = constantProvider.epsValue(x.base)
        var prevResult: Number
        var result = div(numerator, denominator)
        var iterationLimit = 100
        do {
            prevResult = result
            numerator = mul(numerator, numeratorMulRatio)
            denominator = sum(denominator, one)
            result = sum(result, div(numerator, denominator))
        } while (cmp(sub(result, prevResult).apply { sign = true }, eps) > 0 && iterationLimit-- > 0)
        return result
    }

    override fun ln(a: Number): Number {
        if (cmp(a, Number("0", a.base)) <= 0)
            throw RuntimeException("ln from x <= 0 is undefined")

        val one = Number("1", a.base)
        val two = Number(if (a.base == 2) "10" else "2", a.base)
        val e = constantProvider.expValue(a.base)
        val invE = div(one, e)

        val tooSmall = cmp(a, invE) <= 0
        val tooBig = cmp(a, e) >= 0
        if (tooBig || tooSmall) {
            var power = one.copy().apply { sign = tooBig }
            var num: Number = if (tooBig) e else invE
            var nextNum = mul(num, num)
            while ((tooSmall && cmp(nextNum, a) == 1) || (tooBig && cmp(nextNum, a) == -1)) {
                num = nextNum
                nextNum = mul(num, num)
                power = mul(power, two)
            }
            val x = div(a, num)
            return sum(power, ln(x))
        }

        return if (cmp(a, two) == 1)
            tailorRowLnArgPlusOne( sub(div(one, a), one) ).apply { sign = !sign }
        else
            tailorRowLnArgPlusOne(sub(a, one))
    }
}