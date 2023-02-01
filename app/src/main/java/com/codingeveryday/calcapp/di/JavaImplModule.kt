package com.codingeveryday.calcapp.di

import com.codingeveryday.calcapp.data.CalculationImplementation
import com.codingeveryday.calcapp.data.TransformationImplementation
import dagger.Module
import dagger.Provides

@Module
class JavaImplModule {

    @ApplicationScope
    @Provides
    fun provideCalculationImpl(): CalculationImplementation {
        return CalculationImplementation()
    }

    @ApplicationScope
    @Provides
    fun provideTransformationImpl(): TransformationImplementation {
        return TransformationImplementation()
    }
}