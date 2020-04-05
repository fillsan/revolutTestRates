package com.example.revoluttestapp.app

import android.app.Application
import com.example.revoluttestapp.core.di.app.AppComponent
import com.example.revoluttestapp.core.di.app.AppModule
import com.example.revoluttestapp.core.di.app.DaggerAppComponent

class RatesApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = buildAppComponent()
    }

    private fun buildAppComponent() =
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
}