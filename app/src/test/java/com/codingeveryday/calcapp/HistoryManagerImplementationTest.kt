package com.codingeveryday.calcapp

import com.codingeveryday.calcapp.TestDataGenerator.getRandomHistoryItem
import com.softcat.data.database.HistoryItemDao
import com.softcat.data.database.HistoryItemDbModel
import com.softcat.data.implementations.HistoryManagerImplementation
import com.softcat.data.mapper.HistoryItemMapper
import com.softcat.domain.entities.HistoryItem
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import kotlin.random.Random

class HistoryManagerImplementationTest {

    private val database = mock(HistoryItemDao::class.java)
    private val mapper = mock(HistoryItemMapper::class.java)
    private val manager = HistoryManagerImplementation(database, mapper)

    @Before
    fun resetMocks() {
        reset(database)
        reset(mapper)

        `when`(mapper.mapHistoryItemToDbModel(any())).thenAnswer { invocation ->
            val arg = invocation.arguments.first() as HistoryItem
            HistoryItemMapper().mapHistoryItemToDbModel(arg)
        }
        `when`(mapper.mapHistoryItemDbModelToItem(any())).thenAnswer { invocation ->
            val arg = invocation.arguments.first() as HistoryItemDbModel
            HistoryItemMapper().mapHistoryItemDbModelToItem(arg)
        }

        `when`(mapper.mapListDbModelToListEntity(any())).thenAnswer { invocation ->
            val arg = invocation.arguments.first() as List<HistoryItemDbModel>
            HistoryItemMapper().mapListDbModelToListEntity(arg)
        }
    }

    @Test
    fun deleteHistoryItem(): Unit = runBlocking {
        val id = Random.nextInt(1000, 10000)

        manager.removeItem(id)

        verify(database, times(1)).deleteItem(id)
    }

    @Test
    fun addHistoryItem(): Unit = runBlocking {
        val item = getRandomHistoryItem()
        val model = HistoryItemMapper().mapHistoryItemToDbModel(item)

        manager.addItem(item)

        verify(mapper, times(1)).mapHistoryItemToDbModel(item)
        verify(database, times(1)).addItem(model)
    }

    @Test
    fun clearAllHistory(): Unit = runBlocking {
        manager.clearAll()

        verify(database, times(1)).clear()
    }
}