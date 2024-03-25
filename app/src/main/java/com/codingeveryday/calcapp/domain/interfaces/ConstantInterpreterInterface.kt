package com.codingeveryday.calcapp.domain.interfaces

import com.codingeveryday.calcapp.domain.entities.Number

interface ConstantInterpreterInterface {
    fun decode(name: Char, base: Int): Number
}