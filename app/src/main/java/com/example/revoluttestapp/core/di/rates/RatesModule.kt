package com.example.revoluttestapp.core.di.rates

import com.example.revoluttestapp.data.repository.MainRatesRepository
import com.example.revoluttestapp.data.source.rates.LocalRatesSource
import com.example.revoluttestapp.data.source.rates.LocalRatesSourceImpl
import com.example.revoluttestapp.data.source.rates.RemoteRatesSource
import com.example.revoluttestapp.data.source.rates.RemoteRatesSourceImpl
import com.example.revoluttestapp.domain.repository.RatesRepository
import com.example.revoluttestapp.presentation.error.ErrorHandler
import com.example.revoluttestapp.presentation.error.RatesErrorHandler
import dagger.Binds
import dagger.Module

@Module
abstract class RatesModule {

    @Binds
    @RatesScope
    abstract fun bindRatesRepository(mainRatesRepository: MainRatesRepository) : RatesRepository

    @Binds
    @RatesScope
    abstract fun bindRemoteRatesSource(remoteRatesSourceImpl: RemoteRatesSourceImpl): RemoteRatesSource

    @Binds
    @RatesScope
    abstract fun bindLocalRatesSource(localRatesSourceImpl: LocalRatesSourceImpl): LocalRatesSource

    @Binds
    @RatesScope
    abstract fun bindErrorHandler(ratesErrorHandler: RatesErrorHandler): ErrorHandler
}