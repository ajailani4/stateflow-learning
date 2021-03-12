package com.ajailani.stateflowlearning.data.repository

import android.app.Application
import android.util.Log
import com.ajailani.stateflowlearning.data.api.ApiHelper
import com.ajailani.stateflowlearning.data.model.User
import com.ajailani.stateflowlearning.data.roomdb.AppDao
import com.ajailani.stateflowlearning.data.roomdb.AppDatabase
import com.ajailani.stateflowlearning.data.roomdb.NetworkBoundResource
import com.ajailani.stateflowlearning.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MainRepository(private val application: Application, private val apiHelper: ApiHelper) {
    private val appDao: AppDao?

    init {
        val db = AppDatabase.getInstance(application.applicationContext)
        appDao = db?.appDao()
    }

    @ExperimentalCoroutinesApi
    fun getUsers(): Flow<Resource<List<User>>> {
        val networkBoundResource = object : NetworkBoundResource<User, User>(application) {
            override suspend fun saveUsersToDb(usersList: List<User>) {
                appDao?.insertUsers(usersList)
            }

            override suspend fun loadUsersFromDb(): List<User>? {
                return appDao?.getUsers()
            }

            override suspend fun createCall(): Response<List<User>> {
                return apiHelper.getUsers()
            }
        }

        //Execute NetworkBoundResource
        networkBoundResource.execute()

        //Get the result
        return networkBoundResource.result
    }
}