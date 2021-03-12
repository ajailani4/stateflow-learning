package com.ajailani.stateflowlearning.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajailani.stateflowlearning.data.model.User
import com.ajailani.stateflowlearning.databinding.ActivityMainBinding
import com.ajailani.stateflowlearning.ui.adapter.UsersAdapter
import com.ajailani.stateflowlearning.ui.viewmodel.MainViewModel
import com.ajailani.stateflowlearning.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var usersAdapter: UsersAdapter

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        binding.usersRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)

            usersAdapter = UsersAdapter(arrayListOf())

            addItemDecoration(
                DividerItemDecoration(
                    context, (layoutManager as LinearLayoutManager).orientation
                )
            )

            adapter = usersAdapter
        }
    }

    @ExperimentalCoroutinesApi
    private fun setupObserver() {
        lifecycleScope.launch {
            mainViewModel.fetchUsers()
            val usersList = mainViewModel.getUsers()

            usersList.collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        it.data?.let { usersList -> renderList(usersList) }
                        binding.usersRv.visibility = View.VISIBLE
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.usersRv.visibility = View.GONE
                    }

                    Status.CACHED -> {
                        binding.progressBar.visibility = View.GONE
                        it.data?.let { usersList -> renderList(usersList) }
                        binding.usersRv.visibility = View.VISIBLE
                    }

                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(
                            this@MainActivity, it.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun renderList(usersList: List<User>) {
        usersAdapter.addData(usersList)
        usersAdapter.notifyDataSetChanged()
    }
}