package com.codingeveryday.calcapp.di.modules

import com.codingeveryday.calcapp.data.implementations.CalculationImplementation
import com.codingeveryday.calcapp.data.implementations.ConstantInterpreter
import com.codingeveryday.calcapp.data.implementations.ConstantProvider
import com.codingeveryday.calcapp.data.implementations.ExpressionBuilder
import com.codingeveryday.calcapp.data.implementations.ExpressionParser
import com.codingeveryday.calcapp.data.implementations.HistoryManagerImplementation
import com.codingeveryday.calcapp.data.implementations.MathImplementation
import com.codingeveryday.calcapp.data.implementations.TranslationImplementation
import com.codingeveryday.calcapp.di.annotations.ApplicationScope
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.ConstantInterpreterInterface
import com.codingeveryday.calcapp.domain.interfaces.ConstantProviderInterface
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import com.codingeveryday.calcapp.domain.interfaces.MathInterface
import com.codingeveryday.calcapp.domain.interfaces.ParseExpressionInterface
import com.codingeveryday.calcapp.domain.interfaces.TranslationInterface
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @ApplicationScope
    @Binds
    fun bindExpressionBuilder(impl: ExpressionBuilder): ExpressionBuilderInterface

    @Binds
    fun bindConstantProvider(impl: ConstantProvider): ConstantProviderInterface

    @Binds
    fun bindConstantInterpreter(impl: ConstantInterpreter): ConstantInterpreterInterface

    @Binds
    fun bindMathInterface(impl: MathImplementation): MathInterface

    @Binds
    fun bindParseExpressionInterface(impl: ExpressionParser): ParseExpressionInterface

    @Binds
    fun bindCalculationImpl(impl: CalculationImplementation): CalculationInterface

    @Binds
    fun bindHistoryManagerImpl(impl: HistoryManagerImplementation): HistoryManagerInterface

    @Binds
    fun bindTransformationImpl(impl: TranslationImplementation): TranslationInterface
}