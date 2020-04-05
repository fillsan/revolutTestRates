package com.example.revoluttestapp

import com.example.revoluttestapp.data.exceptions.NetworkException
import com.example.revoluttestapp.domain.entity.Currency
import com.example.revoluttestapp.domain.entity.CurrencyItem
import com.example.revoluttestapp.domain.entity.CurrentRateItem
import com.example.revoluttestapp.domain.interactor.CurrenciesInteractor
import com.example.revoluttestapp.domain.repository.RatesRepository
import com.example.revoluttestapp.utils.testAwait
import com.nhaarman.mockito_kotlin.*
import com.example.revoluttestapp.MockedData.mockedRates
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class CurrenciesInteractorTest {

    private lateinit var currenciesInteractor: CurrenciesInteractor

    private val testSchedulerProvider = TestSchedulerProvider()
    private val ratesRepository: RatesRepository = mock()

    @Before
    fun setUp() {
        currenciesInteractor = CurrenciesInteractor(testSchedulerProvider, ratesRepository)
    }

    @Test
    fun `get rates and subscribe to updates`() {
        val currencyCode = "EUR"
        val amount = "10".toBigDecimal()

        whenever(ratesRepository.getRates(currencyCode, false))
            .thenReturn(Single.just(mockedRates()))
        whenever(ratesRepository.getRates(currencyCode, true))
            .thenReturn(Single.just(mockedRates()))

        val currentRateItem = CurrentRateItem(currencyCode, amount)
        currenciesInteractor.createObservable(currentRateItem).testAwait(3) {
            assertValueCount(3)
            assertNoErrors()
            assertNotComplete()
            assertNotTerminated()
        }

        verify(ratesRepository).getRates(currencyCode, false)
        verify(ratesRepository, atLeast(2)).getRates(currencyCode, true)
        verifyNoMoreInteractions(ratesRepository)
    }

    @Test
    fun `unsubscribe from updates if there was an error`() {
        val currencyCode = "EUR"
        val amount = "10".toBigDecimal()

        whenever(ratesRepository.getRates(currencyCode, false))
            .thenReturn(Single.error(NetworkException()))

        val currentRateItem = CurrentRateItem(currencyCode, amount)
        currenciesInteractor.createObservable(currentRateItem).testAwait {
            assertNoValues()
            assertError(NetworkException::class.java)
            assertNotComplete()
            assertTerminated()
        }

        verify(ratesRepository).getRates(currencyCode, false)
        verifyNoMoreInteractions(ratesRepository)
    }

    @Test
    fun `don't get updates if there was an error`() {
        val currencyCode = "EUR"
        val amount = "10".toBigDecimal()

        whenever(ratesRepository.getRates(currencyCode, false))
            .thenReturn(Single.just(mockedRates()))
        whenever(ratesRepository.getRates(currencyCode, true))
            .thenReturn(Single.error(NetworkException()))

        val currentRateItem = CurrentRateItem(currencyCode, amount)
        currenciesInteractor.createObservable(currentRateItem).testAwait {
            assertValueCount(1)
            assertError(NetworkException::class.java)
            assertNotComplete()
            assertTerminated()
        }

        verify(ratesRepository).getRates(currencyCode, false)
        verify(ratesRepository).getRates(currencyCode, true)
        verifyNoMoreInteractions(ratesRepository)
    }

    @Test
    fun `don't get rates if amount is 0`() {
        val currencyCode = "EUR"
        val amount = "0".toBigDecimal()

        whenever(ratesRepository.getRates(currencyCode, false))
            .thenReturn(Single.just(mockedRates()))

        val currentRateItem = CurrentRateItem(currencyCode, amount)
        currenciesInteractor.createObservable(currentRateItem).testAwait {
            assertValueCount(1)
            assertNoErrors()
            assertComplete()
            assertTerminated()
        }

        verify(ratesRepository).getRates(currencyCode, false)
        verifyNoMoreInteractions(ratesRepository)
    }

    @Test
    fun `currency convert correctly`() {
        val currencyCode = "EUR"
        val amount = "10".toBigDecimal()

        whenever(ratesRepository.getRates(currencyCode, false))
            .thenReturn(Single.just(mockedRates()))
        whenever(ratesRepository.getRates(currencyCode, true))
            .thenReturn(Single.just(mockedRates()))

        val currentRateItem = CurrentRateItem(currencyCode, amount)
        currenciesInteractor.createObservable(currentRateItem).testAwait(3) {
            assertValueCount(3)
            assertNoErrors()
            assertNotComplete()
            assertNotTerminated()
            assertValueAt(0) { isConvertCorrectly(it) }
            assertValueAt(1) { isConvertCorrectly(it) }
            assertValueAt(2) { isConvertCorrectly(it) }
        }

        verify(ratesRepository).getRates(currencyCode, false)
        verify(ratesRepository, atLeast(2)).getRates(currencyCode, true)
        verifyNoMoreInteractions(ratesRepository)
    }

    private fun isConvertCorrectly(result: List<CurrencyItem>): Boolean {
        val rates = mockedRates()
        if (result.size != 4) {
            return false
        }
        val expectedBaseCurrency = Currency(
            code = "1",
            name = "Currency1",
            image = 0
        )

        if (result[0].currency != expectedBaseCurrency) {
            return false
        }
        result.forEachIndexed { i, currency ->
            if (i == 0)
                return@forEachIndexed

            if (currency.currency != rates.rates[i - 1].currency) {
                return false
            }
        }

        if (result[0].amount != "10".toBigDecimal()) {
            return false
        }
        if (result[1].amount != "10.00".toBigDecimal()) {
            return false
        }
        if (result[2].amount != "20.00".toBigDecimal()) {
            return false
        }
        if (result[3].amount != "30.00".toBigDecimal()) {
            return false
        }
        return true
    }

}