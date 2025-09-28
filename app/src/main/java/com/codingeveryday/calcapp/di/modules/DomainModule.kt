package com.codingeveryday.calcapp.di.modules

import com.codingeveryday.calcapp.data.implementations.CalculationImplementation
import com.codingeveryday.calcapp.data.core.implementations.ConstantInterpreter
import com.codingeveryday.calcapp.data.core.implementations.ConstantProvider
import com.codingeveryday.calcapp.data.implementations.ExpressionBuilder
import com.codingeveryday.calcapp.data.core.implementations.ExpressionParser
import com.codingeveryday.calcapp.data.implementations.HistoryManagerImplementation
import com.codingeveryday.calcapp.data.core.implementations.MathImplementation
import com.codingeveryday.calcapp.data.implementations.TranslationImplementation
import com.codingeveryday.calcapp.di.annotations.ApplicationScope
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.data.core.interfaces.ConstantInterpreterInterface
import com.codingeveryday.calcapp.data.core.interfaces.ConstantProviderInterface
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import com.codingeveryday.calcapp.data.core.interfaces.MathInterface
import com.codingeveryday.calcapp.data.core.interfaces.ParseExpressionInterface
import com.codingeveryday.calcapp.data.implementations.ShareDataImpl
import com.codingeveryday.calcapp.domain.interfaces.ShareDataInterface
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

    @Binds
    fun bindShareDataInterface(impl: ShareDataImpl): ShareDataInterface
}