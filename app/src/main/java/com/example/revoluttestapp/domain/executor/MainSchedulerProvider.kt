package com.example.revoluttestapp.domain.executor

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainSchedulerProvider @Inject constructor(): SchedulerProvider {
    override val subscribeScheduler: Scheduler
        get() = Schedulers.io()
}