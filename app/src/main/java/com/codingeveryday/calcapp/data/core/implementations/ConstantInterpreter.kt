package com.codingeveryday.calcapp.data.core.implementations

import com.codingeveryday.calcapp.data.core.interfaces.ConstantInterpreterInterface
import com.codingeveryday.calcapp.data.core.interfaces.ConstantProviderInterface
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import javax.inject.Inject

class ConstantInterpreter @Inject constructor(
    private val constantProvider: ConstantProviderInterface
): ConstantInterpreterInterface {
    override fun decode(name: Char, base: Int) = when (name) {
        CalculationInterface.PI -> constantProvider.piValue(base)
        else -> throw Exception("undefined constant")
    }
}