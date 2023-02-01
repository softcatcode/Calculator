package com.codingeveryday.calcapp.domain.useCases

import com.codingeveryday.calcapp.domain.interfaces.TransformationInterface
import javax.inject.Inject

class CheckNumberUseCase @Inject constructor(private val repository: TransformationInterface) {
    operator fun invoke(s: String, base: Int): Boolean {
        return repository.checkNumber(s, base)
    }
}