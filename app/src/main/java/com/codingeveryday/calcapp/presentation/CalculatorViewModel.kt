package com.codingeveryday.calcapp.presentation

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingeveryday.calcapp.data.CalcService
import com.codingeveryday.calcapp.data.states.CalculatorViewModelState
import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.HistoryItem
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import com.codingeveryday.calcapp.domain.useCases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculatorViewModel @Inject constructor(
    private val calculateUseCase: CalculateUseCase,
    private val getHistoryListUseCase: GetHistoryListUseCase,
    private val removeHistoryItemUseCase: RemoveHistoryItemUseCase,
    private val addHistoryItemUseCase: AddHistoryItemUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase,
    private val exprBuilder: ExpressionBuilderInterface
): ViewModel() {

    private var _state = MutableLiveData(CalculatorViewModelState())
    val state: LiveData<CalculatorViewModelState>
        get() = _state

    val errorEvent = MutableLiveData("")

    var solution = ""
        private set(value) { field = value }

    fun calculate(
        base: String,
        angleUnit: AngleUnit = AngleUnit.Radians,
        foregroundMode: Boolean = false,
        context: Context? = null
    ) {
        val state = _state.value ?: CalculatorViewModelState()
        val baseVal = base.toInt()
        try {
            assert(baseVal in 1..36)
            if (!foregroundMode) {
                viewModelScope.launch(Dispatchers.Default) {
                    val result = calculateUseCase(state.expr, baseVal, state.angleUnit)
                    solution = result.second
                    updateHistory(state.expr, result.first)
                    withContext(Dispatchers.Main) {
                        setExpr(result.first)
                    }
                }
            } else if (!CalcService.running) {
                context?.let {
                    ContextCompat.startForegroundService(
                        it,
                        CalcService.newIntent(it, state.expr, baseVal, angleUnit)
                    )
                }
            }
        } catch (_: Exception) {
            errorEvent.value = "Error"
        }
    }

    fun setExpr(expr: String) {
        exprBuilder.setExpression(expr)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun addConstant(a: String) {
        exprBuilder.addConstant(a)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun backspace() {
        exprBuilder.backspace()
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun clear() {
        exprBuilder.clear()
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun appendDigit(d: Char) {
        exprBuilder.addDigit(d)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun openBracket(br: Char) {
        exprBuilder.addBracket(br)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun addOperation(operation: Char) {
        exprBuilder.addOperation(operation)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun addFunction(name: String) {
        exprBuilder.addFunction(name)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun addAbsStick() {
        exprBuilder.addAbs()
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun addPoint() {
        exprBuilder.addPoint()
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun addPi() {
        exprBuilder.addConstant(ExpressionBuilderInterface.PI)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun switchRadDeg() {
        val unit = if (state.value?.angleUnit == AngleUnit.Radians)
            AngleUnit.Degree else AngleUnit.Radians
        _state.value = _state.value?.copy(angleUnit = unit)
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            clearHistoryUseCase()
            _state.postValue(_state.value?.copy(history = getHistoryListUseCase()))
        }
    }
    fun removeHistoryItem(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = _state.value?.history ?: listOf()
            val maxIndex = list.size - 1
            if (index in 0..maxIndex) {
                removeHistoryItemUseCase(list[index].id)
            }
            _state.postValue(_state.value?.copy(history = getHistoryListUseCase()))
        }
    }
    private fun updateHistory(expr: String, result: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addHistoryItemUseCase(HistoryItem(expr, result))
            _state.postValue(_state.value?.copy(history = getHistoryListUseCase()))
        }
    }
}