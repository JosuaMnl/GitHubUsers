package com.yosha10.githubusers

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getListUsers(
        @Query("q") q:String
    ): Call<ListUsersResponse>
}