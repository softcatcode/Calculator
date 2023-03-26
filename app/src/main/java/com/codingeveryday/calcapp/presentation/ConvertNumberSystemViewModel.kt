package com.codingeveryday.calcapp.presentation

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codingeveryday.calcapp.domain.interfaces.TransformationInterface
import com.codingeveryday.calcapp.domain.useCases.CheckNumberUseCase
import com.codingeveryday.calcapp.domain.useCases.GetTransformationResultUseCase
import com.codingeveryday.calcapp.domain.useCases.NumberSystemTranslationUseCase
import javax.inject.Inject

class ConvertNumberSystemViewModel @Inject constructor(
    private val repository: TransformationInterface
): ViewModel() {

    private val translateUseCase = NumberSystemTranslationUseCase(repository)
    private val getTransResultUseCase = GetTransformationResultUseCase(repository)
    private val checkNumberUseCase = CheckNumberUseCase(repository)

    private var _baseSourceCorrect = MutableLiveData<Unit>()
    val baseSourceCorrect: LiveData<Unit>
        get() = _baseSourceCorrect

    private var _baseDestCorrect = MutableLiveData<Unit>()
    val baseDestCorrect: LiveData<Unit>
        get() = _baseDestCorrect

    private var _numberCorrect = MutableLiveData<Unit>()
    val numberCorrect: LiveData<Unit>
        get() = _numberCorrect

    val number = getTransResultUseCase()

    fun translate(a: String, base1: String, base2: String) {
        var check = true
        if (base1 == "" || !base1.isDigitsOnly()) {
            _baseSourceCorrect.value = Unit
            check = false
        }
        if (base2 == "" || !base2.isDigitsOnly()) {
            _baseDestCorrect.value = Unit
            check = false
        }
        if (!check)
            return
        val baseSource = base1.toInt()
        val baseDest = base2.toInt()
        if (!checkNumberUseCase(a, baseSource)) {
            _numberCorrect.value = Unit
            return
        }
        Log.i("mumu", "translating |$a|");
        translateUseCase(a, baseSource, baseDest)
    }
}