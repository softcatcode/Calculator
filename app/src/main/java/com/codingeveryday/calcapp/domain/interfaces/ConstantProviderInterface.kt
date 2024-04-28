package com.codingeveryday.calcapp.domain.interfaces

import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.POINT

interface ConstantProviderInterface {
    fun piValue(base: Int): Number

    fun expValue(base: Int): Number
    
    fun epsValue(base: Int): Number

    companion object {
        const val MAX_ACCURACY_ORDER = -10
        const val PI_DEC_STR = "3${POINT}1415926535897932384626433832795"
        const val EXP_DEC_STR = "2${POINT}71828182845904523536028747135266"
    }
}