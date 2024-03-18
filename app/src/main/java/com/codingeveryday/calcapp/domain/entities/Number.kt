package com.codingeveryday.calcapp.domain.entities

import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface

private val invalidNumberException = Exception("Number is invalid")

class Number: Expression {
    var digits: MutableList<Byte>
    var order: Long
    var sign: Boolean
    var base: Int

    private fun cleanNumber(number: String): String {
        var pointCount = 0
        val sb = StringBuilder()
        for (c in number) {
            if (c in DIGITS || c == POINT)
                sb.append(c)
            if (c == POINT)
                ++pointCount
        }
        if (pointCount > 1)
            throw invalidNumberException
        return sb.toString()
    }

    constructor(number: String, base: Int = 10) {
        this.base = base
        sign = number[0] != operation[SUB_ID]
        digits = mutableListOf()
        order = 0
        val value = cleanNumber(number)
        for (i in value.length - 1 downTo 0) {
            if (value[i] == POINT)
                order -= digits.size.toLong()
            else
                digits.add(DIGITS.indexOf(value[i]).toByte())
        }
        var l = 0
        var r = digits.size - 1
        while (r >= 0 && digits[r] == 0.toByte())
            --r
        while (l <= r && digits[l] == 0.toByte()) {
            ++l
            ++order
        }
        if (l <= r)
            digits = digits.subList(l, r + 1)
        else {
            digits = mutableListOf(0)
            order = 0
            sign = true
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (!sign)
            sb.append('-')
        for (d in digits)
            sb.append(DIGITS[d.toInt()])
        if (order != 0.toLong()) {
            sb.append('E')
            sb.append(order.toString())
        }
        return sb.toString()
    }
}