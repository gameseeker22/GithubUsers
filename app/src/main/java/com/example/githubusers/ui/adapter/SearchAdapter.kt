package com.example.githubusers.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.data.model.User

class SearchAdapter(private val arrayList: ArrayList<User>): RecyclerView.Adapter<DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(position,arrayList[position])
    }

    override fun getItemCount(): Int = arrayList.size
}