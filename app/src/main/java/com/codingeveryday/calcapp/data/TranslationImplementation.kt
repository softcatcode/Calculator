package com.codingeveryday.calcapp.data

import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.domain.interfaces.TranslationInterface
import javax.inject.Inject

class TranslationImplementation@Inject constructor(): TranslationInterface {
    override fun transformNS(a: Number, fromBase: Int, toBase: Int): Number {
        TODO("Not yet implemented")
    }
}