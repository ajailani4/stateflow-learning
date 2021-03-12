package com.ajailani.stateflowlearning.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajailani.stateflowlearning.data.model.User
import com.ajailani.stateflowlearning.databinding.ItemUserBinding

class UsersAdapter(
    private val usersList: ArrayList<User>
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    private lateinit var binding: ItemUserBinding

    class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                name.text = user.name
                email.text = user.email
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(usersList[position])
    }

    override fun getItemCount() = usersList.size

    fun addData(list: List<User>) {
        usersList.addAll(list)
    }
}