package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import javax.inject.Inject

class AppendDigitUseCase @Inject constructor(private val repository: CalculationInterface) {
    operator fun invoke(c: Char) {
        repository.appendDigit(c)
    }
}