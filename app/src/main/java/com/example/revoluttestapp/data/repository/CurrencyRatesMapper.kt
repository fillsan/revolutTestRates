package com.example.revoluttestapp.data.repository

import com.example.revoluttestapp.data.dto.RatesDto
import com.example.revoluttestapp.domain.entity.Currency
import com.example.revoluttestapp.domain.entity.CurrencyRate
import com.example.revoluttestapp.domain.entity.CurrencyRates
import com.mynameismidori.currencypicker.ExtendedCurrency
import javax.inject.Inject

class CurrencyRatesMapper @Inject constructor() {

    fun getCurrencyRates(ratesDto: RatesDto): CurrencyRates {
        val baseCurrency = getCurrency(ratesDto.baseCurrency)
        val currencyRates = ratesDto.rates.map {
            getCurrencyRate(it)
        }
        return CurrencyRates(baseCurrency, currencyRates)
    }

    private fun getCurrency(currencyCode: String): Currency {
        val currencyData = ExtendedCurrency.CURRENCIES.find {
            currencyCode == it.code
        }
        currencyData.let {
            return Currency(
                code = currencyCode,
                name = it?.name.orEmpty(),
                image = it?.flag ?: -1
            )
        }
    }

    private fun getCurrencyRate(entry: Map.Entry<String, String>): CurrencyRate {
        val currency = getCurrency(entry.key)
        return CurrencyRate(currency, entry.value.toBigDecimal())
    }
}