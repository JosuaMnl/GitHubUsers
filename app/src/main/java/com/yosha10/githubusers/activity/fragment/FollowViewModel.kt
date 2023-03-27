package com.yosha10.githubusers.activity.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yosha10.githubusers.ItemsItem
import com.yosha10.githubusers.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel: ViewModel() {
    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers: LiveData<List<ItemsItem>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: LiveData<List<ItemsItem>> = _listFollowing

    private val _isLoadingFollowers = MutableLiveData<Boolean>()
    val isLoadingFollowers: LiveData<Boolean> = _isLoadingFollowers

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    init {
        getFollowers()
        getFollowing()
    }

    fun getFollowers(username: String?=""){
        _isLoadingFollowers.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoadingFollowers.value = false
                if (response.isSuccessful){
                    _listFollowers.value = response.body()
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollowers.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowing(username: String?=""){
        _isLoadingFollowing.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful){
                    _listFollowing.value = response.body()
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollowing.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "FollowViewModel"
    }

}