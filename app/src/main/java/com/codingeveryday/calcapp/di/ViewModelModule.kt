package com.codingeveryday.calcapp.di

import androidx.lifecycle.ViewModel
import com.codingeveryday.calcapp.presentation.CalculatorViewModel
import com.codingeveryday.calcapp.presentation.ConvertNumberSystemViewModel
import com.codingeveryday.calcapp.presentation.HistoryViewModel
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
    @ViewModelKey(HistoryViewModel::class)
    @Binds
    fun bindHistoryViewModel(viewModel: HistoryViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ConvertNumberSystemViewModel::class)
    @Binds
    fun bindConvertNumberSystemViewModel(viewModel: ConvertNumberSystemViewModel): ViewModel
}