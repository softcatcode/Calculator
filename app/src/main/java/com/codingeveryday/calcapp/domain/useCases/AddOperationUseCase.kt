package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import javax.inject.Inject

class AddOperationUseCase @Inject constructor(private val repository: CalculationInterface) {
    operator fun invoke(operation: Char) {
        repository.addOperation(operation)
    }
}