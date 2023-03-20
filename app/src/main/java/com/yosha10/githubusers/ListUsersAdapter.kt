package com.yosha10.githubusers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yosha10.githubusers.databinding.ItemUsersBinding

class ListUsersAdapter(private val listUsers: List<ItemsItem>): RecyclerView.Adapter<ListUsersAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUsersAdapter.ViewHolder {
        val binding =ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListUsersAdapter.ViewHolder, position: Int) {
        holder.binding.tvUsername.text = listUsers[position].login
        holder.binding.tvLocation.text = listUsers[position].htmlUrl
        Glide.with(holder.itemView.context)
            .load(listUsers[position].avatarUrl)
            .into(holder.binding.ivImage)
    }

    override fun getItemCount() = listUsers.size

    class ViewHolder(var binding: ItemUsersBinding): RecyclerView.ViewHolder(binding.root)
}