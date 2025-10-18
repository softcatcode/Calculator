package com.softcat.domain.useCases

import com.softcat.domain.entities.AngleUnit
import com.softcat.domain.interfaces.CalculationInterface
import timber.log.Timber
import javax.inject.Inject

class CalculateUseCase @Inject constructor(private val repository: CalculationInterface) {
    operator fun invoke(expr: String, base: Int, angleUnit: AngleUnit = AngleUnit.Radians): String {
        Timber.i("${this::class.simpleName}.invoke($expr, $base, $angleUnit)")
        return repository.calculateValue(expr, base, angleUnit)
    }
}