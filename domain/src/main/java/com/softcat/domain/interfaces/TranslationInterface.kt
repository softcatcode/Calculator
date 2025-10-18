package com.softcat.domain.interfaces

import com.softcat.domain.entities.Number

interface TranslationInterface {
    fun transformNS(a: Number, toBase: Int): Number
}