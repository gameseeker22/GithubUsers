package com.example.githubusers.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubusers.data.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM githubUsers")
    fun getAllUsers() : List<User>

    @Query("SELECT * FROM githubUsers WHERE login = :login")
    fun getUser(login: String): User

    @Query("SELECT * FROM githubUsers WHERE login LIKE :login OR note LIKE :note")
    fun searchUser(login:String,note:String) : LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("Update githubUsers set note = :note where login = :login")
    suspend fun updateNote( login:String, note:String )

}