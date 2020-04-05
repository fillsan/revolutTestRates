package com.example.revoluttestapp.data.source.local

import com.example.revoluttestapp.core.di.rates.RatesScope
import com.example.revoluttestapp.domain.entity.CurrencyRates
import javax.inject.Inject

@RatesScope
class LocalData @Inject constructor() {

    var currentRates: CurrencyRates? = null

    fun isDataValid(baseCurrencyCode: String): Boolean {
        val rates = currentRates ?: return false
        return baseCurrencyCode == rates.baseCurrency.code &&
                rates.rates.isNotEmpty()
    }
}