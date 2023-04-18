package com.yosha10.githubusers.activity.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.yosha10.githubusers.R
import com.yosha10.githubusers.activity.ViewModelFactory
import com.yosha10.githubusers.adapter.SectionsPagerAdapter
import com.yosha10.githubusers.database.FavoriteUser
import com.yosha10.githubusers.databinding.ActivityDetailUserBinding
import com.yosha10.githubusers.model.DetailUserResponse

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding

    private var linkGithub: String? = ""
    private lateinit var detailViewModel: DetailViewModel
    private var mFavoriteUser: FavoriteUser? = null
//    private var isFavorite: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setting Binding
        initBinding()
        // Get username
        val username = intent.getStringExtra(EXTRA_NAME)
        // Setting ViewModel
        initViewModel()
        // Setting Action Bar
        initActionBar("Github User Details")
        // Setting ViewPager dan Tab
        initViewPager(username)

        // Observe All Data
        initObserveData(username)

        // Click BtnToGithub
        clickBtnToGithub()
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

    private fun clickBtnToGithub(){
        binding.btnLinkToGithub.setOnClickListener {
            val linkURI = Uri.parse(linkGithub)
            val intent = Intent(Intent.ACTION_VIEW, linkURI)
            startActivity(intent)
        }
    }

    private fun setDetailUser(user: DetailUserResponse) {
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
            mFavoriteUser = FavoriteUser(user.login, user.avatarUrl)
        }
    }

    private fun initObserveData(username: String?){
        detailViewModel.getDetailUser(username)

        detailViewModel.detailUserData.observe(this) { data ->
            setDetailUser(data)
        }

        detailViewModel.isLoading.observe(this) { state ->
            showLoading(state)
        }

        detailViewModel.isFavoriteUser(username ?: "").observe(this){ state ->
            val iconBtn = if (state) R.drawable.ic_favorite_fill else R.drawable.ic_favorite_border

            binding.btnFavorite.apply {
                setImageDrawable(ContextCompat.getDrawable(this@DetailUserActivity,  iconBtn))

                setOnClickListener {
                    Log.d("DetailUserActivity", "Btn Favorite Clicked")
                    mFavoriteUser?.let { favoriteUser ->
                        if (state) deleteFavoriteUser(favoriteUser) else addFavoriteUser(favoriteUser)
                    }
                }
            }
        }
    }

    private fun initViewPager(username: String?){
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun initActionBar(title: String?){
        supportActionBar?.apply {
            setLogo(R.drawable.icon_action_bar)
            this.title = title
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
        }
    }

    private fun initBinding(){
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initViewModel(){
        detailViewModel = obtainViewModel(this@DetailUserActivity)
    }

    private fun addFavoriteUser(mFavoriteUser: FavoriteUser){
        detailViewModel.insertUser(mFavoriteUser)
        Toast.makeText(this@DetailUserActivity, "User "+ mFavoriteUser.username +" disimpan", Toast.LENGTH_SHORT).show()
    }

    private fun deleteFavoriteUser(mFavoriteUser: FavoriteUser){
        detailViewModel.deleteUser(mFavoriteUser)
        Toast.makeText(this@DetailUserActivity, "User "+ mFavoriteUser.username +" dihapus", Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    companion object {
        const val EXTRA_NAME = "extra_name"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.title_tab_follower,
            R.string.title_tab_following
        )
    }
}