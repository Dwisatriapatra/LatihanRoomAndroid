package com.example.latihanroomandroid.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//user login manager to manage data of user that logged in now
class UserLoginManager(context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore("login-prefs")

    companion object {
        val NAME = preferencesKey<String>("NAME")
        val PASSWORD = preferencesKey<String>("PASSWORD")
        val USERNAME = preferencesKey<String>("USERNAME")
        val BOOLEAN = preferencesKey<Boolean>("BOOLEAN")
    }

    suspend fun saveDataLogin(
        name: String,
        password: String,
        username: String
    ) {
        dataStore.edit {
            it[NAME] = name
            it[PASSWORD] = password
            it[USERNAME] = username
        }
    }

    suspend fun setBoolean(boolean: Boolean){
        dataStore.edit {
            it[BOOLEAN] = boolean
        }
    }

    suspend fun clearDataLogin(){
        dataStore.edit {
            it.clear()
        }
    }

    val name : Flow<String> = dataStore.data.map {
        it[NAME] ?: ""
    }

    val password : Flow<String> = dataStore.data.map {
        it[PASSWORD] ?: ""
    }

    val username : Flow<String> = dataStore.data.map {
        it[USERNAME] ?: ""
    }

    val boolean : Flow<Boolean> = dataStore.data.map {
        it[BOOLEAN] ?: false
    }
}