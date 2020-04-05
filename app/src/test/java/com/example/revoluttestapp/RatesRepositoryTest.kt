package com.example.revoluttestapp

import com.example.revoluttestapp.data.exceptions.NetworkException
import com.example.revoluttestapp.data.exceptions.NoLocalDataException
import com.example.revoluttestapp.data.repository.MainRatesRepository
import com.example.revoluttestapp.data.source.rates.LocalRatesSource
import com.example.revoluttestapp.data.source.rates.RemoteRatesSource
import com.example.revoluttestapp.domain.repository.RatesRepository
import com.example.revoluttestapp.utils.testAwait
import com.nhaarman.mockito_kotlin.*
import com.example.revoluttestapp.MockedData
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class RatesRepositoryTest {

    private lateinit var ratesRepository: RatesRepository

    private val remoteRatesSource: RemoteRatesSource = mock()
    private val localRatesSource: LocalRatesSource = mock()

    @Before
    fun setUp() {
        ratesRepository = MainRatesRepository(
            remoteRatesSource, localRatesSource
        )
    }

    @Test
    fun `get remote data if there is no local data`() {
        val currencyCode = "EUR"
        val rates = MockedData.mockedRates()
        whenever(localRatesSource.hasData(currencyCode)).thenReturn(false)
        whenever(remoteRatesSource.getRates(currencyCode)).thenReturn(Single.just(rates))

        ratesRepository.getRates(currencyCode, false).testAwait {
            assertValue(rates)
            assertNoErrors()
            assertComplete()
            assertTerminated()
        }

        verify(localRatesSource).hasData(currencyCode)
        verify(remoteRatesSource).getRates(currencyCode)
        verify(localRatesSource).saveRates(rates)
        verifyNoMoreInteractions(localRatesSource)
        verifyNoMoreInteractions(remoteRatesSource)
    }

    @Test
    fun `get remote data if force reload`() {
        val currencyCode = "EUR"
        val rates = MockedData.mockedRates()
        whenever(remoteRatesSource.getRates(currencyCode)).thenReturn(Single.just(rates))

        ratesRepository.getRates(currencyCode, true).testAwait {
            assertValue(rates)
            assertNoErrors()
            assertComplete()
            assertTerminated()
        }

        verify(remoteRatesSource).getRates(currencyCode)
        verify(localRatesSource).saveRates(rates)
        verifyNoMoreInteractions(localRatesSource)
        verifyNoMoreInteractions(remoteRatesSource)
    }

    @Test
    fun `if there is local data - get it`() {
        val currencyCode = "EUR"
        val rates = MockedData.mockedRates()
        whenever(localRatesSource.hasData(currencyCode)).thenReturn(true)
        whenever(localRatesSource.getRates(currencyCode))
            .thenReturn(Single.just(rates))

        ratesRepository.getRates(currencyCode, false).testAwait {
            assertValue(rates)
            assertNoErrors()
            assertComplete()
            assertTerminated()
        }

        verify(localRatesSource).hasData(currencyCode)
        verify(localRatesSource).getRates(currencyCode)
        verifyNoMoreInteractions(localRatesSource)
        verifyZeroInteractions(remoteRatesSource)
    }

    @Test
    fun `emit connection error`() {
        val currencyCode = "EUR"
        whenever(remoteRatesSource.getRates(currencyCode))
            .thenReturn(Single.error(NetworkException()))

        ratesRepository.getRates(currencyCode, true).testAwait {
            assertNoValues()
            assertError(NetworkException::class.java)
            assertNotComplete()
            assertTerminated()
        }

        verify(remoteRatesSource).getRates(currencyCode)
        verifyNoMoreInteractions(remoteRatesSource)
        verifyZeroInteractions(localRatesSource)
    }

    @Test
    fun `emit no local data error`() {
        val currencyCode = "EUR"
        whenever(localRatesSource.hasData(currencyCode)).thenReturn(true)
        whenever(localRatesSource.getRates(currencyCode))
            .thenReturn(Single.error(NoLocalDataException()))

        ratesRepository.getRates(currencyCode, false).testAwait {
            assertNoValues()
            assertError(NoLocalDataException::class.java)
            assertNotComplete()
            assertTerminated()
        }

        verify(localRatesSource).hasData(currencyCode)
        verify(localRatesSource).getRates(currencyCode)
        verifyNoMoreInteractions(localRatesSource)
        verifyZeroInteractions(remoteRatesSource)
    }

}