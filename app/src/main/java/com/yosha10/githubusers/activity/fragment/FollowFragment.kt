package com.yosha10.githubusers.activity.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yosha10.githubusers.adapter.ListUsersAdapter
import com.yosha10.githubusers.databinding.FragmentFollowBinding
import com.yosha10.githubusers.model.ItemsItem

class FollowFragment : Fragment() {
    private var position: Int = 0
    private var username: String? = ""
    private lateinit var binding: FragmentFollowBinding
    private val followViewModel by activityViewModels<FollowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_NAME)
        }

        followViewModel.isLoadingFollowers.observe(viewLifecycleOwner){
            showLoading(it)
        }

        followViewModel.isLoadingFollowing.observe(viewLifecycleOwner){
            showLoading(it)
        }

        when(position){
            1 -> if (username!=null) getFollowers(username)
            2 -> if (username!=null) getFollowing(username)
        }
        binding.rvFragment.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun getFollowers(username: String?){
        followViewModel.getFollowers(username)
        followViewModel.listFollowers.observe(viewLifecycleOwner){ data ->
            setAdapterData(data)
        }
    }

    private fun getFollowing(username: String?){
        followViewModel.getFollowing(username)
        followViewModel.listFollowing.observe(viewLifecycleOwner){ data ->
            setAdapterData(data)
        }
    }

    private fun setAdapterData(list: List<ItemsItem>){
        val adapter = ListUsersAdapter(list)
        binding.rvFragment.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean?) {
        binding.progressBar.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("LOADING", binding.progressBar.isVisible)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val isLoading = savedInstanceState?.getBoolean("LOADING", false)
        showLoading(isLoading)
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_NAME = "arg_username"
    }
}