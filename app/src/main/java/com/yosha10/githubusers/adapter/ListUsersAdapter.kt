package com.yosha10.githubusers.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yosha10.githubusers.activity.detail.DetailUserActivity
import com.yosha10.githubusers.activity.detail.DetailUserActivity.Companion.EXTRA_NAME
import com.yosha10.githubusers.ItemsItem
import com.yosha10.githubusers.databinding.ItemUsersBinding

class ListUsersAdapter(private val listUsers: List<ItemsItem>): RecyclerView.Adapter<ListUsersAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount() = listUsers.size

    class ViewHolder(var itemBinding: ItemUsersBinding): RecyclerView.ViewHolder(itemBinding.root){
        fun bind(user: ItemsItem){
            itemBinding.apply {
                tvUsername.text = user.login
                tvLocation.text = user.htmlUrl
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .into(ivImage)
                itemView.setOnClickListener {
//                    Toast.makeText(it.context, "${user.login.toString()}", Toast.LENGTH_SHORT).show()
                    val detailIntent = Intent(it.context, DetailUserActivity::class.java)
                    detailIntent.putExtra(EXTRA_NAME, user.login)
                    itemView.context.startActivity(detailIntent)
                }
            }
        }
    }
}