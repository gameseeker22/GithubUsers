package com.example.githubusers.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusers.data.model.User
import com.example.githubusers.data.remote.AppService
import com.example.githubusers.data.room.LocalDataSource
import com.example.githubusers.utils.NetworkErrorHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel(val context : Context): ViewModel() {

    var profile = MutableLiveData<User>()
    private val networkHandler = NetworkErrorHandler()

    @SuppressLint("CheckResult")
    fun fetchProfile(user : User) {

        val username = user.login
        if ( user.name.isNullOrEmpty() ) {

            AppService.getService().getProfile( username )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    profile.postValue(it)
                    LocalDataSource.updateData( context, it )
                }, { throwable ->
                    Timber.e(throwable.message)
                    networkHandler.setRetry(Action { fetchProfile(user) })
                })
        }
        else {
            CoroutineScope(Dispatchers.IO).launch {
                var info = LocalDataSource.getUser( context, username )
                profile.postValue(info)
            }
        }
    }

    fun updateNote(note:String) {
        profile.value?.login?.let { LocalDataSource.updateNote(context,it,note) }
    }
}