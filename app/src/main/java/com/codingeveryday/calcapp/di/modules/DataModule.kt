package com.codingeveryday.calcapp.di.modules

import android.app.Application
import com.codingeveryday.calcapp.data.database.AppDataBase
import com.codingeveryday.calcapp.data.database.HistoryItemDao
import com.codingeveryday.calcapp.di.annotations.ApplicationScope
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