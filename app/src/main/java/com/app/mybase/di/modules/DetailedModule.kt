package com.app.mybase.di.modules

import androidx.lifecycle.ViewModelProvider
import com.app.mybase.helper.ViewModelProviderFactory
import com.app.mybase.views.detailedactivity.DetailedViewModel
import dagger.Module
import dagger.Provides

@Module
class DetailedModule {

    @Provides
    fun provideViewModelProvider(viewModel: DetailedViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(viewModel)
    }
}