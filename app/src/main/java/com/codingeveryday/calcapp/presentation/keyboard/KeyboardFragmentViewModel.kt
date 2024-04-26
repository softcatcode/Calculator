package com.codingeveryday.calcapp.presentation.keyboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import javax.inject.Inject

class KeyboardFragmentViewModel @Inject constructor(
    private val exprBuilder: ExpressionBuilderInterface
): ViewModel() {
    private val _textFieldState = MutableLiveData(exprBuilder.get())
    val textFieldState: LiveData<String> = _textFieldState

    fun addDigit(digit: Char) {
        exprBuilder.addDigit(digit)
        _textFieldState.value = exprBuilder.get()
    }

    fun backspace() {
        exprBuilder.backspace()
        _textFieldState.value = exprBuilder.get()
    }
}