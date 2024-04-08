package com.codingeveryday.calcapp.di

import com.codingeveryday.calcapp.data.CalculationImplementation
import com.codingeveryday.calcapp.data.ConstantInterpreter
import com.codingeveryday.calcapp.data.ExpressionBuilder
import com.codingeveryday.calcapp.data.ExpressionParser
import com.codingeveryday.calcapp.data.HistoryItemMapperImpl
import com.codingeveryday.calcapp.data.HistoryManagerImplementation
import com.codingeveryday.calcapp.data.MathImplementation
import com.codingeveryday.calcapp.data.TranslationImplementation
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.ConstantInterpreterInterface
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import com.codingeveryday.calcapp.domain.interfaces.HistoryItemMapper
import com.codingeveryday.calcapp.domain.interfaces.HistoryManagerInterface
import com.codingeveryday.calcapp.domain.interfaces.MathInterface
import com.codingeveryday.calcapp.domain.interfaces.ParseExpressionInterface
import com.codingeveryday.calcapp.domain.interfaces.TranslationInterface
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

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
    fun bindExpressionBuilder(impl: ExpressionBuilder): ExpressionBuilderInterface

    @Binds
    fun bindHistoryItemMapper(impl: HistoryItemMapperImpl): HistoryItemMapper
}