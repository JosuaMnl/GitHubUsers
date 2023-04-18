package com.yosha10.githubusers.model

import com.google.gson.annotations.SerializedName

data class ListUsersResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("items")
	val items: List<ItemsItem>
)

data class ItemsItem(
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String?,
)
