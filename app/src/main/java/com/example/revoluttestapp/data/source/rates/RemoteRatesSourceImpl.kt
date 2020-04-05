package com.example.revoluttestapp.data.source.rates

import com.example.revoluttestapp.data.exceptions.NetworkException
import com.example.revoluttestapp.data.repository.CurrencyRatesMapper
import com.example.revoluttestapp.data.source.api.RatesApi
import com.example.revoluttestapp.domain.entity.CurrencyRates
import io.reactivex.Single
import javax.inject.Inject

class RemoteRatesSourceImpl @Inject constructor(
    private val ratesApi: RatesApi,
    private val currencyMapperCurrency: CurrencyRatesMapper
): RemoteRatesSource {

    override fun getRates(baseCurrency: String): Single<CurrencyRates> {
        return ratesApi.getCurrencyRates(baseCurrency)
            .flatMap{
                if (!it.isSuccessful && it.body() == null) {
                    Single.error(NetworkException())
                } else {
                    Single.just(it.body())
                }
            }
            .map { currencyMapperCurrency.getCurrencyRates(it) }
    }

}