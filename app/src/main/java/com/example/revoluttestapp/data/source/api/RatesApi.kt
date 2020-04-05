package com.example.revoluttestapp.data.source.api

import com.example.revoluttestapp.data.dto.RatesDto
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {

    @GET("api/android/latest")
    fun getCurrencyRates(@Query("base") baseCurrency: String): Single<Response<RatesDto>>

}