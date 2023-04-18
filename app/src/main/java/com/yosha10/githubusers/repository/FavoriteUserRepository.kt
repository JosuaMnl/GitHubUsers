package com.yosha10.githubusers.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.yosha10.githubusers.database.FavoriteUser
import com.yosha10.githubusers.database.FavoriteUserDao
import com.yosha10.githubusers.database.FavoriteUserDatabase

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao

    init {
        val db = FavoriteUserDatabase.getInstance(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun isFavoriteUser(username: String?): LiveData<Boolean> = mFavoriteUserDao.isFavoriteUser(username)

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUsers()

    suspend fun insert(favoriteUser: FavoriteUser){
        mFavoriteUserDao.insert(favoriteUser)
    }

    suspend fun delete(favoriteUser: FavoriteUser) {
        mFavoriteUserDao.delete(favoriteUser)
    }

    companion object {
        private const val TAG = "FavoriteUserRepository"
    }
}