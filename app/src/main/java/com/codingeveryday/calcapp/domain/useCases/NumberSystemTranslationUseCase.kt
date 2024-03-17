package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.domain.interfaces.TranslationInterface
import javax.inject.Inject

class NumberSystemTranslationUseCase @Inject constructor(private val repository: TranslationInterface) {
    operator fun invoke(a: Number, fromBase: Int, toBase: Int): Number {
        return repository.transformNS(a, fromBase, toBase)
    }
}