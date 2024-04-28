package com.codingeveryday.calcapp.data.implementations

import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.domain.interfaces.ConstantProviderInterface
import com.codingeveryday.calcapp.domain.interfaces.ConstantProviderInterface.Companion.EXP_DEC_STR
import com.codingeveryday.calcapp.domain.interfaces.ConstantProviderInterface.Companion.MAX_ACCURACY_ORDER
import com.codingeveryday.calcapp.domain.interfaces.ConstantProviderInterface.Companion.PI_DEC_STR
import javax.inject.Inject

class ConstantProvider @Inject constructor(): ConstantProviderInterface {
    override fun piValue(base: Int): Number {
        val translator = TranslationImplementation(MathImplementation(this))
        val pi = Number(PI_DEC_STR, 10)
        return translator.transformNS(pi, base)
    }

    override fun expValue(base: Int): Number {
        val translator = TranslationImplementation(MathImplementation(this))
        val e = Number(EXP_DEC_STR, 10)
        return translator.transformNS(e, base)
    }

    override fun epsValue(base: Int) = Number(mutableListOf(1), MAX_ACCURACY_ORDER, true, base)

}