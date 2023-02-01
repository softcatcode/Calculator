package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.interfaces.TransformationInterface
import javax.inject.Inject

class NumberSystemTranslationUseCase @Inject constructor(private val repository: TransformationInterface) {
    operator fun invoke(s: String, baseSource: Int, baseDest: Int) {
        repository.transformNS(s, baseSource, baseDest)
    }
}