package com.codingeveryday.calcapp.domain.entities

import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.POINT

private val invalidNumberException = Exception("Number is invalid")

class Number: Expression {
    var digits: MutableList<Byte>
    var order: Int
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
                order -= digits.size
            else
                digits.add(DIGITS.indexOf(value[i]).toByte())
        }
        Number(digits, order, sign, base).let {
            digits = it.digits
            order = it.order
        }
    }

    constructor(digits: MutableList<Byte>, order: Int, sign: Boolean, base: Int) {
        this.base = base
        var r = digits.size - 1
        val zero = 0.toByte()
        while (r >= 0 && digits[r] == zero)
            --r
        var l = 0
        while (l <= r && digits[l] == zero)
            ++l
        if (l > r) {
            this.digits = mutableListOf(0)
            this.order = 0
            this.sign = true
        } else {
            this.digits = digits.subList(l, r + 1)
            this.order = order + l
            this.sign = sign
        }
    }

    fun toString(normal: Boolean = true): String {
        if (normal) {
            val sb = StringBuilder( if (sign) "" else "-" )
            for (d in digits)
                sb.append(DIGITS[d.toInt()])
            if (order != 0) {
                sb.append('e')
                sb.append(order.toString())
            }
            return sb.toString()
        }
        if (order >= 0) {
            val sb = StringBuilder( if (sign) "" else "-" )
            for (i in digits.lastIndex downTo 0)
                sb.append(DIGITS[digits[i].toInt()])
            for (i in 0 until order)
                sb.append('0')
            return sb.toString()
        }
        val sb = StringBuilder( if (sign) "" else "-" )
        for (i in digits.lastIndex downTo -order)
            sb.append(DIGITS[digits[i].toInt()])
        if (sb.isEmpty())
            sb.append('0')
        sb.append(POINT)
        for (i in -order - 1 downTo 0)
            sb.append(DIGITS[digits[i].toInt()])
        return sb.toString()
    }
}