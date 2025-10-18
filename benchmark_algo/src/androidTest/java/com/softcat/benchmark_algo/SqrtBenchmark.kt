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
class SqrtBenchmark {

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

    private val calculator1 = MathImplementation(ConstantProvider())
    private val calculator2 = FastMathImplementation(ConstantProvider())

    private fun measure(length: Int, optimised: Boolean = false) {
        val a = Number(generateNumber(length), 10)
        val calculator: MathInterface = if (optimised) calculator2 else calculator1
        benchmarkRule.measureRepeated {
            calculator.sqrt(a)
        }
    }

    @Test
    fun sqrtTimeTest_1() { measure(5) }
    @Test
    fun sqrtTimeTest_2() { measure(10) }
    @Test
    fun sqrtTimeTest_3() { measure(15) }
    @Test
    fun sqrtTimeTest_4() { measure(20) }
    @Test
    fun sqrtTimeTest_5() { measure(25) }
    @Test
    fun sqrtTimeTest_6() { measure(30) }
    @Test
    fun sqrtTimeTest_7() { measure(35) }
    @Test
    fun sqrtTimeTest_8() { measure(40) }
    @Test
    fun sqrtTimeTest_9() { measure(45) }
    @Test
    fun sqrtTimeTest_10() { measure(50) }
    @Test
    fun sqrtTimeTest_11() { measure(55) }
    @Test
    fun sqrtTimeTest_12() { measure(60) }
    @Test
    fun sqrtTimeTest_13() { measure(65) }
    @Test
    fun sqrtTimeTest_14() { measure(70) }
    @Test
    fun sqrtTimeTest_15() { measure(75) }
    @Test
    fun sqrtTimeTest_16() { measure(80) }
    @Test
    fun sqrtTimeTest_17() { measure(85) }
    @Test
    fun sqrtTimeTest_18() { measure(90) }
    @Test
    fun sqrtTimeTest_19() { measure(95) }
    @Test
    fun sqrtTimeTest_20() { measure(100) }

    @Test
    fun sqrtTimeTest_optimised_1() { measure(5, true) }
    @Test
    fun sqrtTimeTest_optimised_2() { measure(10, true) }
    @Test
    fun sqrtTimeTest_optimised_3() { measure(15, true) }
    @Test
    fun sqrtTimeTest_optimised_4() { measure(20, true) }
    @Test
    fun sqrtTimeTest_optimised_5() { measure(25, true) }
    @Test
    fun sqrtTimeTest_optimised_6() { measure(30, true) }
    @Test
    fun sqrtTimeTest_optimised_7() { measure(35, true) }
    @Test
    fun sqrtTimeTest_optimised_8() { measure(40, true) }
    @Test
    fun sqrtTimeTest_optimised_9() { measure(45, true) }
    @Test
    fun sqrtTimeTest_optimised_10() { measure(50, true) }
    @Test
    fun sqrtTimeTest_optimised_11() { measure(55, true) }
    @Test
    fun sqrtTimeTest_optimised_12() { measure(60, true) }
    @Test
    fun sqrtTimeTest_optimised_13() { measure(65, true) }
    @Test
    fun sqrtTimeTest_optimised_14() { measure(70, true) }
    @Test
    fun sqrtTimeTest_optimised_15() { measure(75, true) }
    @Test
    fun sqrtTimeTest_optimised_16() { measure(80, true) }
    @Test
    fun sqrtTimeTest_optimised_17() { measure(85, true) }
    @Test
    fun sqrtTimeTest_optimised_18() { measure(90, true) }
    @Test
    fun sqrtTimeTest_optimised_19() { measure(95, true) }
    @Test
    fun sqrtTimeTest_optimised_20() { measure(100, true) }
}