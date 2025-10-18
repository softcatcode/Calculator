package com.softcat.data.core.implementations

import com.softcat.data.core.interfaces.ConstantProviderInterface
import com.softcat.data.implementations.TranslationImplementation
import javax.inject.Inject
import com.softcat.domain.entities.Number

class ConstantProvider @Inject constructor(): ConstantProviderInterface {
    override fun piValue(base: Int): Number {
        val translator = TranslationImplementation(MathImplementation(this))
        val pi = Number(ConstantProviderInterface.Companion.PI_DEC_STR, 10)
        return translator.transformNS(pi, base)
    }

    override fun expValue(base: Int): Number {
        val translator = TranslationImplementation(MathImplementation(this))
        val e = Number(ConstantProviderInterface.Companion.EXP_DEC_STR, 10)
        return translator.transformNS(e, base)
    }

    override fun epsValue(base: Int) = Number(mutableListOf(1),
        ConstantProviderInterface.Companion.MAX_ACCURACY_ORDER, true, base)

}