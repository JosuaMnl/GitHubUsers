package com.yosha10.githubusers.api

import com.yosha10.githubusers.model.DetailUserResponse
import com.yosha10.githubusers.model.ItemsItem
import com.yosha10.githubusers.model.ListUsersResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getListUsers(
        @Query("q") q:String?
    ): Call<ListUsersResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String?
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String?
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String?
    ): Call<List<ItemsItem>>
}