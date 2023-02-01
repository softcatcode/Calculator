package com.codingeveryday.calcapp.domain.useCases

import androidx.lifecycle.LiveData
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import javax.inject.Inject

class GetCalcResultUseCase @Inject constructor(private val repository: CalculationInterface) {
    fun getSolution(): LiveData<String> {
        return repository.solution
    }

    fun getExpr(): LiveData<String> {
        return repository.expr
    }
}