package com.example.revoluttestapp.presentation.error

import android.content.Context
import com.example.revoluttestapp.R
import com.example.revoluttestapp.data.exceptions.NetworkException
import com.example.revoluttestapp.data.exceptions.NoLocalDataException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RatesErrorHandler @Inject constructor(
    private val context: Context
) : ErrorHandler {

    override fun getMessage(throwable: Throwable): String {
        val res = when (throwable) {
            is NetworkException, is SocketTimeoutException, is UnknownHostException -> {
                R.string.network_error
            }
            is NoLocalDataException -> { R.string.no_local_data_error }
            else -> R.string.unknown_error
        }
        return context.getString(res)
    }
}