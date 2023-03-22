package com.yosha10.githubusers.activity.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.yosha10.githubusers.databinding.ActivityDetailUserBinding

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_NAME)
        detailViewModel.getDetailUser(username)

        detailViewModel.detailUserData.observe(this) { dataUser ->
            binding.apply {
                tvDetailName.text = dataUser.name
                tvDetailLocation.text = dataUser.location
                tvDetailRepo.text = dataUser.publicRepos.toString()
                Glide.with(this@DetailUserActivity)
                    .load(dataUser.avatarUrl)
                    .into(ivDetail)
            }
        }

        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        const val EXTRA_NAME = "extra_name"
    }
}