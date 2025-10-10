package com.softcat.benchmark_algo

import android.util.Log
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

/**
 * Benchmark, which will execute on an Android device.
 *
 * The body of [BenchmarkRule.measureRepeated] is measured in a loop, and Studio will
 * output the result. Modify your code to see how it affects performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private fun generateNumber(): String {
        val sb = StringBuilder("1")
        repeat(100) {
            sb.append((Random.nextInt(0, 9) + '0'.code).toChar())
        }
        return sb.toString()
    }

    private val numbers = List(10) { generateNumber() }

    @Test
    fun mulTimeTest() {
        benchmarkRule.measureRepeated {
            
        }
    }
}