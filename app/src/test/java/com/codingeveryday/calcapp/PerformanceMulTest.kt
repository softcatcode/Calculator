package com.codingeveryday.calcapp

import android.os.Debug
import android.util.Log
import com.codingeveryday.calcapp.data.core.implementations.ConstantProvider
import com.codingeveryday.calcapp.data.core.implementations.FastMathImplementation
import com.codingeveryday.calcapp.data.core.implementations.MathImplementation
import com.codingeveryday.calcapp.domain.entities.Number
import org.junit.Test
import kotlin.time.measureTime

class PerformanceMulTest {

    private val constantProvider = ConstantProvider()
    private val calculator1 = MathImplementation(constantProvider)
    private val calculator2 = FastMathImplementation(constantProvider)
    val memInfo = Debug.MemoryInfo()

    @Test
    fun testFactorial() {
        for (i in 0..200 step 10) {
            val num = Number(i.toString(), 10)
            val time1 = measureTime { calculator1.fac(num) }
            val time2 = measureTime { calculator2.fac(num) }
            Log.i("${this::class.simpleName}", "$time1 $time2")
        }
    }
}