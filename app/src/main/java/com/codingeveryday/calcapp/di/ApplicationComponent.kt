package com.codingeveryday.calcapp.di

import android.app.Application
import com.codingeveryday.calcapp.data.CalcService
import com.codingeveryday.calcapp.di.annotations.ApplicationScope
import com.codingeveryday.calcapp.di.modules.DataModule
import com.codingeveryday.calcapp.di.modules.DomainModule
import com.codingeveryday.calcapp.di.modules.ViewModelModule
import com.codingeveryday.calcapp.domain.interfaces.ExpressionBuilderInterface
import com.codingeveryday.calcapp.presentation.main.CalculatorFragment
import com.codingeveryday.calcapp.presentation.keyboard.KeyboardDialog
import com.codingeveryday.calcapp.presentation.translation.ToNumberSystemFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DomainModule::class,
        ViewModelModule::class,
        DataModule::class
    ]
)
interface ApplicationComponent {

    fun inject(calcFragment: CalculatorFragment)

    fun inject(nsFragment: ToNumberSystemFragment)

    fun inject(service: CalcService)

    fun inject(dialog: KeyboardDialog)

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance exprBuilder: ExpressionBuilderInterface
        ): ApplicationComponent
    }
}