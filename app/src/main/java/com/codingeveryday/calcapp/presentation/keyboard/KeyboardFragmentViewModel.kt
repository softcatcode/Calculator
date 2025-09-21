package com.codingeveryday.calcapp.presentation.keyboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import timber.log.Timber
import javax.inject.Inject

class KeyboardFragmentViewModel @Inject constructor(
    private val exprBuilder: ExpressionBuilderInterface
): ViewModel() {
    private val _textFieldState = MutableLiveData(exprBuilder.get())
    val textFieldState: LiveData<String> = _textFieldState

    fun addDigit(digit: Char) {
        Timber.i("${this::class.simpleName}.addDigit($digit)")
        exprBuilder.addDigit(digit)
        _textFieldState.value = exprBuilder.get()
    }

    fun backspace() {
        Timber.i("${this::class.simpleName}.backspace()")
        exprBuilder.backspace()
        _textFieldState.value = exprBuilder.get()
    }
}