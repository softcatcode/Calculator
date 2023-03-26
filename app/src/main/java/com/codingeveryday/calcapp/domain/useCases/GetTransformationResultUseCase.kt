package com.codingeveryday.calcapp.domain.useCases

import androidx.lifecycle.LiveData
import com.codingeveryday.calcapp.domain.interfaces.TransformationInterface
import javax.inject.Inject

class GetTransformationResultUseCase @Inject constructor(private val repository: TransformationInterface) {
    operator fun invoke(): LiveData<String> {
        return repository.result
    }
}