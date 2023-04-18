package com.yosha10.githubusers.activity.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yosha10.githubusers.activity.main.MainViewModel
import com.yosha10.githubusers.preference.SettingPreferences

class ViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}