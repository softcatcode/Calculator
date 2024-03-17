package com.codingeveryday.calcapp.domain.entities

private val invalidNumberException = Exception("Number is invalid")

class Number: Expression {
    var int: MutableList<Byte>
    var float: MutableList<Byte>
    var sign: Boolean
    var base: Int

    constructor(number: String, base: Int = 10) {
        if (number.isBlank())
            throw invalidNumberException
        sign = true
        var value = number
        if (number[0] == operation[SUB_ID]) {
            sign = false
            value = value.substring(1)
        }
        val pointInd = value.indexOf(POINT)
        val intBuilder = StringBuilder()
        for (i in 0 until pointInd)
            if (value[i].isDigit())
                intBuilder.append(value[i])
        int = strToDigitSequence(intBuilder.toString())
        if (int.isEmpty())
            throw invalidNumberException
        val floatBuilder = StringBuilder()
        for (i in pointInd + 1 until value.length)
            if (value[i].isDigit())
                floatBuilder.append(value[i])
        float = strToDigitSequence(floatBuilder.toString())
        this.base = base
    }

    constructor(int: MutableList<Byte>, float: MutableList<Byte>, sign: Boolean, base: Int = 10) {
        var i = 0
        while (i < int.size && int[i] == 0.toByte())
            ++i
        if (i == int.size)
            --i
        this.int = int.subList(i, int.size)
        while (i >= 0 && float[i] == 0.toByte())
            --i
        this.float = float.subList(0, i + 1)
        this.base = base
        this.sign = sign
    }

    constructor(int: String, float: String, sign: Boolean, base: Int = 10): this(
        strToDigitSequence(int),
        strToDigitSequence(float),
        sign,
        base
    )

    override fun toString() = "${if (sign) "" else "-"}$int.$float"
}