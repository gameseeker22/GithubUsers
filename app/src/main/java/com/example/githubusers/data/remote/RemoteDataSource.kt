package com.example.githubusers.data.remote

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.example.githubusers.data.model.User
import com.example.githubusers.data.room.LocalDataSource
import com.example.githubusers.utils.Network
import com.example.githubusers.utils.NetworkErrorHandler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action

/**
 * Class for loading the pager function
 */
class RemoteDataSource(
    context : Context,
    private val githubService: AppService,
    private val compositeDisposable: CompositeDisposable
)
    : ItemKeyedDataSource<Long, User>() {

    private val parent = context
    private val networkHandler = NetworkErrorHandler()
    val initialStatus = MutableLiveData<Network>()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<User>) {

        // Update Initial status to control shimmer
        initialStatus.postValue(Network.LOADING)

        // Check if there's data from previous launches
        var localDataList = LocalDataSource.getAllUsers( parent )
        if (localDataList.isNotEmpty()) {
            initialStatus.postValue(Network.LOADED)
            callback.onResult(localDataList)
            return
        }

        //get the initial batch of users
        compositeDisposable.add(githubService.getUsers(0).subscribe({ users ->

            // Update Initial status to remove shimmer
            initialStatus.postValue(Network.LOADED)

            // clear retry since last request succeeded
            networkHandler.setRetry(null)
            //Insert to local database
            LocalDataSource.insertData( parent, users )
            callback.onResult(users)
        }, {
            initialStatus.postValue(Network.FAILED)
            // keep a Completable for future retry
            networkHandler.setRetry(Action { loadInitial(params, callback) })
        }))
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<User>) {

        initialStatus.postValue(Network.LOADING)
        //get the next users batch of users
        compositeDisposable.add(githubService.getUsers(params.key).subscribe({ users ->
            // clear retry since last request succeeded
            networkHandler.setRetry(null)

            //Insert to local database
            LocalDataSource.insertData( parent, users )

            callback.onResult(users)
        }, {
            initialStatus.postValue(Network.FAILED)
            // keep a Completable for future retry
            networkHandler.setRetry(Action { loadAfter(params, callback) })
        }))
    }

    override fun getKey(item: User): Long {
        return item.id
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<User>) {
        // Noting to do here
    }
}