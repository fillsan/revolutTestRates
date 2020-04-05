package com.example.revoluttestapp

import com.example.revoluttestapp.domain.executor.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulerProvider: SchedulerProvider {

    override val subscribeScheduler: Scheduler
        get() = Schedulers.trampoline()
}