package com.codingeveryday.calcapp.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.codingeveryday.calcapp.data.states.NumberSystemTranslationState
import com.codingeveryday.calcapp.domain.useCases.NumberSystemTranslationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.codingeveryday.calcapp.domain.entities.Number

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

    fun updateFirstBase(newValue: String) { firstBase = newValue; }
    fun updateSecondBase(newValue: String) { secondBase = newValue }
    fun updateNumber(newValue: String) { number = newValue }
    fun switchTranslationDir() { translationDir = !translationDir }

    fun translate() {
        val firstBase = firstBase.toInt()
        val secondBase = secondBase.toInt()
        val number = try { Number(number, firstBase) } catch(e: Exception) { null }
        val firstBaseError = firstBase in 2..36
        val secondBaseError = secondBase in 2..36
        val result = if (!firstBaseError && !secondBaseError && number != null) {
            try {
                if (translationDir)
                    translateUseCase(number, firstBase, secondBase)
                else
                    translateUseCase(number, secondBase, firstBase)
            } catch(_: Exception) {
                number
            }
        } else
            number
        state.update {
            state.value.copy(
                firstBaseError = firstBaseError,
                secondBaseError = secondBaseError,
                numberError = number != null,
                result = result.toString()
            )
        }
    }
}