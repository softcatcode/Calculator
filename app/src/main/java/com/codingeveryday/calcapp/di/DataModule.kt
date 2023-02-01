package com.codingeveryday.calcapp.di

import android.app.Application
import com.codingeveryday.calcapp.data.AppDataBase
import com.codingeveryday.calcapp.data.HistoryItemDao
import dagger.Module
import dagger.Provides

@Module
class DataModule {
    @ApplicationScope
    @Provides
    fun provideHistoryItemDao(application: Application): HistoryItemDao {
        return AppDataBase.getInstance(application).getHistoryItemDao()
    }
}