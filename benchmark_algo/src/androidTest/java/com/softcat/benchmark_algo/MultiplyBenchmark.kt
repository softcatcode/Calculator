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
class MultiplyBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private fun generateNumber(length: Int = 100): String {
        val sb = StringBuilder("")
        repeat(length * 5) {
            sb.append((Random.nextInt(0, 9) + '0'.code).toChar())
        }
        if (sb[0] == '0')
            sb[0] = '1'
        return sb.toString()
    }

    private val calculator1 = MathImplementation(ConstantProvider())
    private val calculator2 = FastMathImplementation(ConstantProvider())

    private fun measure(length: Int, optimised: Boolean = false) {
        val a = Number(generateNumber(length), 10)
        val b = Number(generateNumber(length), 10)
        val calculator: MathInterface = if (optimised) calculator2 else calculator1
        benchmarkRule.measureRepeated {
            calculator.mul(a, b)
        }
    }

    @Test
    fun mulTimeTest_1() { measure(5) }
    @Test
    fun mulTimeTest_2() { measure(10) }
    @Test
    fun mulTimeTest_3() { measure(50) }
    @Test
    fun mulTimeTest_4() { measure(100) }
    @Test
    fun mulTimeTest_5() { measure(200) }
    @Test
    fun mulTimeTest_6() { measure(300) }
    @Test
    fun mulTimeTest_7() { measure(400) }
    @Test
    fun mulTimeTest_8() { measure(500) }
    @Test
    fun mulTimeTest_9() { measure(600) }
    @Test
    fun mulTimeTest_10() { measure(700) }
    @Test
    fun mulTimeTest_11() { measure(800) }
    @Test
    fun mulTimeTest_12() { measure(900) }
    @Test
    fun mulTimeTest_13() { measure(1000) }
    @Test
    fun mulTimeTest_14() { measure(1100) }
    @Test
    fun mulTimeTest_15() { measure(1200) }
    @Test
    fun mulTimeTest_16() { measure(1300) }
    @Test
    fun mulTimeTest_17() { measure(1400) }
    @Test
    fun mulTimeTest_18() { measure(1500) }
    @Test
    fun mulTimeTest_19() { measure(1600) }
    @Test
    fun mulTimeTest_20() { measure(1700) }
    @Test
    fun mulTimeTest_21() { measure(1800) }
    @Test
    fun mulTimeTest_22() { measure(1900) }
    @Test
    fun mulTimeTest_23() { measure(2000) }

    @Test
    fun mulTimeTest_optimised_1() { measure(5, true) }
    @Test
    fun mulTimeTest_optimised_2() { measure(10, true) }
    @Test
    fun mulTimeTest_optimised_3() { measure(50, true) }
    @Test
    fun mulTimeTest_optimised_4() { measure(100, true) }
    @Test
    fun mulTimeTest_optimised_5() { measure(200, true) }
    @Test
    fun mulTimeTest_optimised_6() { measure(300, true) }
    @Test
    fun mulTimeTest_optimised_7() { measure(400, true) }
    @Test
    fun mulTimeTest_optimised_8() { measure(500, true) }
    @Test
    fun mulTimeTest_optimised_9() { measure(600, true) }
    @Test
    fun mulTimeTest_optimised_10() { measure(700, true) }
    @Test
    fun mulTimeTest_optimised_11() { measure(800, true) }
    @Test
    fun mulTimeTest_optimised_12() { measure(900, true) }
    @Test
    fun mulTimeTest_optimised_13() { measure(1000, true) }
    @Test
    fun mulTimeTest_optimised_14() { measure(1100, true) }
    @Test
    fun mulTimeTest_optimised_15() { measure(1200, true) }
    @Test
    fun mulTimeTest_optimised_16() { measure(1300, true) }
    @Test
    fun mulTimeTest_optimised_17() { measure(1400, true) }
    @Test
    fun mulTimeTest_optimised_18() { measure(1500, true) }
    @Test
    fun mulTimeTest_optimised_19() { measure(1600, true) }
    @Test
    fun mulTimeTest_optimised_20() { measure(1700, true) }
    @Test
    fun mulTimeTest_optimised_21() { measure(1800, true) }
    @Test
    fun mulTimeTest_optimised_22() { measure(1900, true) }
    @Test
    fun mulTimeTest_optimised_23() { measure(2000, true) }
}