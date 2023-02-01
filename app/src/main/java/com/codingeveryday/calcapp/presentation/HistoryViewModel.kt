package com.codingeveryday.calcapp.presentation

import androidx.lifecycle.*
import com.codingeveryday.calcapp.domain.HistoryItem
import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import com.codingeveryday.calcapp.domain.useCases.AddHistoryItemUseCase
import com.codingeveryday.calcapp.domain.useCases.ClearHistoryUseCase
import com.codingeveryday.calcapp.domain.useCases.GetHistoryListUseCase
import com.codingeveryday.calcapp.domain.useCases.RemoveHistoryItemUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val getListUseCase: GetHistoryListUseCase,
    private val removeItemUseCase: RemoveHistoryItemUseCase,
    private val addItemUseCase: AddHistoryItemUseCase,
    private val clearUseCase: ClearHistoryUseCase
): ViewModel() {

    val historyItemList = getListUseCase()

    fun remove(id: Int) {
        viewModelScope.launch {
            removeItemUseCase(id)
        }
    }

    fun add(expr: String, result: String) {
        viewModelScope.launch {
            addItemUseCase(HistoryItem(expr, result))
        }
    }

    fun clear() {
        viewModelScope.launch {
            clearUseCase()
        }
    }
}