package com.example.revoluttestapp.data.source.rates

import com.example.revoluttestapp.data.exceptions.NoLocalDataException
import com.example.revoluttestapp.data.source.local.LocalData
import com.example.revoluttestapp.domain.entity.CurrencyRates
import io.reactivex.Single
import javax.inject.Inject

class LocalRatesSourceImpl @Inject constructor(
    private val localData: LocalData
): LocalRatesSource {

    override fun getRates(baseCurrency: String): Single<CurrencyRates> {
        val rates = localData.currentRates
            ?: return Single.error(NoLocalDataException())
        return Single.just(rates)
    }

    override fun saveRates(currencyRates: CurrencyRates) {
        localData.currentRates = currencyRates
    }

    override fun hasData(actualBaseCurrency: String): Boolean {
        return localData.isDataValid(actualBaseCurrency)
    }

}