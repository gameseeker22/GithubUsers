package com.example.githubusers.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.githubusers.data.model.User
import com.example.githubusers.data.remote.AppService
import com.example.githubusers.data.remote.RemoteDataFactory
import com.example.githubusers.data.remote.RemoteDataSource
import com.example.githubusers.data.room.LocalDataSource
import com.example.githubusers.utils.Network
import io.reactivex.disposables.CompositeDisposable

class UserViewModel(context:Context) : ViewModel() {

    var parent = context
    lateinit var userList: LiveData<PagedList<User>>
    private val compositeDisposable = CompositeDisposable()
    private val sourceFactory: RemoteDataFactory

    // Page size is determine after first load of data
    private val defaultPageSize = 15

    init {
        sourceFactory = RemoteDataFactory( this.parent, AppService.getService(),compositeDisposable)
        initializePageBuilder(defaultPageSize)
    }

    /**
     *  Initial page size is set for default
     *  But after first load of data the page size
     *  is determine and set to builder.
     */
    fun initializePageBuilder( page_size : Int ) {

        var pageSize = page_size
        if ( pageSize == 0 ) {
            pageSize = defaultPageSize
        }

        val config = PagedList.Config.Builder()
            .setPageSize( pageSize )
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()

        userList = LivePagedListBuilder<Long, User>(sourceFactory, config).build()
    }

    fun refresh() {
        try { sourceFactory.usersLiveData.value!!.invalidate()}
        catch (e: Exception) { e.printStackTrace() }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun searchUser(data:String):LiveData<List<User>>{
        val searchData = "%$data%"
        return LocalDataSource.searchUser(this.parent ,searchData, searchData)
    }

    /**
     *  Return status when fetching of data
     */
    fun getNetworkStatus(): LiveData<Network> =
        Transformations.switchMap<RemoteDataSource, Network>( sourceFactory.usersLiveData) {
        it.initialStatus
    }
}