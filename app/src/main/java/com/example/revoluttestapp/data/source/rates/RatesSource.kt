package com.example.revoluttestapp.data.source.rates

import com.example.revoluttestapp.domain.entity.CurrencyRates
import io.reactivex.Single

interface RatesSource {
    fun getRates(baseCurrency: String): Single<CurrencyRates>
}

interface LocalRatesSource: RatesSource {
    fun saveRates(currencyRates: CurrencyRates)
    fun hasData(actualBaseCurrency: String): Boolean
}

interface RemoteRatesSource: RatesSource