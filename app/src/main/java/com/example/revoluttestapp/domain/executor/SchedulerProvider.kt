package com.example.revoluttestapp.domain.executor

import io.reactivex.Scheduler

interface SchedulerProvider {
    val subscribeScheduler: Scheduler
}