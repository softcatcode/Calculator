package com.codingeveryday.calcapp.di.modules

import androidx.lifecycle.ViewModel
import com.codingeveryday.calcapp.di.annotations.ViewModelKey
import com.codingeveryday.calcapp.presentation.main.CalculatorViewModel
import com.codingeveryday.calcapp.presentation.translation.ConvertNumberSystemViewModel
import com.codingeveryday.calcapp.presentation.keyboard.KeyboardFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(CalculatorViewModel::class)
    @Binds
    fun bindCalculatorViewModel(viewModel: CalculatorViewModel): ViewModel

    @IntoMap
    @ViewModelKey(KeyboardFragmentViewModel::class)
    @Binds
    fun bindKeyboardViewModel(viewModel: KeyboardFragmentViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ConvertNumberSystemViewModel::class)
    @Binds
    fun bindConvertNumberSystemViewModel(viewModel: ConvertNumberSystemViewModel): ViewModel
}