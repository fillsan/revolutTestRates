package com.example.revoluttestapp.data.dto

data class RatesDto(
    val baseCurrency: String,
    val rates: Map<String, String>
)