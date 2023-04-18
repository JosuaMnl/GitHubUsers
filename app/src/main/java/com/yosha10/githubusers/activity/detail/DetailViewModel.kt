package com.yosha10.githubusers.activity.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yosha10.githubusers.api.ApiConfig
import com.yosha10.githubusers.database.FavoriteUser
import com.yosha10.githubusers.model.DetailUserResponse
import com.yosha10.githubusers.repository.FavoriteUserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val application: Application): ViewModel() {
    private val _detailUserData = MutableLiveData<DetailUserResponse>()
    val detailUserData: LiveData<DetailUserResponse> = _detailUserData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val favoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    init {
        getDetailUser()
    }

    fun insertUser(favoriteUser: FavoriteUser){
        viewModelScope.launch {
            favoriteUserRepository.insert(favoriteUser)
        }
    }

    fun deleteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            favoriteUserRepository.delete(favoriteUser)
        }
    }

    fun isFavoriteUser(username: String?) =
        favoriteUserRepository.isFavoriteUser(username)

    fun getDetailUser(username: String?=""){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _detailUserData.postValue(response.body())
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "DetailViewModel"
    }
}