package com.example.githubusers.data.remote

import com.example.githubusers.data.model.User
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppService {

    @GET("/users")
    fun getUsers(@Query("since") userId: Long): Single<List<User>>

    @GET("/users/{username}")
    fun getProfile( @Path("username") username: String ): Single<User>

    companion object {
        fun getService(): AppService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(AppService::class.java)
        }
    }
}