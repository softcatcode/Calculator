package com.codingeveryday.calcapp.di.modules

import android.app.Application
import com.codingeveryday.calcapp.di.annotations.ApplicationScope
import com.softcat.data.database.AppDataBase
import com.softcat.data.database.HistoryItemDao
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