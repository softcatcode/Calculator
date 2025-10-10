package com.softcat.domain.useCases

import com.softcat.domain.interfaces.TranslationInterface
import timber.log.Timber
import com.softcat.domain.entities.Number
import javax.inject.Inject

class NumberSystemTranslationUseCase @Inject constructor(private val repository: TranslationInterface) {
    operator fun invoke(a: Number, toBase: Int): Number {
        Timber.i("${this::class.simpleName}.invoke($a, $toBase)")
        return repository.transformNS(a, toBase)
    }
}