package com.app.mybase.di.modules

import androidx.lifecycle.ViewModelProvider
import com.app.mybase.helper.ViewModelProviderFactory
import com.app.mybase.views.historyactivity.HistoryViewModel
import dagger.Module
import dagger.Provides

@Module
class HistoryModule {

    @Provides
    fun provideViewModelProvider(viewModel: HistoryViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(viewModel)
    }
}
