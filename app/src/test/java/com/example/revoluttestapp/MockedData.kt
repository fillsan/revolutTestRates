package com.example.revoluttestapp

import com.example.revoluttestapp.domain.entity.Currency
import com.example.revoluttestapp.domain.entity.CurrencyRate
import com.example.revoluttestapp.domain.entity.CurrencyRates

object MockedData {

    fun mockedRates(): CurrencyRates {
        val baseCurrency = Currency(
            code = "1",
            name = "Currency1",
            image = 0
        )
        return CurrencyRates(
            baseCurrency = baseCurrency,
            rates = listOf(
                CurrencyRate(
                    currency = Currency("2", "Currency2", 0),
                    rate = 1.0.toBigDecimal()
                ),
                CurrencyRate(
                    currency = Currency("3", "Currency3", 0),
                    rate = 2.0.toBigDecimal()
                ),
                CurrencyRate(
                    currency = Currency("4", "Currency4", 0),
                    rate = 3.0.toBigDecimal()
                )
            )
        )
    }

}