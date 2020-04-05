package com.example.revoluttestapp.utils

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver

fun <T> Single<T>.check(): TestObserver<T> {
    return this.test().also {
        it.awaitTerminalEvent()
    }
}

fun <T> Observable<T>.check(count: Int = -1): TestObserver<T> {
    return this.test().also {
        if (count == -1) {
            it.awaitTerminalEvent()
        } else {
            it.awaitCount(count)
        }
    }
}

fun Completable.check(): TestObserver<Void> {
    return this.test().also {
        it.awaitTerminalEvent()
    }
}

fun <T> Single<T>.testAwait(testBlock: TestObserver<T>.() -> Unit) {
    this.check().apply {
        testBlock.invoke(this)
    }
}

fun Completable.testAwait(testBlock: TestObserver<Void>.() -> Unit) {
    this.check().apply {
        testBlock.invoke(this)
    }
}

fun <T> Observable<T>.testAwait(count: Int = -1, testBlock: TestObserver<T>.() -> Unit) {
    this.check(count).apply {
        testBlock.invoke(this)
    }
}