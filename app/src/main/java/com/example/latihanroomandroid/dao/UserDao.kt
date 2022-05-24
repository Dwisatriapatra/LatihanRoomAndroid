package com.example.latihanroomandroid.dao

import androidx.room.*
import com.example.latihanroomandroid.dataclass.Catatan
import com.example.latihanroomandroid.dataclass.User

@Dao
interface UserDao {
    @Insert
    fun insertNewUSer(user: User) : Long
    @Query("SELECT name FROM User WHERE User.username = :username AND User.password = :password")
    fun checkUserLoginData(username: String, password : String) : String
    @Delete
    fun deleteDataUser(user: User) : Int
    @Update
    fun updateDataUser(user: User) : Int
}