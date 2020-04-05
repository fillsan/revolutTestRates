package com.example.revoluttestapp.domain.entity

import java.math.BigDecimal

data class Currency(
    val code: String,
    val name: String,
    val image: Int
)

data class CurrentRateItem(
    val baseCurrency: String,
    val amount: BigDecimal
)

data class CurrencyRate(
    val currency: Currency,
    val rate: BigDecimal
)

data class CurrencyRates(
    val baseCurrency: Currency,
    val rates: List<CurrencyRate>
)

open class CurrencyItem(
    val currency: Currency,
    val amount: BigDecimal
)

class BaseCurrencyItem(
    currency: Currency,
    amount: BigDecimal
): CurrencyItem(currency, amount)