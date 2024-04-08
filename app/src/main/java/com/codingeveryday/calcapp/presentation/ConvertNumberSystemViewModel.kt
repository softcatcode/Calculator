package com.codingeveryday.calcapp.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingeveryday.calcapp.data.states.NumberSystemTranslationState
import com.codingeveryday.calcapp.domain.useCases.NumberSystemTranslationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.codingeveryday.calcapp.domain.entities.Number
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConvertNumberSystemViewModel @Inject constructor(
    private val translateUseCase: NumberSystemTranslationUseCase
): ViewModel() {

    var firstBase by mutableStateOf("")
        private set
    var secondBase by mutableStateOf("")
        private set
    var number by mutableStateOf("")
        private set
    var translationDir by mutableStateOf(true)
        private set

    private val state = MutableStateFlow(NumberSystemTranslationState())
    val uiState: StateFlow<NumberSystemTranslationState> = state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

    }

    fun updateFirstBase(newValue: String) { firstBase = newValue; }
    fun updateSecondBase(newValue: String) { secondBase = newValue }
    fun updateNumber(newValue: String) { number = newValue }
    fun switchTranslationDir() { translationDir = !translationDir }

    private fun launchTranslation(number: Number, secondBase: Int) {
        viewModelScope.launch(Dispatchers.Default + exceptionHandler) {
            val result = translateUseCase(number, secondBase)
            withContext(Dispatchers.Main) {
                state.update {
                    state.value.copy(
                        firstBaseError = false,
                        secondBaseError = false,
                        numberError = false,
                        result = result.toString()
                    )
                }
            }
        }
    }

    fun translate() {
        val firstBase = try { firstBase.toInt() } catch (_: Exception) { 0 }
        val secondBase = try { secondBase.toInt() } catch (_: Exception) { 0 }
        val firstBaseError = firstBase !in 2..36
        val secondBaseError = secondBase !in 2..36
        val numberBase = if (translationDir) firstBase else secondBase
        val destBase = if (translationDir) secondBase else firstBase
        val number = try { Number(number, numberBase) } catch(e: Exception) { null }
        if (!firstBaseError && !secondBaseError && number != null) {
            launchTranslation(number, destBase)
        } else {
            state.update {
                state.value.copy(
                    firstBaseError = firstBaseError,
                    secondBaseError = secondBaseError,
                    numberError = number == null,
                    result = ""
                )
            }
        }
    }
}