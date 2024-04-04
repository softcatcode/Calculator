package com.codingeveryday.calcapp.domain.interfaces

import com.codingeveryday.calcapp.domain.entities.Number

interface TranslationInterface {
    fun transformNS(a: Number, toBase: Int): Number
}