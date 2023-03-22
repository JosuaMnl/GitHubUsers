package com.yosha10.githubusers.api

import com.yosha10.githubusers.model.DetailUserResponse
import com.yosha10.githubusers.ListUsersResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getListUsers(
        @Query("q") q:String
    ): Call<ListUsersResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String?
    ): Call<DetailUserResponse>
}