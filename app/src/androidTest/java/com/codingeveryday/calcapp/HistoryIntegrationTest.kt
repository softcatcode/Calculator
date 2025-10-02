package com.codingeveryday.calcapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codingeveryday.calcapp.domain.entities.HistoryItem
import com.codingeveryday.calcapp.domain.useCases.AddHistoryItemUseCase
import com.codingeveryday.calcapp.domain.useCases.ClearHistoryUseCase
import com.codingeveryday.calcapp.domain.useCases.GetHistoryListUseCase
import com.codingeveryday.calcapp.domain.useCases.RemoveHistoryItemUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import kotlin.collections.first
import kotlin.collections.forEach
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class HistoryIntegrationTest {

    private val application = InstrumentationRegistry
        .getInstrumentation()
        .targetContext
        .applicationContext as CalculatorApplication

    private val component = DaggerTestingComponent.factory().create(application)
    private val manager = component.getHistoryManager()
    private var latestData: List<HistoryItem>? = null

    private val addUseCase = AddHistoryItemUseCase(manager)
    private val removeUseCase = RemoveHistoryItemUseCase(manager)
    private val clearUseCase = ClearHistoryUseCase(manager)
    private val getUseCase = GetHistoryListUseCase(manager)

    @Before
    fun clearDatabase(): Unit = runBlocking {
        clearUseCase()
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    val observer = Observer<List<HistoryItem>> { latestData = it }

    @Before
    fun init() {
        latestData = null
        getUseCase().observeForever(observer)
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
        items.forEach { addUseCase(it) }

        removeUseCase(items[0].id)

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

        items.forEach { addUseCase(it) }

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
        items.forEach { addUseCase(it) }

        clearUseCase()

        val result = latestData!!
        assert(result.isEmpty())
    }
}