package com.yosha10.githubusers.activity.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yosha10.githubusers.database.FavoriteUser
import com.yosha10.githubusers.repository.FavoriteUserRepository

class FavoriteUserModel(private val application: Application): ViewModel() {
    private val favoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> =
        favoriteUserRepository.getAllFavoriteUser()
}