package com.codingeveryday.calcapp.di

import android.app.Application
import com.codingeveryday.calcapp.data.CalcService
import com.codingeveryday.calcapp.presentation.CalculatorFragment
import com.codingeveryday.calcapp.presentation.ToNumberSystemFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DomainModule::class,
        ViewModelModule::class,
        JavaImplModule::class,
        DataModule::class
    ]
)
interface ApplicationComponent {

    fun inject(calcFragment: CalculatorFragment)

    fun inject(nsFragment: ToNumberSystemFragment)

    fun inject(service: CalcService)

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}