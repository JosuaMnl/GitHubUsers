package com.yosha10.githubusers.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yosha10.githubusers.ItemsItem
import com.yosha10.githubusers.ListUsersResponse
import com.yosha10.githubusers.R
import com.yosha10.githubusers.adapter.ListUsersAdapter
import com.yosha10.githubusers.api.ApiConfig
import com.yosha10.githubusers.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                setSearchData(query.toString())
                binding.search.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        supportActionBar?.setLogo(R.drawable.icon_action_bar)
        supportActionBar?.title = "Github Users"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        mainViewModel.listUser.observe(this) {
            setAdapterData(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)
    }

    private fun setSearchData(query: String?){
        mainViewModel.setUsersData(query)
        mainViewModel.listUser.observe(this) {
            setAdapterData(it)
        }
    }

    private fun setAdapterData(listUsers: List<ItemsItem>){
        val adapter = ListUsersAdapter(listUsers)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }



}