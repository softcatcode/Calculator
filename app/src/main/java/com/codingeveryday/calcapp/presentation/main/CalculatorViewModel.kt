package com.codingeveryday.calcapp.presentation.main

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.data.CalcService
import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.BracketType
import com.codingeveryday.calcapp.domain.entities.HistoryItem
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import com.codingeveryday.calcapp.domain.useCases.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
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
        Timber.i("${this::class.simpleName}: exception is thrown when calculating: $msg.")
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
                Timber.i("Calculation is launched.")
                val result = calculateUseCase(state.expr, baseVal, state.angleUnit)
                updateHistory(state.expr, result, baseVal)
                withContext(Dispatchers.Main) {
                    setExpr(result)
                }
            }
        } else if (!CalcService.running) {
            context?.let {
                Timber.i("Foreground calculation is launching.")
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
        Timber.i("${this::class.simpleName}.setExpr($expr)")
        exprBuilder.setExpression(expr)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun backspace() {
        Timber.i("${this::class.simpleName}.backspace()")
        exprBuilder.backspace()
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun clear() {
        Timber.i("${this::class.simpleName}.clear()")
        exprBuilder.clear()
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun appendDigit(d: Char) {
        Timber.i("${this::class.simpleName}.appendDigit($d)")
        exprBuilder.addDigit(d)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun openBracket(type: BracketType) {
        Timber.i("${this::class.simpleName}.openBracket($type)")
        exprBuilder.addBracket(type)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun addOperation(operation: Char) {
        Timber.i("${this::class.simpleName}.addOperation($operation)")
        exprBuilder.addOperation(operation)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun addFunction(name: String) {
        Timber.i("${this::class.simpleName}.addFunction($name)")
        exprBuilder.addFunction(name)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun addAbsStick() {
        Timber.i("${this::class.simpleName}.addAbsStick()")
        exprBuilder.addAbs()
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun addPoint() {
        Timber.i("${this::class.simpleName}.addPoint()")
        exprBuilder.addPoint()
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun addConstant(name: String) {
        Timber.i("${this::class.simpleName}.addConstant($name)")
        exprBuilder.addConstant(name)
        _state.value = _state.value?.copy(expr = exprBuilder.get())
    }
    fun switchRadDeg() {
        Timber.i("${this::class.simpleName}.switchRadDeg()")
        val unit = if (state.value?.angleUnit == AngleUnit.Radians)
            AngleUnit.Degree else AngleUnit.Radians
        _state.value = _state.value?.copy(angleUnit = unit)
    }

    fun clearHistory() {
        Timber.i("${this::class.simpleName}.clearHistory()")
        viewModelScope.launch(Dispatchers.IO) {
            clearHistoryUseCase()
        }
    }
    fun removeHistoryItem(index: Int) {
        Timber.i("${this::class.simpleName}.removeHistoryItem($index)")
        viewModelScope.launch(Dispatchers.IO) {
            val list = history.value ?: listOf()
            if (index in 0..list.lastIndex)
                removeHistoryItemUseCase(list[index].id)
        }
    }

    fun updateExpression() {
        Timber.i("${this::class.simpleName}.updateExpression()")
        _state.value = state.value?.copy(expr = exprBuilder.get())
    }

    private fun updateHistory(expr: String, result: String, base: Int) {
        Timber.i("${this::class.simpleName}.updateHistory($expr, $result, $base)")
        if (expr == result)
            return
        viewModelScope.launch(Dispatchers.IO) {
            addHistoryItemUseCase(HistoryItem(expr, result, base))
        }
    }
}