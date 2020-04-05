package com.example.revoluttestapp.core.di.app

import com.example.revoluttestapp.BuildConfig
import com.example.revoluttestapp.data.source.api.RatesApi
import com.example.revoluttestapp.domain.executor.MainSchedulerProvider
import com.example.revoluttestapp.domain.executor.SchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
abstract class NetworkModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.RATES_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideApi(retrofit: Retrofit): RatesApi {
            return retrofit.create(RatesApi::class.java)
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
                okHttpClientBuilder.addInterceptor(loggingInterceptor)
            }
            return okHttpClientBuilder.build()
        }
    }

    @Binds
    @Singleton
    abstract fun provideExecutor(mainExecutor: MainSchedulerProvider): SchedulerProvider
}