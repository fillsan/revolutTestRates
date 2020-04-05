package com.example.revoluttestapp.core.di.rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.revoluttestapp.core.di.app.ViewModelFactory
import com.example.revoluttestapp.core.di.app.ViewModelKey
import com.example.revoluttestapp.presentation.viewmodel.RatesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RatesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RatesViewModel::class)
    abstract fun bindRatesViewModel(ratesViewModel: RatesViewModel): ViewModel

    @Binds
    @RatesScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}