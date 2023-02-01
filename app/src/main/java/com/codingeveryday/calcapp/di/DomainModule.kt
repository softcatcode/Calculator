package com.codingeveryday.calcapp.di

import com.codingeveryday.calcapp.data.CalculationImplementation
import com.codingeveryday.calcapp.data.HistoryManagerImplementation
import com.codingeveryday.calcapp.data.TransformationImplementation
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import com.codingeveryday.calcapp.domain.interfaces.TransformationInterface
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @Binds
    fun bindCalculationImpl(impl: CalculationImplementation): CalculationInterface

    @Binds
    fun bindHistoryManagerImpl(impl: HistoryManagerImplementation): HistoryManagerInterface

    @Binds
    fun bindTransformationImpl(impl: TransformationImplementation): TransformationInterface
}