package com.codingeveryday.calcapp.presentation

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.data.CalcService
import com.codingeveryday.calcapp.data.states.CalculatorViewModelState
import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.HistoryItem
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import com.codingeveryday.calcapp.domain.useCases.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculatorViewModel @Inject constructor(
    private val calculateUseCase: CalculateUseCase,
    private val removeHistoryItemUseCase: RemoveHistoryItemUseCase,
    private val addHistoryItemUseCase: AddHistoryItemUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase,
    private val exprBuilder: ExpressionBuilderInterface,
    getHistoryListUseCase: GetHistoryListUseCase,
    application: Application
): AndroidViewModel(application) {

    private val _state = MutableLiveData(CalculatorViewModelState(""))
    val state: LiveData<CalculatorViewModelState> = _state

    val errorEvent = MutableLiveData("")

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val msg = getApplication<Application>().getString(R.string.calc_error_message)
        errorEvent.postValue(msg)
    }

    val history = getHistoryListUseCase()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _state.postValue(CalculatorViewModelState())
        }
    }

    fun calculate(
        base: String,
        angleUnit: AngleUnit = AngleUnit.Radians,
        foregroundMode: Boolean = false,
        context: Context? = null
    ) {
        val state = _state.value ?: CalculatorViewModelState()
        val baseVal = base.toInt()
        assert(baseVal in 1..36)
        if (!foregroundMode) {
            viewModelScope.launch(Dispatchers.Default + exceptionHandler) {
                val result = calculateUseCase(state.expr, baseVal, state.angleUnit)
                updateHistory(state.expr, result, baseVal)
                withContext(Dispatchers.Main) {
                    setExpr(result)
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
    }

    fun resetError() {
        errorEvent.value = ""
    }

    fun setExpr(expr: String) {
        exprBuilder.setExpression(expr)
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
    fun openBracket(type: CalculationInterface.Companion.BracketType) {
        exprBuilder.addBracket(type)
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
    fun addConstant(name: String) {
        exprBuilder.addConstant(name)
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
        }
    }
    fun removeHistoryItem(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = history.value ?: listOf()
            if (index in 0..list.lastIndex)
                removeHistoryItemUseCase(list[index].id)
        }
    }

    fun updateExpression() {
        _state.value = state.value?.copy(expr = exprBuilder.get())
    }

    private fun updateHistory(expr: String, result: String, base: Int) {
        if (expr == result)
            return
        viewModelScope.launch(Dispatchers.IO) {
            addHistoryItemUseCase(HistoryItem(expr, result, base))
        }
    }
}