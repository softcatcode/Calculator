package com.softcat.data.core.implementations

import com.softcat.data.core.interfaces.ConstantInterpreterInterface
import com.softcat.data.core.interfaces.ConstantProviderInterface
import com.softcat.domain.interfaces.CalculationInterface
import javax.inject.Inject

class ConstantInterpreter @Inject constructor(
    private val constantProvider: ConstantProviderInterface
): ConstantInterpreterInterface {
    override fun decode(name: Char, base: Int) = when (name) {
        CalculationInterface.PI -> constantProvider.piValue(base)
        else -> throw Exception("undefined constant")
    }
}