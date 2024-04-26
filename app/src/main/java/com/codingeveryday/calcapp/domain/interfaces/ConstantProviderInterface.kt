package com.codingeveryday.calcapp.domain.interfaces

import com.codingeveryday.calcapp.domain.entities.Number

interface ConstantProviderInterface {
    fun piValue(base: Int): Number
    
    fun epsValue(base: Int): Number

    companion object {
        const val MAX_ACCURACY_ORDER = -10
        const val PI_DEC_STR = "3.1415926535897932384626433832795"
    }
}