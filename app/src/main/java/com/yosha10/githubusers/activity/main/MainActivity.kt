package com.yosha10.githubusers.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yosha10.githubusers.R
import com.yosha10.githubusers.activity.favorite.FavoriteActivity
import com.yosha10.githubusers.activity.theme.ThemeActivity
import com.yosha10.githubusers.activity.theme.ViewModelFactory
import com.yosha10.githubusers.adapter.ListUsersAdapter
import com.yosha10.githubusers.databinding.ActivityMainBinding
import com.yosha10.githubusers.model.ItemsItem
import com.yosha10.githubusers.preference.SettingPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == ""){
                    Toast.makeText(this@MainActivity, "Username yang dicari tidak ada!", Toast.LENGTH_SHORT).show()
                } else {
                    setSearchData(query.toString())
                    binding?.search?.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Setting Action Bar
        supportActionBar?.apply {
            setLogo(R.drawable.icon_action_bar)
            title = "Github Users"
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
        }

        mainViewModel.listUser.observe(this) {
            setAdapterData(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding?.rvUsers?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvUsers?.addItemDecoration(itemDecoration)
    }

    private fun setSearchData(query: String?){
        mainViewModel.setUsersData(query)
        mainViewModel.listUser.observe(this) {
            setAdapterData(it)
        }
    }

    private fun setAdapterData(listUsers: List<ItemsItem>){
        val adapter = ListUsersAdapter(listUsers)
        binding?.rvUsers?.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_favorite -> startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
            R.id.action_mode -> startActivity(Intent(this@MainActivity, ThemeActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }
}