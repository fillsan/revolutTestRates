package com.example.revoluttestapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.revoluttestapp.domain.entity.CurrencyItem
import com.example.revoluttestapp.domain.entity.CurrentRateItem
import com.example.revoluttestapp.domain.interactor.CurrenciesInteractor
import com.example.revoluttestapp.presentation.error.ErrorHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val currenciesInteractor: CurrenciesInteractor,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    var currentCurrencyState =
        CurrentCurrencyState(
            "EUR",
            "1"
        )

    private val compositeDisposable = CompositeDisposable()

    private val _data = MutableLiveData<List<CurrencyItem>>()
    val data: LiveData<List<CurrencyItem>> = _data

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<String> = _error.map {
        errorHandler.getMessage(it)
    }

    fun onNewAmount(currencyItem: CurrencyItem, amount: String) {
        val newCurrentCurrencyState =
            CurrentCurrencyState(
                currencyItem.currency.code,
                amount
            )
        subscribeToUpdates(newCurrentCurrencyState)
    }

    fun subscribeToUpdates(state: CurrentCurrencyState? = null) {
        if (state != null) {
            currentCurrencyState = state
        }
        val currentRateItem = CurrentRateItem(
            currentCurrencyState.baseCurrency,
            currentCurrencyState.amount.toBigDecimal()
        )
        compositeDisposable.clear()
        compositeDisposable.add(
            currenciesInteractor.createObservable(currentRateItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _data.value = it },
                    { _error.value = it }
                ))
    }

    fun unsubscribeToUpdates() {
        clearDisposable()
    }

    override fun onCleared() {
        super.onCleared()
        clearDisposable()
    }

    private fun clearDisposable() {
        compositeDisposable.clear()
    }
}