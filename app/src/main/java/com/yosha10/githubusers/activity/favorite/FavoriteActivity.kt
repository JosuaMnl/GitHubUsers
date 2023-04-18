package com.yosha10.githubusers.activity.favorite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yosha10.githubusers.R
import com.yosha10.githubusers.activity.ViewModelFactory
import com.yosha10.githubusers.adapter.FavoriteAdapter
import com.yosha10.githubusers.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter

    private lateinit var favoriteUserModel: FavoriteUserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initActionBar("Github User Favorites")
        initViewModel()
        initAdapter()
        initObserveData()
    }

    private fun initAdapter(){
        favoriteAdapter = FavoriteAdapter()
        val layoutManager= LinearLayoutManager(this)
        binding?.rvFavorite?.apply {
            this.layoutManager = layoutManager
            val itemDecoration = DividerItemDecoration(this@FavoriteActivity, layoutManager.orientation)
            addItemDecoration(itemDecoration)
            adapter = favoriteAdapter
        }
    }

    private fun initBinding(){
        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    private fun initViewModel(){
        favoriteUserModel = obtainViewModel(this@FavoriteActivity)
    }

    private fun initObserveData(){
        favoriteUserModel.getAllFavoriteUser().observe(this) { users ->
            favoriteAdapter.setFavoriteUser(users)
        }
    }

    private fun initActionBar(title: String?){
        supportActionBar?.apply {
            setLogo(R.drawable.icon_action_bar)
            this.title = title
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteUserModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        favoriteUserModel.getAllFavoriteUser().observe(this) { users ->
            favoriteAdapter.setFavoriteUser(users)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }
}