package com.example.revoluttestapp.domain.repository

import com.example.revoluttestapp.domain.entity.CurrencyRates
import io.reactivex.Single

interface RatesRepository {

    fun getRates(baseCurrency: String, forceReload: Boolean = true): Single<CurrencyRates>
}