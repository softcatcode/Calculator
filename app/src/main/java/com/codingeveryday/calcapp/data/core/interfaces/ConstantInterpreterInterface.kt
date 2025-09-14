package com.codingeveryday.calcapp.data.core.interfaces

import com.codingeveryday.calcapp.domain.entities.Number

interface ConstantInterpreterInterface {
    fun decode(name: Char, base: Int): Number
}