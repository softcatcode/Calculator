package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import javax.inject.Inject

class PopBackUseCase @Inject constructor(private val repository: CalculationInterface) {
    operator fun invoke() {
        repository.popBack();
    }
}