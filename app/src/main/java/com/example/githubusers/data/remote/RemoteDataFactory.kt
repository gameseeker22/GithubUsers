package com.example.githubusers.data.remote

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.githubusers.data.model.User
import io.reactivex.disposables.CompositeDisposable

class RemoteDataFactory(context : Context,
                        private val githubService: AppService,
                        private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Long, User>() {

    var usersLiveData = MutableLiveData<RemoteDataSource>()

    private val parent = context

    override fun create(): DataSource<Long, User> {
        var usersDataSource = RemoteDataSource(parent, githubService, compositeDisposable)
        usersLiveData.postValue(usersDataSource)
        return usersDataSource
    }
}