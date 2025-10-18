package com.codingeveryday.calcapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softcat.domain.entities.HistoryItem
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class HistoryManagerInstrumentationTest {

    private val application = InstrumentationRegistry
        .getInstrumentation()
        .targetContext
        .applicationContext as CalculatorApplication

    private val component = DaggerTestingComponent.factory().create(application)
    private val manager = component.getHistoryManager()
    private var latestData: List<HistoryItem>? = null

    @Before
    fun clearDatabase(): Unit = runBlocking {
        manager.clearAll()
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    val observer = Observer<List<HistoryItem>> { latestData = it }

    @Before
    fun init() {
        latestData = null
        manager.getHistoryList().observeForever(observer)
    }

    @Test
    fun deleteHistoryItem(): Unit = runBlocking {
        val items = listOf(
            HistoryItem(
                expr = "0.5×(1/2+1/2-12/3)",
                result = "-1.5",
                base = 10,
                id = Random.nextInt(100, 1000)
            ),
            HistoryItem(
                expr = "1+1",
                result = "2",
                base = 3,
                id = Random.nextInt(100, 1000)
            )
        )
        items.forEach { manager.addItem(it) }

        manager.removeItem(items[0].id)

        val result = latestData!!
        assertEquals(result.size, 1)
        assertEquals(result.first(), items[1])
    }

    @Test
    fun addHistoryItem(): Unit = runBlocking {
        val items = listOf(
            HistoryItem(
                expr = "0.5×(1/2+1/2-12/3)",
                result = "-1.5",
                base = 10,
                id = Random.nextInt(100, 1000)
            ),
            HistoryItem(
                expr = "1+1",
                result = "2",
                base = 3,
                id = Random.nextInt(100, 1000)
            )
        )

        items.forEach { manager.addItem(it) }

        val result = latestData!!
        assertEquals(result.size, 2)
        assert(
            result[0] == items[0] && result[1] == items[1] ||
            result[1] == items[0] && result[0] == items[1]
        )
    }

    @Test
    fun clearAllHistory(): Unit = runBlocking {
        val items = listOf(
            HistoryItem(
                expr = "0.5×(1/2+1/2-12/3)",
                result = "-1.5",
                base = 10,
                id = Random.nextInt(100, 1000)
            ),
            HistoryItem(
                expr = "1+1",
                result = "2",
                base = 3,
                id = Random.nextInt(100, 1000)
            )
        )
        items.forEach { manager.addItem(it) }

        manager.clearAll()

        val result = latestData!!
        assert(result.isEmpty())
    }
}