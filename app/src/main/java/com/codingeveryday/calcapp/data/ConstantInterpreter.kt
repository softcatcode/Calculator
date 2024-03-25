package com.codingeveryday.calcapp.data

import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.ConstantInterpreterInterface
import javax.inject.Inject

class ConstantInterpreter @Inject constructor(): ConstantInterpreterInterface {
    override fun decode(name: Char, base: Int) = when (name) {
        CalculationInterface.PI -> MathImplementation.piValue(base)
        else -> throw Exception("undefined constant")
    }
}