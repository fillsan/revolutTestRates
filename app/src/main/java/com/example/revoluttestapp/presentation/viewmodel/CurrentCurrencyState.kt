package com.example.revoluttestapp.presentation.viewmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentCurrencyState(val baseCurrency: String, val amount: String): Parcelable