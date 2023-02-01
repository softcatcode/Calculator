package com.codingeveryday.calcapp.presentation

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codingeveryday.calcapp.data.CalculationImplementation
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.useCases.*
import javax.inject.Inject

class CalculatorViewModel @Inject constructor(
    private val repository: CalculationInterface
): ViewModel() {

    private val calculateUseCase = CalculateUseCase(repository)
    private val getResUseCase = GetCalcResultUseCase(repository)
    private val appendUseCase = AppendDigitUseCase(repository)
    private val checkExprUseCase = CheckExprUseCase(repository)
    private val parseExprUseCase = ParseExprUseCase(repository)
    private val openBracketUseCase = OpenBracketUseCase(repository)
    private val addOperationUseCase = AddOperationUseCase(repository)
    private val addFuncUseCase = AddFunctionUseCase(repository)
    private val addAbsStickUseCase = AddAbsStickUseCase(repository)
    private val addDotUseCase = AddDotUseCase(repository)
    private val addPiUseCase = AddPiUseCase(repository)
    private val switchRadDegUseCase = SwitchRadDegUseCase(repository)
    private val clearSolutionUseCase = ClearSolutionUseCase(repository)
    private val clearExprUseCase = ClearExprUseCase(repository)
    private val setExprUseCase = SetExprUseCase(repository)
    private val popBackUseCase = PopBackUseCase(repository)

    val solution = getResUseCase.getSolution()
    val expr = getResUseCase.getExpr()

    private var _exprCorrect = MutableLiveData<Boolean>()
    val exprCorrect: LiveData<Boolean>
        get() = _exprCorrect

    private var _baseCorrect = MutableLiveData<Boolean>()
    val baseCorrect: LiveData<Boolean>
        get() = _baseCorrect

    fun resetErrors() {
        _baseCorrect.value = true
        _exprCorrect.value = true
    }

    fun calculate(base: String) {
        clearSolutionUseCase()
        if (base.isEmpty() || !base.isDigitsOnly()) {
            _baseCorrect.value = false
            return
        }
        val baseVal = base.toInt()
        if (baseVal < 2 || baseVal > 36) {
            _baseCorrect.value = false
            return
        }
        if (!checkExprUseCase(baseVal)) {
            _exprCorrect.value = false
            return
        }
        parseExprUseCase()
        calculateUseCase(baseVal)
        val expr = getResUseCase.getExpr().value!!
        if (expr[0] in "×/") {
            _exprCorrect.value = false
            clearSolutionUseCase()
        }
        for (i in 1 until expr.length) {
            if (expr[i] in "+-×/") {
                _exprCorrect.value = false
                clearSolutionUseCase()
            }
        }
    }

    fun popBack() {
        popBackUseCase()
    }

    fun clear() {
        clearExprUseCase()
    }

    fun appendDigit(c: Char) {
        if (c in '0'..'9')
            appendUseCase(c)
    }

    fun openBracket(bracketType: Char) {
        openBracketUseCase(bracketType)
    }

    fun addOperation(operation: Char) {
        addOperationUseCase(operation)
    }

    fun addFunction(funcId: Int) {
        addFuncUseCase(funcId)
    }

    fun addAbsStick() {
        addAbsStickUseCase()
    }

    fun addDot() {
        addDotUseCase()
    }

    fun addPi() {
        addPiUseCase()
    }

    fun switchRadDeg() {
        switchRadDegUseCase()
    }

    fun setExpr(newExpr: String) {
        setExprUseCase(newExpr)
    }
}