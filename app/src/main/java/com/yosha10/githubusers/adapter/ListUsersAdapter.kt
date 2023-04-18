package com.yosha10.githubusers.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yosha10.githubusers.activity.detail.DetailUserActivity
import com.yosha10.githubusers.activity.detail.DetailUserActivity.Companion.EXTRA_NAME
import com.yosha10.githubusers.databinding.ItemUsersBinding
import com.yosha10.githubusers.model.ItemsItem

class ListUsersAdapter(private val listUsers: List<ItemsItem>): RecyclerView.Adapter<ListUsersAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount() = listUsers.size

    class ViewHolder(private var itemBinding: ItemUsersBinding): RecyclerView.ViewHolder(itemBinding.root){
        fun bind(user: ItemsItem){
            itemBinding.apply {
                tvUsername.text = user.login
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .into(ivImage)
                itemView.setOnClickListener {
                    val detailIntent = Intent(it.context, DetailUserActivity::class.java)
                    detailIntent.putExtra(EXTRA_NAME, user.login)
                    itemView.context.startActivity(detailIntent)
                }
            }
        }
    }
}