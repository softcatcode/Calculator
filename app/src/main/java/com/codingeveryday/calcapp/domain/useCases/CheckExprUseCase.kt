package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import javax.inject.Inject

class CheckExprUseCase @Inject constructor(private val repository: CalculationInterface) {
    operator fun invoke(base: Int): Boolean {
        return repository.checkExpr(base)
    }
}