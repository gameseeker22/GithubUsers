package com.example.githubusers.data.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.githubusers.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalDataSource {

    companion object {

        var appDB: AppDatabase? = null
        var user : LiveData<User>? = null

        private fun initializeDB(context: Context) : AppDatabase {
            return AppDatabase.getDatabase( context )
        }

        fun insertData(context: Context, users : List<User> ) {
            appDB = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                appDB!!.userDao().insertAll(users)
            }
        }
        fun updateData(context: Context, users : User ) {
            appDB = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                appDB!!.userDao().insert(users)
            }
        }

        fun updateNote(context: Context, login:String, note:String ) {
            appDB = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                appDB!!.userDao().updateNote(login,note)
            }
        }

        fun getAllUsers( context: Context) : List<User> {
            appDB = initializeDB(context)
            return appDB!!.userDao().getAllUsers()
        }

        fun getUser( context: Context, login: String) : User {
            appDB = initializeDB(context)
            return appDB!!.userDao().getUser(login)
        }

        fun searchUser( context: Context, login:String, note:String) : LiveData<List<User>> {
            appDB = initializeDB(context)
            return appDB!!.userDao().searchUser(login,note)
        }
    }
}