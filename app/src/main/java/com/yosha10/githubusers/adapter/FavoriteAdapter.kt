package com.yosha10.githubusers.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yosha10.githubusers.activity.detail.DetailUserActivity
import com.yosha10.githubusers.database.FavoriteUser
import com.yosha10.githubusers.databinding.ItemUsersBinding

class FavoriteAdapter: ListAdapter<FavoriteUser, FavoriteAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {
    private var favoriteUser = ArrayList<FavoriteUser>()

    fun setFavoriteUser(mFavoriteUser: List<FavoriteUser>){
        favoriteUser.clear()
        favoriteUser.addAll(mFavoriteUser)
        submitList(favoriteUser)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteUsers = favoriteUser[position]
        holder.bind(favoriteUsers)
        holder.itemView.setOnClickListener {
            val detailIntent = Intent(it.context, DetailUserActivity::class.java)
            detailIntent.putExtra(DetailUserActivity.EXTRA_NAME, favoriteUsers.username)
            holder.itemView.context.startActivity(detailIntent)
        }
    }

    override fun getItemCount() = favoriteUser.size

    inner class FavoriteViewHolder(private val binding: ItemUsersBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUser){
            binding.apply {
                tvUsername.text = favoriteUser.username
                Glide.with(itemView.context)
                    .load(favoriteUser.avatarUrl)
                    .centerCrop()
                    .into(ivImage)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<FavoriteUser> =
            object : DiffUtil.ItemCallback<FavoriteUser>() {
                override fun areItemsTheSame(
                    oldItem: FavoriteUser,
                    newItem: FavoriteUser
                ): Boolean {
                    return oldItem.username == newItem.username
                }

                override fun areContentsTheSame(
                    oldItem: FavoriteUser,
                    newItem: FavoriteUser
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }
}