package com.codingeveryday.calcapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codingeveryday.calcapp.data.ExpressionBuilder
import javax.inject.Inject

class KeyboardFragmentViewModel @Inject constructor(
    private val exprBuilder: ExpressionBuilder
): ViewModel() {
    private val _textFieldState = MutableLiveData("")
    val textFieldState: LiveData<String> = _textFieldState

    fun addDigit(digit: Char) {
        exprBuilder.addDigit(digit)
        _textFieldState.value = exprBuilder.get()
    }
}