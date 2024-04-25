package com.codingeveryday.calcapp.data.implementations

import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.ConstantInterpreterInterface
import com.codingeveryday.calcapp.domain.interfaces.ConstantProviderInterface
import javax.inject.Inject

class ConstantInterpreter @Inject constructor(
    private val constantProvider: ConstantProviderInterface
): ConstantInterpreterInterface {
    override fun decode(name: Char, base: Int) = when (name) {
        CalculationInterface.PI -> constantProvider.piValue(base)
        else -> throw Exception("undefined constant")
    }
}