package com.codingeveryday.calcapp.data.core.implementations

import com.codingeveryday.calcapp.data.core.interfaces.ConstantProviderInterface
import com.codingeveryday.calcapp.domain.entities.Number
import kotlin.math.min

class FastMathImplementation(
    constantProvider: ConstantProviderInterface
): MathImplementation(constantProvider) {

    private fun karatsubaMul(a: MutableList<Byte>, b: MutableList<Byte>, base: Int): MutableList<Byte> {
        val n = min(a.size, b.size) / 2
        if (n < 2)
            return super.mul(a, b, base)

        // x = 10^n
        // (a1 * x + b1) * (a2 * x + b2) =
        // (a1 * a2) * x^2 + ((a1 + b1)(a2 + b2) - a1 * a2 - b1 * b2)x + b1 * b2
        val a1 = MutableList(a.size - n) { a[it + n] }
        val b1 = MutableList(n) { a[it] }
        val a2 = MutableList(b.size - n) { b[it + n] }
        val b2 = MutableList(n) { b[it] }

        val a12 = karatsubaMul(a1, a2, base) // a1 * a2
        val b12 = karatsubaMul(b1, b2, base) // b1 * b2
        val ab1ab2 = karatsubaMul(sum(a1, b1, base), sum(a2, b2, base), base) // (a1 + a2) * (b1 + b2)

        val first = MutableList<Byte>(n * 2) { 0 }.apply {
            addAll(a12)
        } // (a1 * a2) * x^2
        val second = MutableList<Byte>(n) { 0 }.apply {
            sub(ab1ab2, a12, base)
            sub(ab1ab2, b12, base)
            addAll(ab1ab2)
        } // ((a1 + a2)(b1 + b2) - a1 * a2 - b1 * b2)x
        val result = sum(sum(first, second, base), b12, base)
        return result
    }

    override fun mul(a: Number, b: Number): Number {
        if (a.base != b.base)
            throw Exception("Numbers are in different number systems: ${a.base} and ${b.base}")
        val sign = a.sign == b.sign
        val order = a.order + b.order
        return if (a.digits.size > 5 && b.digits.size > 5) {
            val result = karatsubaMul(a.digits, b.digits, a.base)
            Number(result, order, sign, a.base)
        } else {
            val result = mul(a.digits, b.digits, a.base)
            Number(result, order, sign, a.base)
        }
    }
}