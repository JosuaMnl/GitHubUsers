package com.yosha10.githubusers.activity.theme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.yosha10.githubusers.R
import com.yosha10.githubusers.activity.main.MainViewModel
import com.yosha10.githubusers.databinding.ActivityThemeBinding
import com.yosha10.githubusers.preference.SettingPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class ThemeActivity : AppCompatActivity() {
    private var _activityThemeBinding: ActivityThemeBinding? = null
    private val binding get() = _activityThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityThemeBinding = ActivityThemeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.apply {
            setLogo(R.drawable.icon_action_bar)
            title = "Github User Themes Mode"
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
        }

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding?.switchTheme?.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding?.switchTheme?.isChecked = false
            }
        }

        binding?.switchTheme?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityThemeBinding = null
    }
}