package com.codingeveryday.calcapp

import com.codingeveryday.calcapp.data.database.HistoryItemDbModel
import com.codingeveryday.calcapp.domain.entities.HistoryItem
import com.codingeveryday.calcapp.domain.entities.Number
import kotlin.random.Random

object TestDataGenerator {

    private val expressions = listOf(
        "23+5/2" to "25.5",
        //"3+20-4.32×log₂128" to "62.76",
        "sin(π)-π×cos(π/2)" to "0",
        "1+√(10!/70)" to "721",
        //"||3+(-34)|-|4×log₂(0.0625)||" to "15",
        "||1/3+(-4/3)|-|4×ln(1)-2||" to "1",
        "0.5×(1/2+1/2-12/3)" to "-1.5",
        "0.625 + 0.375 - 0.1×sin(3×π/2)" to "1.1",
    )

    fun getRandomHistoryItem(): HistoryItem {
        val data = expressions[Random.nextInt(0, expressions.size)]
        return HistoryItem(
            expr = data.first,
            result = data.second,
            base = getRandomBase(),
            id = Random.nextInt(10, 100)
        )
    }

    fun getRandomHistory() = List<HistoryItemDbModel>(Random.nextInt(1, 11)) {
        val data = expressions[Random.nextInt(0, expressions.size)]
        HistoryItemDbModel(
            expr = data.first,
            result = data.second,
            base = getRandomBase(),
            id = Random.nextInt(10, 100)
        )
    }

    fun getRandomHistoryItemList() = List(Random.nextInt(1, 10)) { getRandomHistoryItem() }

    fun getRandomExpr() = expressions[Random.nextInt(0, expressions.size)].first

    fun getRandomBase() = Random.nextInt(10, 37)

    fun getRandomString(length: Int): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val sb = StringBuilder()
        repeat(length) {
            sb.append(charPool[Random.nextInt(0, charPool.size)])
        }
        return sb.toString()
    }

    fun getRandomDecimalNumber(): String {
        var result = 0L
        val n = Random.nextInt(1, 15)
        repeat(n) {
            val d = Random.nextInt(0, 10)
            result = result * 10 + d
        }
        return result.toString()
    }

    fun getRandomNumber() = Number(getRandomDecimalNumber(), getRandomBase())
}