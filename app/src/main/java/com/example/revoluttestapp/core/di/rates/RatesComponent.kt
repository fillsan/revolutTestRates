package com.example.revoluttestapp.core.di.rates

import com.example.revoluttestapp.presentation.ui.RatesActivity
import dagger.Subcomponent

@RatesScope
@Subcomponent(modules = [RatesModule::class, RatesViewModelModule::class])
interface RatesComponent {
    fun inject(ratesActivity: RatesActivity)
}