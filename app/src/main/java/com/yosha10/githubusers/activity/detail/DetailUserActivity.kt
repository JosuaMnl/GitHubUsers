package com.yosha10.githubusers.activity.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.yosha10.githubusers.R
import com.yosha10.githubusers.activity.fragment.FollowFragment
import com.yosha10.githubusers.adapter.SectionsPagerAdapter
import com.yosha10.githubusers.databinding.ActivityDetailUserBinding
import com.yosha10.githubusers.model.DetailUserResponse

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private var linkGithub: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setLogo(R.drawable.icon_action_bar)
        supportActionBar?.title = "Github User Details"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        val username = intent.getStringExtra(EXTRA_NAME)
        detailViewModel.getDetailUser(username)

        detailViewModel.detailUserData.observe(this) { data ->
            setDetailUser(data)
        }

        detailViewModel.isLoading.observe(this){state ->
            showLoading(state)
        }

        // Tab and ViewPager
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.btnLinkToGithub.setOnClickListener {
            val linkURI = Uri.parse(linkGithub)
            val intent = Intent(Intent.ACTION_VIEW, linkURI)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("LOADING", binding.progressBar.isVisible)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val isLoading = savedInstanceState.getBoolean("LOADING", false)
        showLoading(isLoading)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setDetailUser(user: DetailUserResponse){
        linkGithub = user.htmlUrl
        binding.apply {
            tvName.text = user.name
            tvUsername.text = user.login
            tvLocation.text = user.location ?: "-"
            tvCompany.text = user.company ?: "-"
            tvLinkWebsite.text = if (user.blog === "") user.blog else "-"
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()
            tvRepo.text = user.publicRepos.toString()
            Glide.with(this@DetailUserActivity)
                .load(user.avatarUrl)
                .into(ivDetail)
        }
    }

    companion object{
        const val EXTRA_NAME = "extra_name"
        const val TAG = "DetailUserActivity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.title_tab_follower,
            R.string.title_tab_following
        )
    }
}