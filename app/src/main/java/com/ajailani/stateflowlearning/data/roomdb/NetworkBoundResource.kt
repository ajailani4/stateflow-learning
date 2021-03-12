package com.ajailani.stateflowlearning.data.roomdb

import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import com.ajailani.stateflowlearning.utils.NetworkHelper
import com.ajailani.stateflowlearning.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

abstract class NetworkBoundResource<ResultType, RequestType>(private val application: Application) {
    // Flow that represents the Resource
    @ExperimentalCoroutinesApi
    val result = MutableStateFlow<Resource<List<ResultType>>>(Resource.loading(null))

    // Save users list to Room Database
    @WorkerThread
    protected abstract suspend fun saveUsersToDb(usersList: List<ResultType>)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    /*@WorkerThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean*/

    // Get users list from Room Database
    @WorkerThread
    protected abstract suspend fun loadUsersFromDb(): List<ResultType>?

    // Create API call
    @WorkerThread
    protected abstract suspend fun createCall(): Response<List<ResultType>>

    @ExperimentalCoroutinesApi
    fun execute() {
        CoroutineScope(Dispatchers.IO).launch {
            val localData = loadUsersFromDb()

            if (NetworkHelper(application.applicationContext).isNetworkConnected()) {
                result.value = Resource.loading(localData)

                if (localData?.isEmpty() == true) {
                    val remoteData = Resource.success(createCall().body())
                    result.value = remoteData

                    // Save data to Room Database
                    remoteData.data?.let { saveUsersToDb(it) }
                } else {
                    result.value = Resource.success(localData)
                }
            } else {
                result.value = Resource.cached(localData)
            }
        }
    }
}