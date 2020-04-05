package com.example.revoluttestapp.data.repository

import com.example.revoluttestapp.data.source.rates.LocalRatesSource
import com.example.revoluttestapp.data.source.rates.RemoteRatesSource
import com.example.revoluttestapp.domain.entity.CurrencyRates
import com.example.revoluttestapp.domain.repository.RatesRepository
import io.reactivex.Single
import javax.inject.Inject

class MainRatesRepository @Inject constructor(
    private val remoteRatesSource: RemoteRatesSource,
    private val localRatesSource: LocalRatesSource
) : RatesRepository {

    override fun getRates(baseCurrency: String, forceReload: Boolean): Single<CurrencyRates> {
        return if (!forceReload && localRatesSource.hasData(baseCurrency)) {
            localRatesSource.getRates(baseCurrency)
        } else {
            remoteRatesSource.getRates(baseCurrency)
                .doOnSuccess {
                    localRatesSource.saveRates(it)
                }
        }
    }

}