package com.softcat.benchmark_algo

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softcat.data.core.implementations.ConstantProvider
import com.softcat.data.core.implementations.FastMathImplementation
import com.softcat.data.core.implementations.MathImplementation
import com.softcat.data.core.interfaces.MathInterface
import com.softcat.domain.entities.Number
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class TrigonometryBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private fun generateNumber(length: Int = 100): String {
        val sb = StringBuilder("")
        repeat(length) {
            sb.append((Random.nextInt(0, 9) + '0'.code).toChar())
        }
        if (sb[0] == '0')
            sb[0] = '1'
        return sb.toString()
    }

    private val calculator = MathImplementation(ConstantProvider())

    private fun measureSin(length: Int) {
        val a = Number(generateNumber(length), 10)
        benchmarkRule.measureRepeated {
            calculator.sin(a)
        }
    }

    private fun measureCos(length: Int) {
        val a = Number(generateNumber(length), 10)
        benchmarkRule.measureRepeated {
            calculator.cos(a)
        }
    }

    private fun measureTan(length: Int) {
        val a = Number(generateNumber(length), 10)
        benchmarkRule.measureRepeated {
            calculator.tan(a)
        }
    }

    private fun measureSqrt(length: Int) {
        val a = Number(generateNumber(length), 10)
        benchmarkRule.measureRepeated {
            calculator.sqrt(a)
        }
    }

    @Test
    fun calculationTime_sin_1() { measureSin(5) }
    @Test
    fun calculationTime_sin_2() { measureSin(10) }
    @Test
    fun calculationTime_sin_3() { measureSin(15) }
    @Test
    fun calculationTime_sin_4() { measureSin(20) }
    @Test
    fun calculationTime_sin_5() { measureSin(25) }
    @Test
    fun calculationTime_sin_6() { measureSin(30) }
    @Test
    fun calculationTime_sin_7() { measureSin(35) }
//    @Test
//    fun calculationTime_sin_8() { measureSin(40) }
//    @Test
//    fun calculationTime_sin_9() { measureSin(45) }
//    @Test
//    fun calculationTime_sin_10() { measureSin(50) }

    @Test
    fun calculationTime_cos_1() { measureCos(5) }
    @Test
    fun calculationTime_cos_2() { measureCos(10) }
    @Test
    fun calculationTime_cos_3() { measureCos(15) }
    @Test
    fun calculationTime_cos_4() { measureCos(20) }
    @Test
    fun calculationTime_cos_5() { measureCos(25) }
    @Test
    fun calculationTime_cos_6() { measureCos(30) }
    @Test
    fun calculationTime_cos_7() { measureCos(35) }
//    @Test
//    fun calculationTime_cos_8() { measureCos(40) }
//    @Test
//    fun calculationTime_cos_9() { measureCos(45) }
//    @Test
//    fun calculationTime_cos_10() { measureCos(50) }

    @Test
    fun calculationTime_Tan_1() { measureTan(5) }
    @Test
    fun calculationTime_Tan_2() { measureTan(10) }
    @Test
    fun calculationTime_Tan_3() { measureTan(15) }
    @Test
    fun calculationTime_Tan_4() { measureTan(20) }
    @Test
    fun calculationTime_Tan_5() { measureTan(25) }
    @Test
    fun calculationTime_Tan_6() { measureTan(30) }
    @Test
    fun calculationTime_Tan_7() { measureTan(35) }
//    @Test
//    fun calculationTime_Tan_8() { measureTan(40) }
//    @Test
//    fun calculationTime_Tan_9() { measureTan(45) }
//    @Test
//    fun calculationTime_Tan_10() { measureTan(50) }
}