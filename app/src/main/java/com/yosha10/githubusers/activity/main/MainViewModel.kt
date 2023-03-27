package com.yosha10.githubusers.activity.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yosha10.githubusers.ItemsItem
import com.yosha10.githubusers.ListUsersResponse
import com.yosha10.githubusers.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    init {
        setUsersData()
    }

    fun setUsersData(query: String? = QUERY) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUsers(query)
        client.enqueue(object : Callback<ListUsersResponse> {
            override fun onResponse(
                call: Call<ListUsersResponse>,
                response: Response<ListUsersResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                } else {
                    Log.d(TAG, "onFailure: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<ListUsersResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val QUERY = "a"
    }
}