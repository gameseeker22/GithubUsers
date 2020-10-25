package com.example.githubusers.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import kotlin.concurrent.schedule

class NetworkErrorHandler {

    private var maxRetryBackoff : Long = 10000 // Max retry time
    private var retryTime : Long = 2000 // Initial retry time

    private val compositeDisposable = CompositeDisposable()
    private var retryCompletable: Completable? = null

    /**
     * Retry actions when failed to fetch data
     */
    fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)

            // Run retry connection handler
            retryHandler()
        }
    }

    /**
    *Retry function with exponential backoff
    */
    private fun retryHandler() {

        Timer("RetryNetwork", false).schedule(retryTime) {
            // Add 2secs to retry timer
            if ( retryTime < maxRetryBackoff ) {
                retryTime += 2000
            }
            retry()
        }
    }

    private fun retry() {

        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, { throwable ->
                    Timber.e(throwable.message) }))
        }
    }

    /**
     * Method to check Internet availability.
     * Calls during initial boot and paging
     */
    fun checkInternetConnectivity(context: Context) : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }
}