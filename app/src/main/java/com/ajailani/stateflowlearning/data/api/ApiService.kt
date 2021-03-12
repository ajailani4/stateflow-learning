package com.ajailani.stateflowlearning.data.api

import com.ajailani.stateflowlearning.data.model.User
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}