package com.example.revoluttestapp.domain.interactor

import com.example.revoluttestapp.domain.entity.BaseCurrencyItem
import com.example.revoluttestapp.domain.entity.CurrencyItem
import com.example.revoluttestapp.domain.entity.CurrencyRates
import com.example.revoluttestapp.domain.entity.CurrentRateItem
import com.example.revoluttestapp.domain.executor.SchedulerProvider
import com.example.revoluttestapp.domain.repository.RatesRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrenciesInteractor @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val ratesRepository: RatesRepository)  {

    fun createObservable(currentRateItem: CurrentRateItem): Observable<List<CurrencyItem>> {
        return Observable.concat(
                listOf(
                    getCurrencyItemsSingle(currentRateItem, false).toObservable(),
                    getIntervalObservable(currentRateItem)
                )
            )
            .subscribeOn(schedulerProvider.subscribeScheduler)
    }

    private fun getCurrencyItemsList(currentRateItem: CurrentRateItem, currencyRates: CurrencyRates): List<CurrencyItem> {
        val currenciesList = mutableListOf<CurrencyItem>()
        val baseCurrency = BaseCurrencyItem(
            currency = currencyRates.baseCurrency,
            amount = currentRateItem.amount
        )
        val currencyRateList = currencyRates.rates.map {
            CurrencyItem(
                currency = it.currency,
                amount = getScaledAmount(currentRateItem.amount, it.rate)
            )
        }
        return currenciesList.apply {
            add(baseCurrency)
            addAll(currencyRateList)
        }
    }

    private fun getScaledAmount(amount: BigDecimal, rate: BigDecimal): BigDecimal {
        return (amount * rate).setScale(2, HALF_UP)
    }

    private fun getCurrencyItemsSingle(currentRateItem: CurrentRateItem, forceReload: Boolean): Single<List<CurrencyItem>> {
        return ratesRepository.getRates(currentRateItem.baseCurrency, forceReload)
            .map{ rates -> getCurrencyItemsList(currentRateItem, rates) }
    }

    private fun getIntervalObservable(currentRateItem: CurrentRateItem): Observable<List<CurrencyItem>> {
        return if (currentRateItem.amount == BigDecimal.ZERO) {
            Observable.empty()
        } else {
            Flowable.interval(1, TimeUnit.SECONDS)
                .onBackpressureDrop()
                .concatMapSingle {
                    getCurrencyItemsSingle(currentRateItem, true)
                }
                .throttleLatest(1, TimeUnit.SECONDS)
                .toObservable()
        }
    }
}