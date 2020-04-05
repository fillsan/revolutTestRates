package com.example.revoluttestapp.presentation.error

interface ErrorHandler {
    fun getMessage(throwable: Throwable): String
}