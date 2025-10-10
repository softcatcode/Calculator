package com.codingeveryday.calcapp.di.modules

import com.codingeveryday.calcapp.BuildConfig
import com.codingeveryday.calcapp.CalculatorApplication
import com.codingeveryday.calcapp.di.annotations.ApplicationScope
import com.softcat.data.core.implementations.ConstantInterpreter
import com.softcat.data.core.implementations.ConstantProvider
import com.softcat.data.core.implementations.ExpressionParser
import com.softcat.data.core.implementations.MathImplementation
import com.softcat.data.core.interfaces.ConstantInterpreterInterface
import com.softcat.data.core.interfaces.ConstantProviderInterface
import com.softcat.data.core.interfaces.MathInterface
import com.softcat.data.core.interfaces.ParseExpressionInterface
import com.softcat.data.implementations.CalculationImplementation
import com.softcat.data.implementations.ExpressionBuilder
import com.softcat.data.implementations.HistoryManagerImplementation
import com.softcat.data.implementations.ShareDataImpl
import com.softcat.data.implementations.TranslationImplementation
import com.softcat.domain.interfaces.CalculationInterface
import com.softcat.domain.interfaces.ExpressionBuilderInterface
import com.softcat.domain.interfaces.HistoryManagerInterface
import com.softcat.domain.interfaces.ShareDataInterface
import com.softcat.domain.interfaces.TranslationInterface
import dagger.Binds
import dagger.Module
import dagger.Provides

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

    companion object {
        @Provides
        fun provideSharedDataInterface(): ShareDataInterface {
            return ShareDataImpl(
                "${BuildConfig.APPLICATION_ID}.provider"
            )
        }
    }
}