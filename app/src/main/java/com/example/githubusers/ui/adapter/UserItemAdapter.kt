package com.example.githubusers.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.R
import com.example.githubusers.data.model.User

class UserItemAdapter: PagedListAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user -> DataViewHolder.create(parent)
            R.layout.item_loading -> LoadingViewHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user -> (holder as DataViewHolder).bind(position,getItem(position))
            R.layout.item_loading -> (holder as LoadingViewHolder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if ( position == itemCount - 1) {
            R.layout.item_loading
        } else {
            R.layout.item_user
        }
    }
    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    companion object {
        val UserDiffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

}