package com.softcat.data.core.interfaces

import com.softcat.domain.entities.Number

interface ConstantInterpreterInterface {
    fun decode(name: Char, base: Int): Number
}