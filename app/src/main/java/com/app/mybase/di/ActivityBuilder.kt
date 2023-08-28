package com.app.mybase.di

import com.app.mybase.MainActivity
import com.app.mybase.di.modules.DetailedModule
import com.app.mybase.di.modules.HistoryModule
import com.app.mybase.di.modules.MainModule
import com.app.mybase.views.detailedactivity.DetailedActivity
import com.app.mybase.views.historyactivity.HistoryActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun provideMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [DetailedModule::class])
    abstract fun provideDetailedActivity(): DetailedActivity

    @ContributesAndroidInjector(modules = [HistoryModule::class])
    abstract fun provideHistoryActivity(): HistoryActivity

}