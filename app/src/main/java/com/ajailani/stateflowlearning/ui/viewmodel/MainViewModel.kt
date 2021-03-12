package com.ajailani.stateflowlearning.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ajailani.stateflowlearning.data.api.ApiHelper
import com.ajailani.stateflowlearning.data.api.RetrofitBuilder
import com.ajailani.stateflowlearning.data.model.User
import com.ajailani.stateflowlearning.data.repository.MainRepository
import com.ajailani.stateflowlearning.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val apiHelper = ApiHelper(RetrofitBuilder.apiService)
    private val mainRepository = MainRepository(application, apiHelper)

    @ExperimentalCoroutinesApi
    private val userList = MutableStateFlow<Resource<List<User>>>(Resource.loading(null))

    @ExperimentalCoroutinesApi
    fun fetchUsers() {
        viewModelScope.launch {
            mainRepository.getUsers()
                .catch { e ->
                    userList.value = (Resource.error(e.toString(), null))
                }
                .collect {
                    userList.value = it
                }
        }
    }

    @ExperimentalCoroutinesApi
    fun getUsers(): StateFlow<Resource<List<User>>> {
        return userList
    }
}