package com.yosha10.githubusers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yosha10.githubusers.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    
    companion object{
        private const val TAG = "MainActivity"
        private const val QUERY = "a"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        

        binding.search.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                binding.search.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        setUsersData()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setUsersData(){
        showLoading(true)
        val client = ApiConfig.getApiService().getListUsers(QUERY)
        client.enqueue(object : Callback<ListUsersResponse> {
            override fun onResponse(
                call: Call<ListUsersResponse>,
                response: Response<ListUsersResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful){
                    val listUser = response.body()?.items as List<ItemsItem>
                    val adapter = ListUsersAdapter(listUser)
                    binding.rvUsers.adapter = adapter
                } else {
                    Log.d(TAG, "onFailure: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<ListUsersResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}", )
            }

        })
    }

}