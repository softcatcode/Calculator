package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import javax.inject.Inject

class SetExprUseCase @Inject constructor(private val repository: CalculationInterface) {
    operator fun invoke(newExpr: String) {
        repository.setExpr(newExpr)
    }
}