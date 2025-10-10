package com.codingeveryday.calcapp

import android.app.Application
import com.codingeveryday.calcapp.di.annotations.ApplicationScope
import com.codingeveryday.calcapp.di.modules.DataModule
import com.codingeveryday.calcapp.di.modules.DomainModule
import com.softcat.data.implementations.CalculationImplementation
import com.softcat.data.implementations.HistoryManagerImplementation
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DomainModule::class,
        DataModule::class
    ]
)
interface TestingComponent {

    fun getHistoryManager(): HistoryManagerImplementation

    fun getCalculationImplementation(): CalculationImplementation

    @Component.Factory
    interface TestingComponentFactory {
        fun create(
            @BindsInstance application: Application,
        ): TestingComponent
    }
}