package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.entities.Number
import com.codingeveryday.calcapp.domain.interfaces.TranslationInterface
import timber.log.Timber
import javax.inject.Inject

class NumberSystemTranslationUseCase @Inject constructor(private val repository: TranslationInterface) {
    operator fun invoke(a: Number, toBase: Int): Number {
        Timber.i("${this::class.simpleName}.invoke($a, $toBase)")
        return repository.transformNS(a, toBase)
    }
}