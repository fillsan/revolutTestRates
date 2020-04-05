package com.example.revoluttestapp.core.di.app

import com.example.revoluttestapp.core.di.rates.RatesComponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class])
@Singleton
interface AppComponent {
    fun ratesComponent(): RatesComponent
}